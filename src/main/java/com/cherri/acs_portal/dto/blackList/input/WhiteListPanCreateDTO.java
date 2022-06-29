package com.cherri.acs_portal.dto.blackList.input;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.util.HmacUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.WhiteListPanDO;

/** 新增白名單Request參數物件 */
public class WhiteListPanCreateDTO extends ManualPanCreateDTO {

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.WHITE_LIST;
    }

    /** 取得Hash卡號 */
    public String getHashedCardNumber() {
        return HmacUtils.encrypt(this.getRealCardNumber(), EnvironmentConstants.hmacHashKey);
    }

    public static WhiteListPanCreateDTO valueOfWhiteListPanDO(WhiteListPanDO entity) {
        WhiteListPanCreateDTO dto = new WhiteListPanCreateDTO();
        dto.setId(entity.getId());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
        return dto;
    }

}
