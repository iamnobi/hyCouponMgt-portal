package com.cherri.acs_portal.listener;

import com.cherri.acs_portal.annotation.LogInfo;
import com.cherri.acs_portal.dto.acs_integrator.CipherData;
import com.cherri.acs_portal.dto.acs_integrator.CipherReqDTO;
import com.cherri.acs_portal.dto.acs_integrator.CipherResDTO;
import com.cherri.acs_portal.dto.acs_integrator.HttpRequestParameter;
import com.cherri.acs_portal.util.OkHttpConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.OceanException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

@Log4j2
public class DatabasePropertiesListener
  implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @LogInfo(end = true)
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();

        String location = environment.getProperty("portal.datasource.location");
        if (StringUtils.isNotBlank(location)) {
            log.warn("[onApplicationEvent] portal datasource location={}", location);
            int decryptErrorRetryCount =
              Integer.parseInt(environment.getProperty("descrypt.error.retry.count", "3"));
            long decryptErrorRetryGap =
              Long.parseLong(environment.getProperty("descrypt.error.retry.gap", "5000"));
            Path dataSourceLocation = Paths.get(location.trim());

            try (InputStream input = new FileInputStream(dataSourceLocation.toUri().getPath())) {
                Properties prop = new Properties();
                prop.load(input);

                // DB URL, username放入PropertySources
                MutablePropertySources propertySources = environment.getPropertySources();
                String acsPortalDbUrl = prop.getProperty("db.url");
                String acsPortalDbUsername = prop.getProperty("db.username");
                String fiscProdDbUrl = prop.getProperty("fisc.housekeeping.prod41.db.url");
                String fiscProdDbUsername = prop
                  .getProperty("fisc.housekeeping.prod41.db.username");

                Map<String, Object> map = new HashMap<>();
                map.put("spring.datasource.url", acsPortalDbUrl);
                map.put("spring.datasource.username", acsPortalDbUsername);
                map.put("fisc.housekeeping.prod41.datasource.jdbc-url", fiscProdDbUrl);
                map.put("fisc.housekeeping.prod41.datasource.username", fiscProdDbUsername);

                // 解密，若發生錯誤則重試
                String acsPortalDbPwd = null;
                String fiscProd41DbPwd = null;
                for (int i = 1; i <= decryptErrorRetryCount; i++) {
                    try {
                        acsPortalDbPwd = decryptDbPwd(environment, prop.getProperty("db.password"));
                        fiscProd41DbPwd =
                          decryptDbPwd(environment,
                            prop.getProperty("fisc.housekeeping.prod41.db.password"));
                        break;
                    } catch (Exception e) {
                        log.error("[onApplicationEvent] unknown exception, Decrypt retry count={}",
                          i, e);
                        if (i == decryptErrorRetryCount) {
                            throw e;
                        }
                    }
                    Thread.sleep(decryptErrorRetryGap);
                }
                if (acsPortalDbPwd == null) {
                    throw new OceanException("Decrypt error.");
                }

                // 將解密後的密碼放入propertySources
                map.put("spring.datasource.password", acsPortalDbPwd);
                map.put("fisc.housekeeping.prod41.datasource.password", fiscProd41DbPwd);
                propertySources.addFirst(new MapPropertySource("PreProcessedMap", map));
                log.info("[onApplicationEvent] Decrypt data source done.");

            } catch (FileNotFoundException e) {
                throw new MissingResourceException(
                  "Decrypt data source file does not exist or not readable", null, null);
            } catch (Exception e) {
                throw new OceanException(e.getMessage());
            }
        }
    }

    private String decryptDbPwd(ConfigurableEnvironment environment, String cipherPwd)
      throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OkHttpConnector httpConnector = new OkHttpConnector(environment, objectMapper);

        String cipherKey = "dbPwd";
        CipherReqDTO req = new CipherReqDTO();
        req.addData(cipherKey, cipherPwd);
        String reqJson = objectMapper.writeValueAsString(req);

        String acsiDecryptUrl = environment.getProperty("acs.integrator.url") + "/decrypt";
        HttpRequestParameter reqParams =
          new HttpRequestParameter(acsiDecryptUrl, reqJson, CipherResDTO.class);

        Response response = httpConnector.post(reqParams);
        if (!response.isSuccessful()) {
            throw new OceanException("Decrypt error, integrator response error.");
        }
        String result = response.body().string();
        CipherResDTO res = objectMapper.readValue(result, CipherResDTO.class);
        if (res.isSuccess()) {
            return getValue(res, cipherKey);
        }
        String errMsg =
          String.format(
            "Decrypt error, integrator response status:%s, message:%s",
            res.getStatus(), res.getMsg());
        throw new NoSuchElementException(errMsg);
    }

    private String getValue(CipherResDTO cipherRes, String targetKey)
      throws NoSuchElementException {
        return cipherRes
          .getCipherData(targetKey)
          .map(CipherData::getValue)
          .orElseThrow(
            () -> new NoSuchElementException("Decrypt error, no such value by key:" + targetKey));
    }
}
