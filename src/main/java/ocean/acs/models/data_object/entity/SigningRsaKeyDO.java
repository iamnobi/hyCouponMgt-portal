package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigningRsaKeyDO {

    private Long id;
    private String keyId;
    private String modulusHex;
    private String publicExponentHex;
    private Long createMillis = System.currentTimeMillis();
    private Long updateMillis;

    public static SigningRsaKeyDO valueOf(ocean.acs.models.oracle.entity.SigningRsaKey e) {
        SigningRsaKeyDO d = new SigningRsaKeyDO();
        d.setId(e.getId());
        d.setKeyId(e.getKeyId());
        d.setModulusHex(e.getModulusHex());
        d.setPublicExponentHex(e.getPublicExponentHex());
        d.setCreateMillis(e.getCreateMillis());
        d.setUpdateMillis(e.getCreateMillis());
        return d;
    }
    
    public static SigningRsaKeyDO valueOf(ocean.acs.models.sql_server.entity.SigningRsaKey e) {
        SigningRsaKeyDO d = new SigningRsaKeyDO();
        d.setId(e.getId());
        d.setKeyId(e.getKeyId());
        d.setModulusHex(e.getModulusHex());
        d.setPublicExponentHex(e.getPublicExponentHex());
        d.setCreateMillis(e.getCreateMillis());
        d.setUpdateMillis(e.getCreateMillis());
        return d;
    }

    public SigningRsaKeyDO(Builder builder) {
        this.createMillis = builder.createMillis;
        this.updateMillis = builder.updateMillis;
        this.keyId = builder.keyId;
        this.modulusHex = builder.rsaModulus;
        this.publicExponentHex = builder.rsaPublicExponent;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public final static class Builder {

        private String keyId;
        private String rsaModulus;
        private String rsaPublicExponent;
        private Long createMillis = Long.valueOf(System.currentTimeMillis());
        private Long updateMillis = Long.valueOf(System.currentTimeMillis());

        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public Builder rsaModulus(String rsaModulus) {
            this.rsaModulus = rsaModulus;
            return this;
        }

        public Builder rsaPublicExponent(String rsaPublicExponent) {
            this.rsaPublicExponent = rsaPublicExponent;
            return this;
        }

        public SigningRsaKeyDO build() {
            return new SigningRsaKeyDO(this);
        }
    }

}
