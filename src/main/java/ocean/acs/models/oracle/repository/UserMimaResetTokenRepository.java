package ocean.acs.models.oracle.repository;

import java.util.Optional;
import ocean.acs.models.oracle.entity.UserMimaResetToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserMimaResetTokenRepository extends CrudRepository<UserMimaResetToken, String> {

  Optional<UserMimaResetToken> findByTokenAndDeleteFlagIsFalse(String token);

  @Modifying
  @Transactional
  @Query("update UserMimaResetToken t "
      + "set t.deleteFlag = true, t.deleter = :deleter, t.deleteMillis = :deleteMillis "
      + "where t.userAccountId = :userAccountId and t.deleteFlag = false")
  void deleteAliveUserMimaResetToken(
      @Param("userAccountId") Long userAccountId,
      @Param("deleter") String deleter,
      @Param("deleteMillis") Long deleteMillis);
}
