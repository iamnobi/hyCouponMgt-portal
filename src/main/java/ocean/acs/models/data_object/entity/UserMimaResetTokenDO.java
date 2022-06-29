package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.oracle.entity.UserMimaResetToken;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMimaResetTokenDO {

  private String token;
  private Long userAccountId;
  private Long expireMillis;

  private String creator;
  private Long createMillis;
  private boolean deleteFlag;
  private String deleter;
  private Long deleteMillis;

  public boolean isExpired() {
    return System.currentTimeMillis() > expireMillis;
  }

  public static UserMimaResetTokenDO valueOf(UserMimaResetToken e) {
    return new UserMimaResetTokenDO(
        e.getToken(),
        e.getUserAccountId(),
        e.getExpireMillis(),
        e.getCreator(),
        e.getCreateMillis(),
        e.isDeleteFlag(),
        e.getDeleter(),
        e.getDeleteMillis()
    );
  }

  public UserMimaResetToken toOracleUserMimaResetToken() {
    return new UserMimaResetToken(
        token,
        userAccountId,
        expireMillis,
        creator,
        createMillis,
        deleteFlag,
        deleter,
        deleteMillis);
  }
}
