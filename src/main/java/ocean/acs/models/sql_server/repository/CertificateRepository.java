package ocean.acs.models.sql_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
