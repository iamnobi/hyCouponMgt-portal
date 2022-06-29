package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACQUIRER_BANK_ACQUIRER_BIN")
public class AcquirerBankAcquirerBin {

  @Id
  @GenericGenerator(
      name = "acquirer_bank_acquirer_bin_seq_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(
            name = "sequence_name",
            value = "ACQUIRER_BANK_ACQUIRER_BIN_ID_SEQ"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
      })
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "acquirer_bank_acquirer_bin_seq_generator")
  @Column(name = "ID")
  private Long id;

  @NonNull
  @Column(name = "ACQUIRER_BANK_ID")
  private Long acquirerBankId;

  @NonNull
  @Column(name = "ACQUIRER_B_I_N")
  private String acquirerBin;

  @NonNull
  @Column(name = DBKey.COL_CREATOR)
  private String creator;

  @NonNull
  @Column(name = DBKey.COL_CREATE_MILLIS)
  private Long createMillis = System.currentTimeMillis();
}
