package ocean.acs.models.oracle.repository;

import java.util.List;
import java.util.Optional;
import ocean.acs.models.oracle.entity.AcquirerBankAcquirerBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcquirerBankAcquirerBinRepository
    extends JpaRepository<AcquirerBankAcquirerBin, Long> {

  void deleteByAcquirerBankId(Long acquirerBankId);

  List<AcquirerBankAcquirerBin> findByAcquirerBankId(Long acquirerBankId);

  Optional<AcquirerBankAcquirerBin> findByAcquirerBin(String acquirerBin);
}
