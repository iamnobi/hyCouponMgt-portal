package com.cherri.acs_portal.controller.response;

import lombok.Data;
import ocean.acs.models.data_object.entity.SystemSettingDO;

import java.util.List;

@Data
public class GeneralSettingDTO {
    private Integer preqResendInterval;
    private Integer areqConnectionTimeout;
    private Integer rreqConnectionTimeout;
    private Integer areqReadTimeout;
    private Integer rreqReadTimeout;

    public static GeneralSettingDTO valueOf(List<SystemSettingDO> generalSettingList) {
        GeneralSettingDTO generalSettingDTO = new GeneralSettingDTO();
        generalSettingList.forEach(generalSetting -> {
            if ("preq.resend.interval".equals(generalSetting.getKey())) {
                generalSettingDTO.setPreqResendInterval(Integer.valueOf(generalSetting.getValue()));
            } else if ("areq.connection.timeout".equals(generalSetting.getKey())) {
                generalSettingDTO.setAreqConnectionTimeout(Integer.valueOf(generalSetting.getValue()));
            }  else if ("rreq.connection.timeout".equals(generalSetting.getKey())) {
                generalSettingDTO.setRreqConnectionTimeout(Integer.valueOf(generalSetting.getValue()));
            }  else if ("areq.read.timeout".equals(generalSetting.getKey())) {
                generalSettingDTO.setAreqReadTimeout(Integer.valueOf(generalSetting.getValue()));
            }  else if ("rreq.read.timeout".equals(generalSetting.getKey())) {
                generalSettingDTO.setRreqReadTimeout(Integer.valueOf(generalSetting.getValue()));
            }
        });

        return generalSettingDTO;
    }
}
