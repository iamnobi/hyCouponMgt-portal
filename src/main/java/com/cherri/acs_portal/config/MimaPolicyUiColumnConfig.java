package com.cherri.acs_portal.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MimaPolicyUiColumnConfig
 *
 * @author Alan Chen
 */
@Getter
@Setter
@Component
@Slf4j
@ConfigurationProperties(prefix = "mima.policy.column.rule")
public class MimaPolicyUiColumnConfig {

    /** 密碼重試次數下限 */
    private int retryCountMin;
    /** 密碼重試次數上限 */
    private int retryCountMax;
    /** 密碼過期天數下限 */
    private int freshIntervalNumMin;
    /** 密碼過期天數上限 */
    private int freshIntervalNumMax;
    /** 帳號未使用天數下限 */
    private int accountIdleDayMin;
    /** 帳號未使用天數上限 */
    private int accountIdleDayMax;
    /** 密碼不可相同追朔數量 */
    private int historyDuplicateCount;
    /** 密碼最小長度 */
    private int lengthMin;
    /** 密碼最大長度 */
    private int lengthMax;
}

