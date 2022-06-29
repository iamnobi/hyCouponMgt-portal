package ocean.acs.models.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.data_object.entity.AcquirerBank3dsRefNumDO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "THREE_D_S_SDK")
public class AcquirerBank3dsRefNum {

  @Id
  @GenericGenerator(
          name = "acquirer_bank_three_d_s_sdk_seq_generator",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(
                          name = "sequence_name",
                          value = "ACQUIRER_BANK_THREE_D_S_SDK_SEQ"
                  ),
                  @org.hibernate.annotations.Parameter(
                          name = "increment_size",
                          value = "1"
                  )
          }
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "acquirer_bank_three_d_s_sdk_seq_generator")
  @Column(name = "ID")
  private Long id;

  @NonNull
  @Column(name = "SDK_REFERENCE_NUMBER")
  private String sdkReferenceNumber;

  @NonNull
  @Column(name = "CREATOR")
  private String creator;

  @NonNull
  @Column(name = "CREATE_MILLIS")
  private Long createMillis = System.currentTimeMillis();

  public static AcquirerBank3dsRefNum valueOf(AcquirerBank3dsRefNumDO d) {
    return new AcquirerBank3dsRefNum(
            d.getId(),
            d.getSdkReferenceNumber(),
            d.getCreator(),
            d.getCreateMillis()
    );
  }
}
