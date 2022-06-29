package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.oracle.entity.AcquirerBank3dsRefNum;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcquirerBank3dsRefNumDO {

  private Long id;
  private String sdkReferenceNumber;
  private String creator;
  @Builder.Default
  private Long createMillis = System.currentTimeMillis();

  public static AcquirerBank3dsRefNumDO valueOf(AcquirerBank3dsRefNum e) {
    return new AcquirerBank3dsRefNumDO(
            e.getId(),
            e.getSdkReferenceNumber(),
            e.getCreator(),
            e.getCreateMillis()
    );
  }
}
