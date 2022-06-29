package ocean.acs.models.dao;

import java.util.Optional;
import ocean.acs.models.data_object.entity.UserMimaResetTokenDO;

public interface UserMimaResetTokenDAO {

  UserMimaResetTokenDO save(UserMimaResetTokenDO userMimaResetTokenDO);

  Optional<UserMimaResetTokenDO> findByToken(String token);

  void deleteAliveUserMimaResetToken(Long userAccountId, String deleter, Long deleteMillis);
}
