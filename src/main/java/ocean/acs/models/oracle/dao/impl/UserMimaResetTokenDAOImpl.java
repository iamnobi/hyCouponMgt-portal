package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.models.dao.UserMimaResetTokenDAO;
import ocean.acs.models.data_object.entity.UserMimaResetTokenDO;
import ocean.acs.models.oracle.repository.UserMimaResetTokenRepository;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class UserMimaResetTokenDAOImpl implements UserMimaResetTokenDAO {

  private final UserMimaResetTokenRepository repo;


  @Override
  public UserMimaResetTokenDO save(UserMimaResetTokenDO userMimaResetTokenDO) {
    return UserMimaResetTokenDO
        .valueOf(repo.save(userMimaResetTokenDO.toOracleUserMimaResetToken()));
  }

  @Override
  public Optional<UserMimaResetTokenDO> findByToken(String token) {
    return repo
        .findByTokenAndDeleteFlagIsFalse(token)
        .map(UserMimaResetTokenDO::valueOf);
  }

  @Override
  public void deleteAliveUserMimaResetToken(Long userAccountId, String deleter, Long deleteMillis) {
    repo.deleteAliveUserMimaResetToken(userAccountId, deleter, deleteMillis);
  }
}
