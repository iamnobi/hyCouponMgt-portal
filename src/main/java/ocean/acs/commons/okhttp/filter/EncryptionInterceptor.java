package ocean.acs.commons.okhttp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.okhttp.dto.ReqResEncryptionDTO;
import ocean.acs.commons.okhttp.utils.JweUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * EncryptionInterceptor
 *
 * @author Alan Chen
 */
@Log4j2
public class EncryptionInterceptor implements Interceptor {

    /**
     * 與integrator的連線加解密密鑰
     */
    private final byte[] secretKey;

    private final ObjectMapper objectMapper;

    public EncryptionInterceptor(byte[] secretKey, ObjectMapper objectMapper) {
        if (secretKey.length != 32) {
            throw new IllegalArgumentException(
              "Failed in application.properties, the parameter:[req.res.encrypt.secret-key] must be 32 characters.");
        }
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        RequestBody rawBody = request.body();
        if (null == rawBody) {
            return chain.proceed(request);
        }

        // Encrypt request body
        String rawBodyString = requestBodyToString(rawBody);
        String encryptedRequestBody = encryptRequest(rawBodyString);

        if (log.isDebugEnabled()) {
            log.debug("[intercept] encrypt request body={}", encryptedRequestBody);
        }

        // build new request
        RequestBody newRequestBody =
          createRequestBody(request.header("Content-Type"), encryptedRequestBody);
        Request newRequest =
          request
            .newBuilder()
            .header("Content-Length", String.valueOf(newRequestBody.contentLength()))
            .method(request.method(), newRequestBody)
            .build();

        return chain.proceed(newRequest);
    }

    private RequestBody createRequestBody(String contentTypeString, String encryptedRequestBody) {
        return RequestBody.create(getMediaType(contentTypeString), encryptedRequestBody);
    }

    private MediaType getMediaType(String contentTypeString) {
        if (null == contentTypeString) {
            return MediaType.parse("application/json;charset=UTF-8");
        }
        return MediaType.parse(Objects.requireNonNull(contentTypeString));
    }

    private String encryptRequest(String plaintextRequest) throws OceanException {
        try {
            String kid = UUID.randomUUID().toString();
            String jweRequestBody =
              JweUtil.encryptJwe(EncryptionMethod.A128CBC_HS256, secretKey, plaintextRequest, kid);
            return objectMapper.writeValueAsString(new ReqResEncryptionDTO(jweRequestBody));
        } catch (Exception e) {
            String errMsg =
              "[encryptRequest] unknown exception";
            log.error(errMsg, e);
            throw new OceanException(errMsg, e);
        }
    }

    private String requestBodyToString(RequestBody requestBody) throws IOException {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readUtf8();
    }
}
