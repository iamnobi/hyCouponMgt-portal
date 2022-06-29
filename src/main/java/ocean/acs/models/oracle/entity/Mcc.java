package ocean.acs.models.oracle.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

/**
 * Mcc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MCC)
public class Mcc {

  @Id
  @SequenceGenerator(name = "SQ_MCC", sequenceName = "MCC_SEQ", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MCC")
  @Column(name = DBKey.COL_MCC_ID)
  private Long id;

  @Column(name = DBKey.COL_MCC_MCC)
  private String mcc;

  @NonNull
  @Column(name = DBKey.COL_CREATOR)
  private String creator;

  @NonNull
  @Column(name = DBKey.COL_CREATE_MILLIS)
  private Long createMillis = System.currentTimeMillis();

  public static List<Mcc> valueOf(Collection<String> list, String creator) {
    List<Mcc> mccList = new ArrayList<>();
    long createMillis = System.currentTimeMillis();
    for (String code : list) {
      Mcc mcc = new Mcc();
      mcc.setMcc(code);
      mcc.setCreateMillis(createMillis);
      mcc.setCreator(creator);
      mccList.add(mcc);
    }
    return mccList;
  }
}
