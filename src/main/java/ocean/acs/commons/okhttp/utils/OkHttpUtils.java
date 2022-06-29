package ocean.acs.commons.okhttp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.okhttp.dto.HttpRequestParameter;
import ocean.acs.commons.okhttp.filter.DecryptionInterceptor;
import ocean.acs.commons.okhttp.filter.EncryptionInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * OkHttpUtils
 *
 * @author Alan Chen
 */
@Log4j2
@Component
@ConditionalOnProperty(name = "kernel.properties.ignore", havingValue = "false", matchIfMissing = true)
public class OkHttpUtils {

    /**
     * 與integrator的連線加解密是否啟用
     */
    private final boolean reqResEncryptEnable;
    /**
     * 與integrator的連線加解密密鑰
     */
    private final byte[] secretKey;

    private int connectTimeout;
    private int readTimeout;


    private static final String PROTOCOL_VERSION = "TLSv1.2";

    private static final MediaType APPLICATION_JSON =
      MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client;

    private static final Integer MAX_REQUESTS = 128;
    private static final Integer MAX_REQUESTS_PER_HOST = 16;

    private final ObjectMapper objectMapper;

    private final TrustManager[] trustAllCerts =
      new TrustManager[]{
        new X509TrustManager() {

            @Override
            public void checkClientTrusted(
              java.security.cert.X509Certificate[] chain, String authType)
              throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
              java.security.cert.X509Certificate[] chain, String authType)
              throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }
      };

    @PostConstruct
    private void init() {
        if (reqResEncryptEnable && secretKey.length != 32) {
            throw new IllegalArgumentException(
              "Failed in application.properties, the parameter:[req.res.encrypt.secret-key] must be 32 characters.");
        }
    }

    @Autowired
    public OkHttpUtils(Environment environment, ObjectMapper objectMapper) {
        this.reqResEncryptEnable =
          Boolean.parseBoolean(environment.getProperty("req.res.encrypt.enable", "true"));
        this.secretKey = environment.getProperty("req.res.encrypt.secret-key").getBytes();

        this.objectMapper = objectMapper;

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(MAX_REQUESTS);
        dispatcher.setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);

        int maxIdle =
          Integer.parseInt(environment.getProperty("okhttp.max.idle.connections", "10"));
        int keepAlive =
          Integer.parseInt(environment.getProperty("okhttp.keep.alive.duration", "5"));
        this.connectTimeout =
          Integer.parseInt(environment.getProperty("okhttp.connect.timeout", "5"));
        this.readTimeout = Integer
          .parseInt(environment.getProperty("okhttp.read.timeout", "5"));

        SSLContext sc;
        try {
            sc = SSLContext.getInstance(PROTOCOL_VERSION);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

        OkHttpClient.Builder clientBuilder =
          new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(maxIdle, keepAlive, TimeUnit.MINUTES))
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
            .hostnameVerifier(OkHttpUtils::verify)
            .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

        // 啟用Integrator加密連線
        if (reqResEncryptEnable) {
            clientBuilder.addInterceptor(new EncryptionInterceptor(secretKey, objectMapper));
            clientBuilder.addInterceptor(new DecryptionInterceptor(secretKey, objectMapper));
        }

        client = clientBuilder.build();
    }

    private static boolean verify(String s, SSLSession sslSession) {
        // Trust all hostname
        return true;
    }

    public Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        log.debug("[get] request url={}", url);
        return execute(request);
    }

    public Response post(HttpRequestParameter reqParam) throws IOException {
        RequestBody body = RequestBody.create(APPLICATION_JSON, reqParam.getReqJson());
        log.debug("[post] post params={}", reqParam.getReqJson());
        Request request = new Request.Builder().url(reqParam.getUrl()).post(body).build();
        return execute(request);
    }

    private Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    /**
     * Do http post without connection pool and encrypt request response
     */
    public ocean.acs.commons.okhttp.dto.Response oncePost(String url, String json)
      throws IOException {
        SSLContext sc;
        try {
            sc = SSLContext.getInstance(PROTOCOL_VERSION);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

        OkHttpClient.Builder clientBuilder =
          new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .hostnameVerifier(OkHttpUtils::verify)
            .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

        RequestBody body = RequestBody.create(APPLICATION_JSON, json);

        Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body)
          .build();

        try (Response response = clientBuilder.build().newCall(request).execute()) {
            /* Convert okhttp response to custom response object is to prevent okhttp response close */
            ocean.acs.commons.okhttp.dto.Response r = new ocean.acs.commons.okhttp.dto.Response();
            r.setSuccess(response.isSuccessful());
            r.setBody(response.body().string());
            r.setMessage(response.message());
            r.setCode(response.code());
            return r;
        }
    }
}
