package com.cherri.acs_portal.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.BrowserErrorCodeEnum;
import ocean.acs.commons.enumerator.ErrorFunctionType;
import ocean.acs.models.dao.BrowserErrorLogDAO;
import ocean.acs.models.dao.ErrorCodeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class BrowserAbnormalJobService {

    private final BrowserErrorLogDAO browserErrorLogDAO;
    private final ErrorCodeDAO errorCodeDAO;

    @Autowired
    public BrowserAbnormalJobService(
            BrowserErrorLogDAO browserErrorLogDAO,
            ErrorCodeDAO errorCodeDAO) {
        this.browserErrorLogDAO = browserErrorLogDAO;
        this.errorCodeDAO = errorCodeDAO;
    }

    /** 產生瀏覽器異常紀錄統計 */
    @Transactional(rollbackFor = Exception.class)
    public boolean staticBrowserErrorLog(String operator) {
        List<Integer> errorCodes =
                errorCodeDAO.getErrorCodeMappingByErrorGroupId(ErrorFunctionType.FAILURE_REASON.getId())
                        .stream()
                        .map(e -> Integer.parseInt(e.getCode()))
                        .filter(BrowserErrorCodeEnum::isBrowserErrorCode)
                        .collect(Collectors.toList());

        return browserErrorLogDAO.generateDailyBrowserErrorLog(operator, errorCodes);
    }

}
