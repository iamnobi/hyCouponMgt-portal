package com.cherri.acs_portal.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import ocean.acs.models.data_object.portal.CardBrandSigningCertificateConfigDO;

@Data
@AllArgsConstructor
public class CardBrandSigningCertificateConfigDTO {

    private String cardBrand;
    private boolean useGlobalSigningCertificate;
    private boolean useGlobalSigningCertificate3ds1;

    public static CardBrandSigningCertificateConfigDTO valueOf(
      CardBrandSigningCertificateConfigDO d) {
        return new CardBrandSigningCertificateConfigDTO(d.getCardBrand(),
          d.isUseGlobalSigningCertificate(), d.isUseGlobalSigningCertificate3ds1());
    }
}
