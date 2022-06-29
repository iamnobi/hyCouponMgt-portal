package ocean.acs.models.oracle.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.portal.ThreeDSVerifyDO;
import ocean.acs.models.oracle.entity.PanInfo;
import ocean.acs.models.oracle.entity.PanOtpStatistics;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Repository
@AllArgsConstructor
public class PanInfoDAOImpl implements PanInfoDAO {

    private final ocean.acs.models.oracle.repository.PanInfoRepository repo;
    private final ocean.acs.models.oracle.repository.PanOtpStatisticsRepository panOtpStatisticsRepo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Optional<PanInfoDO> findById(Long panInfoId) throws DatabaseException {
        try {
            return repo.findById(panInfoId).map(PanInfoDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, panInfoId={}", panInfoId, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<PanInfoDO> getByCardNumberHash(String cardNumberHash) throws DatabaseException {
        try {
            List<PanInfo> list = repo.findByCardNumberHash(cardNumberHash);
            if (list == null || list.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(list.get(0)).map(PanInfoDO::valueOf);
        } catch (Exception e) {
            log.error("[getByCardNumberHash] unknown exception, cardNumberHash={}", cardNumberHash,
                    e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Optional<PanInfoDO> findByCardNumber(Long issuerBankId, String cardNumberHash) {
        List<PanInfo> panInfoList = repo
                .findByIssuerBankIdAndCardNumberHashAndDeleteFlagOrderByUpdateMillisDescCreateMillisDesc(
                        issuerBankId, cardNumberHash, false);
        if (panInfoList != null && panInfoList.size() > 0) {
            return Optional.of(PanInfoDO.valueOf(panInfoList.get(0)));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PanInfoDO> findByIdAndDeleteFlagFalse(Long id) {
        return repo.findByIdAndDeleteFlagFalse(id).map(PanInfoDO::valueOf);
    }

    @Override
    public Optional<PanInfoDO> update(ThreeDSVerifyDO threeDsVerifyDO) {
        if (threeDsVerifyDO == null) {
            log.error("[update] Failed in update pan info due to missing input content");
            throw new IllegalArgumentException(
                    "Failed in update pan info due to missing input content");
        }

        PanInfo panInfo = repo.findById(threeDsVerifyDO.getPanId()).map(e -> {
            e.setThreeDSVerifyEnable(threeDsVerifyDO.getVerifyEnabled());
            e.setAuditStatus(threeDsVerifyDO.getAuditStatus().getSymbol());
            e.setUpdater(threeDsVerifyDO.getUser());
            e.setUpdateMillis(System.currentTimeMillis());
            e.setIssuerBankId(threeDsVerifyDO.getIssuerBankId());
            return e;
        }).map(repo::save).orElseThrow(() -> {
            log.error(
                    "[update] Failed in update pan info due to unknown issuer bank content with id={}",
                    threeDsVerifyDO.getPanId());
            return new OceanExceptionForPortal("Command failed in missing target content.");
        });

        return Optional.of(PanInfoDO.valueOf(panInfo));
    }

    @Override
    public PanInfoDO save(PanInfoDO panInfoDO) {
        PanInfo panInfo = PanInfo.valueOf(panInfoDO);
        return PanInfoDO.valueOf(repo.save(panInfo));
    }

    @Override
    public List<PanInfoDO> saveAll(Collection<PanInfoDO> panInfoDoList) {
        List<PanInfo> panInfoList =
                panInfoDoList.stream().map(PanInfo::valueOf).collect(Collectors.toList());
        Iterable<PanInfo> panInfoIter = repo.saveAll(panInfoList);
        return StreamSupport.stream(panInfoIter.spliterator(), false).map(PanInfoDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public int countsByIdAndIssuerBankId(Long id, Long issuerBankId) {
        return repo.countsByIdAndIssuerBankId(id, issuerBankId);
    }

    @Override
    public PanInfoDO findOrCreatePanInfo(Long issuerBankId, String cardBrand, String cardNumber,
            String encryptedCardNumber, String cardNumberHash,  String user) {
        return findByCardNumber(issuerBankId, cardNumberHash).orElseGet(() -> {
            // Save Pan_Info
            PanInfo panInfo = new PanInfo();
            panInfo.setIssuerBankId(issuerBankId);
            panInfo.setCardBrand(cardBrand);
            panInfo.setCardNumber(cardNumber);
            panInfo.setCardNumberHash(cardNumberHash);
            panInfo.setCardNumberEn(encryptedCardNumber);
            panInfo.setThreeDSVerifyEnable(true);
            panInfo.setCardStatus(CardStatus.NORMAL.getCode());
            panInfo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
            panInfo.setCreator(user);
            panInfo.setCreateMillis(System.currentTimeMillis());
            panInfo = repo.save(panInfo);
            log.debug("[findOrCreatePanInfo] create PanInfo. id={}", panInfo.getId());

            // Save Pan_Otp_Statistics
            PanOtpStatistics otpStatistics = panOtpStatisticsRepo
              .save(PanOtpStatistics.newInstance(panInfo.getId(), "PanInfo"));
            log.debug("[findOrCreatePanInfo] create PanOtpStatistics. id={}",
                    otpStatistics.getId());
            return PanInfoDO.valueOf(panInfo);
        });
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(PanInfo.class, issuerBankId, deleter, deleteMillis);
    }

    @Override
    public void updatePreviousTransactionInfo(
            Long id,
            boolean transactionSuccess,
            String deviceId,
            String updater,
            long updateMillis)
            throws DatabaseException {
        try {
            repo.updatePreviousTransactionInfo(
                    id, transactionSuccess, deviceId, updater, updateMillis);
        } catch (Exception e) {
            log.error(
                    "[updatePreviousTransactionInfo] unknown exception, id={}, transactionSuccess={}, deviceId={}, updater={}, updateMillis={}",
                    id,
                    transactionSuccess,
                    deviceId,
                    updater,
                    updateMillis,
                    e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updatePreviousTransactionInfoSuccess(Long id, String updater, long updateMillis)
            throws DatabaseException {
        try {
            repo.updatePreviousTransactionInfoSuccess(id, updater, updateMillis);
        } catch (Exception e) {
            log.error(
                    "[updatePreviousTransactionInfoSuccess] unknown exception, id={}, updater={}, updateMillis={}",
                    id,
                    updater,
                    updateMillis,
                    e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void updateCardStatus(Long id, String cardStatus, String updater, long updateMillis)
            throws DatabaseException {
        try {
            repo.updateCardStatus(id, cardStatus, updater, updateMillis);
        } catch (Exception e) {
            log.error(
                    "[updateCardStatus] unknown exception, id={}, updater={}, updateMillis={}",
                    id,
                    updater,
                    updateMillis,
                    e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }
}
