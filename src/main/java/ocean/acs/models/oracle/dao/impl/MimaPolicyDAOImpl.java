package ocean.acs.models.oracle.dao.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.MimaPolicyDAO;
import ocean.acs.models.data_object.entity.MimaPolicyDO;
import ocean.acs.models.oracle.entity.MimaPolicy;

/**
 * Mima Policy DAO 密碼管理原則資料存取物件
 *
 * @author Alan Chen
 */
@Slf4j
@Service
@AllArgsConstructor
public class MimaPolicyDAOImpl implements MimaPolicyDAO {

    private final ocean.acs.models.oracle.repository.MimaPolicyRepository mimaPolicyRepository;

    /**
     * 密碼管理原則是否存在
     *
     * @param issuerBankId 銀行代碼
     * @return isExist 是否存在
     */
    @Override
    public boolean isPolicyExistByIssuerBankId(long issuerBankId) {
        boolean isExist = mimaPolicyRepository.findByIssuerBankId(issuerBankId).isPresent();
        log.debug("[MimaPolicyDao][isPolicyExistByIssuerBankId] isExist: {}", isExist);
        return isExist;
    }

    /**
     * 密碼管理原則是否存在
     *
     * @param id Mima policy id 密碼管理原則ID
     * @return isExist 是否存在
     */
    @Override
    public boolean isPolicyExistById(long id) {
        boolean isExist = mimaPolicyRepository.findById(id).isPresent();
        log.debug("[MimaPolicyDao][isPolicyExist] isExist: {}", isExist);
        return isExist;
    }

    /**
     * 查詢密碼管理原則
     *
     * @param issuerBankId 銀行代碼
     * @return Optional 已存在密碼原則
     */
    @Override
    public Optional<MimaPolicyDO> findByIssuerBankId(long issuerBankId) {
        return mimaPolicyRepository.findByIssuerBankId(issuerBankId).map(MimaPolicyDO::valueOf);
    }

    /**
     * 建立密碼管理原則
     *
     * @param mimaPolicyDO {@link MimaPolicyDO} 密碼管理原則物件
     * @return isSuccess 是否建立成功
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Override
    public boolean createPolicy(MimaPolicyDO mimaPolicyDO) {
        try {
            mimaPolicyRepository.save(MimaPolicy.valueOf(mimaPolicyDO));
            return true;
        } catch (Exception e) {
            log.error("[MimaPolicyDao][create] Create exception. entity: {}", mimaPolicyDO);
            log.error(e.getMessage(), e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }
    }

    /**
     * 建立預設密碼管理原則
     *
     * @param issuerBankId 銀行代碼
     * @return 已儲存密碼管理原則 {@link MimaPolicyDO}
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Override
    public MimaPolicyDO createDefaultPolicy(long issuerBankId) {
        try {
            log.info("[MimaPolicyDao][createDefaultPolicy] Create default mima policy.");
            MimaPolicy policy = mimaPolicyRepository.save(MimaPolicy.createDefault(issuerBankId));
            return MimaPolicyDO.valueOf(policy);
        } catch (Exception e) {
            log.error("[MimaPolicyDao][createDefaultPolicy] Create Exception. ", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }
    }

    /**
     * 更新密碼管理原則
     *
     * @param dto {@link MimaPolicyDO} 新密碼管理原則
     * @return isSuccess 是否更新成功
     * @throws OceanException {@link ResultStatus} DB_SAVE_ERROR
     */
    @Override
    public boolean updatePolicy(MimaPolicyDO dto) {
        try {
            mimaPolicyRepository.save(MimaPolicy.valueOf(dto));
            return true;
        } catch (Exception e) {
            log.error("[MimaPolicyDao][update] Update exception. entity: {}", dto);
            log.error(e.getMessage(), e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR);
        }
    }
}
