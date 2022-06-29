package com.cherri.acs_portal.controller.response;

import lombok.Data;

@Data
public class GetCertificateInfoResponse {

    private CertificateRootCaSubCaInfoDto visa;
    private CertificateRootCaSubCaInfoDto mastercard;
    private CertificateRootCaSubCaInfoDto jcb;

    protected GetCertificateInfoResponse(GetCertificateInfoResponse.Builder builder) {
        this.visa = builder.visa;
        this.mastercard = builder.mastercard;
        this.jcb = builder.jcb;
    }

    public static final class Builder {

        private CertificateRootCaSubCaInfoDto visa;
        private CertificateRootCaSubCaInfoDto mastercard;
        private CertificateRootCaSubCaInfoDto jcb;

        public GetCertificateInfoResponse.Builder visa(CertificateRootCaSubCaInfoDto visa) {
            this.visa = visa;
            return this;
        }

        public GetCertificateInfoResponse.Builder mastercard(
          CertificateRootCaSubCaInfoDto mastercard) {
            this.mastercard = mastercard;
            return this;
        }

        public GetCertificateInfoResponse.Builder jcb(CertificateRootCaSubCaInfoDto jcb) {
            this.jcb = jcb;
            return this;
        }

        public GetCertificateInfoResponse build() {
            return new GetCertificateInfoResponse(this);
        }

    }

}
