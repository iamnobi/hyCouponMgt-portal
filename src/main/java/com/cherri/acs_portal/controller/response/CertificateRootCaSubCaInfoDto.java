package com.cherri.acs_portal.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@JsonInclude(Include.NON_NULL)
public class CertificateRootCaSubCaInfoDto {

    @JsonProperty("certificateApplyDate")
    @Getter
    @Setter
    private Long certApplyDate;

    @JsonProperty("certificateExpiryDate")
    @Getter
    @Setter
    private Long certExpiryDate;

    @JsonProperty("certificateInformation")
    @Getter
    @Setter
    private String certInformation;

    @JsonProperty("certificateIssuer")
    @Getter
    @Setter
    private String certIssuer;

    @JsonProperty("rootCaCertificateApplyDate")
    @Getter
    @Setter
    private Long rootCaCertApplyDate;

    @JsonProperty("rootCaCertificateExpiryDate")
    @Getter
    @Setter
    private Long rootCaCertExpiryDate;

    @JsonProperty("rootCaCertificateInformation")
    @Getter
    @Setter
    private String rootCaCertInformation;

    @JsonProperty("rootCaCertificateIssuer")
    @Getter
    @Setter
    private String rootCaCertIssuer;

    @JsonProperty("subCaCertificateApplyDate")
    @Getter
    @Setter
    private Long subCaCertApplyDate;

    @JsonProperty("subCaCertificateExpiryDate")
    @Getter
    @Setter
    private Long subCaCertExpiryDate;

    @JsonProperty("subCaCertificateInformation")
    @Getter
    @Setter
    private String subCaCertInformation;

    @JsonProperty("subCaCertificateIssuer")
    @Getter
    @Setter
    private String subCaCertIssuer;
}
