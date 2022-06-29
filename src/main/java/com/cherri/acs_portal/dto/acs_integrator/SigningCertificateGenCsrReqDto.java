package com.cherri.acs_portal.dto.acs_integrator;

import com.cherri.acs_portal.controller.request.GenCsrRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigningCertificateGenCsrReqDto {
    private String cardBrand;
    private String commonName;
    private String org;
    private String orgUnit;
    private String country;
    private String state;
    private String locality;
    private String bankCode;

    public static SigningCertificateGenCsrReqDto valueOf(GenCsrRequestDto genCsrRequestDto,
      String cardInfo, String bankCode) {
        SigningCertificateGenCsrReqDto reqDto = new SigningCertificateGenCsrReqDto();
        reqDto.setCardBrand(cardInfo);
        reqDto.setCommonName(genCsrRequestDto.getCommonName());
        reqDto.setOrg(genCsrRequestDto.getOrganization());
        reqDto.setOrgUnit(genCsrRequestDto.getOrganizationUnit());
        reqDto.setCountry(genCsrRequestDto.getCountry());
        reqDto.setState(genCsrRequestDto.getStateOrProvince());
        reqDto.setLocality(genCsrRequestDto.getLocality());
        reqDto.setBankCode(bankCode);
        return reqDto;
    }

}
