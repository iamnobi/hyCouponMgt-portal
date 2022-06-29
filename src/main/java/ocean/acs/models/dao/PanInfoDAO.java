package ocean.acs.models.dao;

import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.portal.ThreeDSVerifyDO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PanInfoDAO {

    Optional<PanInfoDO> findById(Long panInfoId) throws DatabaseException;

    Optional<PanInfoDO> getByCardNumberHash(String cardNumberHash) throws DatabaseException;

    Optional<PanInfoDO> findByCardNumber(Long issuerBankId, String cardNumber);

    Optional<PanInfoDO> findByIdAndDeleteFlagFalse(Long id);

    Optional<PanInfoDO> update(ThreeDSVerifyDO threeDsVerifyDO);

    PanInfoDO save(PanInfoDO panInfoDO);

    List<PanInfoDO> saveAll(Collection<PanInfoDO> panInfoDoList);

    int countsByIdAndIssuerBankId(Long id, Long issuerBankId);

    /** 取得或新增卡片資料 */
    PanInfoDO findOrCreatePanInfo(Long issuerBankId, String cardBrand, String cardNumber,
            String encryptedCardNumber, String cardNumberHash, String user);

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

    void updatePreviousTransactionInfo(
            Long id,
            boolean transactionSuccess,
            String deviceId,
            String updater,
            long updateMillis)
            throws DatabaseException;

    void updatePreviousTransactionInfoSuccess(Long id, String updater, long updateMillis)
            throws DatabaseException;

    void updateCardStatus(Long id, String cardStatus, String updater, long updateMillis)
            throws DatabaseException;
}
