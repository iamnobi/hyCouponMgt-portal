package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.Auditing;

@Repository
public interface AuditingRepository extends CrudRepository<Auditing, Long>,
        JpaSpecificationExecutor<Auditing>, PagingAndSortingRepository<Auditing, Long> {

    Page<Auditing> findByIssuerBankIdAndDeleteFlagFalseAndAuditStatusAndCreatorIsNotOrderByCreateMillisDesc(
            Long issuerBankId, String auditStatus, String creator, Pageable pageable);

    Page<Auditing> findByIssuerBankIdAndAuditStatusAndCreatorIsNotAndFunctionTypeAndDeleteFlagFalseOrderByCreateMillisDesc(
            Long issuerBankId, String auditStatus, String creator, String functionType,
            Pageable pageable);

    Page<Auditing> findByCreatorAndAuditStatusAndDeleteFlagFalseAndCreateMillisBetweenOrderByCreateMillisDesc(
            String creator, String auditStatus, Long startTime, Long endTime, Pageable paging);

    Page<Auditing> findByIssuerBankIdAndAuditStatusAndCreatorIsNotAndFunctionTypeInAndDeleteFlagIsFalseOrderByCreateMillisDesc(
            Long issuerBankId, String auditStatus, String creator, List<String> functionTypeList,
            Pageable pageable);

    @Query("select a from Auditing a where functionType = 'ATTEMPT' and actionType = 'A' and (auditStatus = 'A' or auditStatus = 'P') and draftContent like ?1 "
            + "order by id desc ")
    List<Auditing> findNewWhitelistSettingByDraftDataLike(String panIdLikeStr);

}
