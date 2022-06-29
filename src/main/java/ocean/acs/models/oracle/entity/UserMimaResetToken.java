package ocean.acs.models.oracle.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMimaResetToken implements Serializable {

  @Id
  private String token;
  private Long userAccountId;
  private Long expireMillis;

  private String creator;
  private Long createMillis;
  private boolean deleteFlag;
  private String deleter;
  private Long deleteMillis;
}
