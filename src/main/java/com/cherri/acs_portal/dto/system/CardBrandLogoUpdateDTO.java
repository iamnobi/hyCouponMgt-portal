package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.dto.audit.AuditableDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.CardBrandLogoDO;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CardBrandLogoUpdateDTO extends AuditableDTO {

    private String cardBrand;
    private long fileSize;
    private AuditStatus auditStatus;
    private String user;

    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.SYS_CARD_BRAND_LOGO;
    }

    public static CardBrandLogoUpdateDTO valueOf(CardBrandLogoDO cbl) {
        CardBrandLogoUpdateDTO dto = new CardBrandLogoUpdateDTO();
        dto.setCardBrand(cbl.getCardBrand());
        dto.setFileSize(cbl.getImageSize().longValue());
        dto.setAuditStatus(AuditStatus.getStatusBySymbol(cbl.getAuditStatus()));
        dto.setUser(null != cbl.getUpdater() ? cbl.getUpdater() : cbl.getCreator());
        return dto;
    }

}
