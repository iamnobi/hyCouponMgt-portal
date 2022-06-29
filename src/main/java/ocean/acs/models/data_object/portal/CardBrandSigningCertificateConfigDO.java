package ocean.acs.models.data_object.portal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardBrandSigningCertificateConfigDO {
  private String cardBrand;
  private boolean useGlobalSigningCertificate;
  private boolean useGlobalSigningCertificate3ds1;
}
