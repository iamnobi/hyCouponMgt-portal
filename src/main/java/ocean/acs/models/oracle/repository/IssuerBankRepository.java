package ocean.acs.models.oracle.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.IssuerBank;

@Repository
public interface IssuerBankRepository extends PagingAndSortingRepository<IssuerBank, Long> {

    @Override
    @Query("select i from IssuerBank i where id != -1 and deleteFlag = 0 order by code asc")
    Page<IssuerBank> findAll(Pageable pageable);

    List<IssuerBank> findByIdInAndDeleteFlagFalse(Collection<Long> ids);

    @Override
    @Query("select i from IssuerBank i where deleteFlag = 0 order by code asc")
    List<IssuerBank> findAll();

    @Query("select i.id from IssuerBank i where deleteFlag = 0 order by code asc")
    List<Long> findIdAll();

    @Override
    @Query("select i from IssuerBank i where id = ?1 and deleteFlag = 0")
    Optional<IssuerBank> findById(Long id);

    @Query("select i from IssuerBank i where code = ?1 and deleteFlag = 0")
    Optional<IssuerBank> findByCode(String code);

    //00003 allow control asc/3ds oper id by user
    @Query("select i from IssuerBank i where acsOperatorId = ?1")
    Optional<IssuerBank> findByAcsOperatorId(String acsOperatorId);

    boolean existsByCodeAndDeleteFlagIsFalse(String code);

    @Query(value = "SELECT ACS_OPERATOR_ID_SEQ.nextval FROM dual", nativeQuery = true)
    Long getAcsOperatorIdNextVal();
}
