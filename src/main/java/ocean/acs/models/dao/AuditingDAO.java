package ocean.acs.models.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.data_object.entity.AuditingDO;

public interface AuditingDAO {

    Optional<Page<AuditingDO>> findByFunctionTypeAndAuditStatusExcludeSelf(Long issuerBankId,
            AuditFunctionType functionType, AuditStatus auditStatus, String applicant,
            Pageable paging);

    /** 取得全部待審項目 */
    Optional<Page<AuditingDO>> findAllAuditStatusExcludeSelf(Long issuerBankId,
            AuditStatus auditStatus, String applicant, Pageable paging);

    /** 依審核權限項目清單取得待審核項目 */
    Optional<Page<AuditingDO>> findByFunctionTypeListAndAuditStatusExcludeSelf(Long issuerBankId,
            List<AuditFunctionType> functionTypeList, AuditStatus auditStatus, String applicant,
            Pageable paging);

    Optional<AuditingDO> save(AuditingDO auditingDO);

    Optional<AuditingDO> findById(Long id);

    Optional<Page<AuditingDO>> getLogByUser(String user, AuditStatus auditStatus, Long startTime,
            Long endTime, Pageable paging);

    /**
     * 找出是"新增"的彈性授權的草稿，目的是為了在持卡人查詢中的卡片清單顯示審核狀態<br>
     * 為什麼不直接查詢WhiteListAttemptSetting?<br>
     * 因為是新增的資料，所以在WhitelistSetting表中會沒有資料可拿<br>
     * 使用前綴模糊查詢，所以issuerBankId和panId的順序不能被改變
     */
    List<AuditingDO> findDraftForNewWhitelistSetting(Long issuerBankId, Long panId);
    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
