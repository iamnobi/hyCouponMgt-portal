package ocean.acs.models.oracle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.SubCaCertificate;

@Repository
public interface SubCaCertificateRepository extends JpaRepository<SubCaCertificate, Long> {

}
