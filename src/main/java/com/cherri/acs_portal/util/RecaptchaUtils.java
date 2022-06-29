package com.cherri.acs_portal.util;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class RecaptchaUtils {

    public boolean isTokenValid(String recaptchaToken) {
        try {
            URI verifyUri = URI.create(String.format(
                    "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
                    EnvironmentConstants.RECAPTCHA_SECRET, recaptchaToken));
            Request request = new Request.Builder()
                    .url(verifyUri.toURL())
                    .get()
                    .build();
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder
                    .hostnameVerifier((hostname, session) -> true)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(10, TimeUnit.SECONDS);
            if (EnvironmentConstants.IS_PROXY_ENABLED) {
                Proxy proxy =
                        new Proxy(
                                Proxy.Type.HTTP,
                                new InetSocketAddress(
                                        EnvironmentConstants.PROXY_HOST,
                                        EnvironmentConstants.PROXY_PORT));
                clientBuilder.proxy(proxy);
            }
            Response response = clientBuilder.build().newCall(request).execute();
            JsonObject jsonObject = new Gson().fromJson(response.body().string(), JsonObject.class);
            boolean success = jsonObject.get("success").getAsBoolean();
            log.debug("isTokenValid: " + success);
            return success;
        } catch (Exception e) {
            log.error("isTokenValid, error: " + e);
        }
        return false;
    }

}
