package com.cherri.acs_portal.dto.system;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.validator.validation.CardBrandValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GenKeyRequest {

  private String cardBrand;

  private String country = "";

  @NotBlank(message = "{column.notempty}")
  private String organization = "";

  @NotBlank(message = "{column.notempty}")
  private String organizationUnit = "";

  @NotBlank(message = "{column.notempty}")
  private String commonName = "";

  @NotBlank(message = "{column.notempty}")
  private String locality = "";

  @NotBlank(message = "{column.notempty}")
  private String stateOrProvince = "";

  private String user;

  public String toDistinguishedName() {
    StringBuilder sb = new StringBuilder();
    sb.append("CN=")
        .append(format(commonName))
        .append(", ")
        .append("OU=")
        .append(format(organizationUnit))
        .append(", ")
        .append("O=")
        .append(format(organization))
        .append(", ")
        .append("L=")
        .append(format(locality))
        .append(", ")
        .append("ST=")
        .append(format(stateOrProvince))
        .append(", ")
        .append("C=")
        .append(format(country));
    return sb.toString();
  }

  /** See https://tools.ietf.org/html/rfc1779 */
  private String format(String val) {
    if (StringUtils.isEmpty(val)) {
      return "";
    }
    val = val.trim();
    if (val.contains(",") && !val.startsWith("\"") && !val.endsWith("\"")) {
      val = String.format("\"%s\"", val);
    }
    return val;
  }

  public void setCountry(String country) {
    if (!StringUtils.isEmpty(country)) {
      country = country.toUpperCase();
    }
    this.country = country;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
  }
}
