package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.MimaRecordDAO;
import ocean.acs.models.data_object.entity.MimaRecordDO;
import ocean.acs.models.oracle.entity.MimaRecord;
import ocean.acs.models.oracle.repository.MimaRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Mima Record DAO 密碼異動歷程紀錄資料庫存取物件
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MimaRecordDAOImpl implements MimaRecordDAO {

    private final MimaRecordRepository mimaRecordRepository;

    @Autowired
    public MimaRecordDAOImpl(
      MimaRecordRepository mimaRecordRepository) {
        this.mimaRecordRepository = mimaRecordRepository;
    }

    /**
     * 密碼是否於追朔次數內有重複
     *
     * @param issuerBankId  銀行代碼
     * @param account       使用者帳號
     * @param range         追朔次數
     * @param encryptedMima 加密後密碼
     * @return 是否重複
     */
    @Override
    public boolean isDuplicatedByMimaRecordHistory(long issuerBankId,
      String account, int range, String encryptedMima) {
        try {
            Specification<MimaRecord> spec = (root, query, cb) -> {
                List<Predicate> predicatesList = new ArrayList<>();

                /* Where */
                Predicate p1 = cb.and(cb.equal(root.get("issuerBankId"), issuerBankId));
                Predicate p2 = cb.and(cb.equal(root.get("account"), account));
                predicatesList.add(p1);
                predicatesList.add(p2);

                /* Sort */
                query.orderBy(cb.desc(root.get("createTime")));

                Predicate[] predicates = new Predicate[predicatesList.size()];
                return cb.and(predicatesList.toArray(predicates));
            };

            List<MimaRecord> list = mimaRecordRepository.findAll(spec, PageRequest.of(0, range)).getContent();
            boolean isMatch = false;
            for (MimaRecord r : list) {
                if (encryptedMima.equals(r.getMima())) {
                    isMatch = true;
                    break;
                }
            }
            log.debug("[MimaPolicyDao][isDuplicatedByMimaRecordHistory] isDuplicated: {}", isMatch);
            return isMatch;
        } catch (Exception e) {
            log.error("[MimaRecordDao][isDuplicatedByMimaRecordHistory] Exception. ", e);
            throw new OceanException(ResultStatus.DB_READ_ERROR);
        }
    }

    /**
     * 儲存密碼異動紀錄
     *
     * @param mimaRecord 密碼異動紀錄 {@link MimaRecord}
     * @return 是否儲存成功
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Override
    public boolean saveRecord(MimaRecordDO mimaRecord) {
        try {
            mimaRecordRepository.save(MimaRecord.valueOf(mimaRecord));
            return true;
        } catch (Exception e) {
            log.error("[MimaRecordDao][saveRecord] Save Exception.", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }
    }

    /**
     * 查詢最後一次密碼異動紀錄
     *
     * @param issuerBankId 銀行代碼
     * @param account      使用者帳號
     * @return 密碼異動紀錄
     */
    @Override
    public Optional<MimaRecordDO> findLastChangeMimaRecord(long issuerBankId, String account) {
        Optional<MimaRecord> mimaRecordOptional =
          mimaRecordRepository
            .findTop1ByIssuerBankIdAndAccountOrderByCreateTimeDesc(issuerBankId, account);
        return mimaRecordOptional.map(MimaRecordDO::valueOf);
    }
}
