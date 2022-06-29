package com.cherri.acs_portal.config;

import com.cherri.acs_portal.service.SystemSettingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AppStartupRunner implements ApplicationRunner {

    private final SystemSettingService systemSettingService;

    @Autowired
    public AppStartupRunner(SystemSettingService systemSettingService) {
        this.systemSettingService = systemSettingService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        systemSettingService.loadKeyStore();
    }
}
