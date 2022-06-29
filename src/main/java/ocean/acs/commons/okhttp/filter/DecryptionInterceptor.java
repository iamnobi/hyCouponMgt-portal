package ocean.acs.commons.okhttp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWEObject;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.okhttp.dto.ReqResEncryptionDTO;
import ocean.acs.commons.okhttp.utils.JweUtil;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * DecryptionInterceptor
 *
 * @author Alan Chen
 */
@Log4j2
public class DecryptionInterceptor implements Interceptor {

    /**
     * 與integrator的連線加解密密鑰
     */
    private final byte[] secretKey;

    private final ObjectMapper objectMapper;

    public DecryptionInterceptor(byte[] secretKey, ObjectMapper objectMapper) {
        if (secretKey.length != 32) {
            throw new IllegalArgumentException(
              "Failed in application.properties, the parameter:[req.res.encrypt.secret-key] must be 32 characters.");
        }
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String cipherResponseString = response.body().string();

        if (log.isDebugEnabled()) {
            log.debug("[intercept] cipher response body={}", cipherResponseString);
        }

        // Decrypt response body
        String rawResponseBody = decryptResponse(cipherResponseString);

        if (log.isDebugEnabled()) {
            log.debug("[intercept] decrypt response body={}", rawResponseBody);
        }

        // build new response
        ResponseBody newResponseBody =
          newResponseBody(response.header("Content-Type"), rawResponseBody);
        return response.newBuilder().body(newResponseBody).build();
    }

    private ResponseBody newResponseBody(String contentTypeString, String rawResponseBodyString) {
        return ResponseBody.create(getMediaType(contentTypeString), rawResponseBodyString);
    }

    private MediaType getMediaType(String contentTypeString) {
        if (null == contentTypeString) {
            return MediaType.parse("application/json;charset=UTF-8");
        }
        return MediaType.parse(Objects.requireNonNull(contentTypeString));
    }

    private String decryptResponse(String response) throws OceanException {
        try {
            ReqResEncryptionDTO reqResEncryptionDto =
              objectMapper.readValue(response, ReqResEncryptionDTO.class);
            JWEObject jweObject = JWEObject.parse(reqResEncryptionDto.getToken());
            return JweUtil.decryptJwe(secretKey, jweObject);
        } catch (Exception e) {
            String errMsg =
              "[decryptResponse] unknown exception";
            log.error(errMsg, e);
            throw new OceanException(errMsg, e);
        }
    }
}
