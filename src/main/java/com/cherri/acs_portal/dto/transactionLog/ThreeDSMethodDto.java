package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.ThreeDSMethodLogDO;
import org.springframework.beans.BeanUtils;

@Data
@ToString
@NoArgsConstructor
@JsonInclude
public class ThreeDSMethodDto {

    private String threeDSMethodNotificationURL;
    private String BrowserAcceptHeader;
    private String browserUserAgent;
    private Boolean browserJavaEnabled;
    private String browserLanguage;
    private String browserColorDepth;
    private String browserScreenHeight;
    private String browserScreenWidth;
    private String browserTZ;
    private Boolean browserPrivateMode;


    public static ThreeDSMethodDto valueOf(ThreeDSMethodLogDO threeDSMethodLog) {
        ThreeDSMethodDto threeDSMethodDto = new ThreeDSMethodDto();
        BeanUtils.copyProperties(threeDSMethodLog, threeDSMethodDto);
        return threeDSMethodDto;
    }
}
