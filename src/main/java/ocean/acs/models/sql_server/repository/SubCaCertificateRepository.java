package ocean.acs.models.sql_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.sql_server.entity.SubCaCertificate;

@Repository
public interface SubCaCertificateRepository extends JpaRepository<SubCaCertificate, Long> {

}
