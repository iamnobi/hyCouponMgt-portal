package com.cherri.acs_portal.service;

import com.cherri.acs_portal.dto.cardholder.ThreeDSVerifyDTO;
import com.cherri.acs_portal.dto.cardholder.UnlockOtpVerifyDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.CardStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.OtpOperationLogDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.PanOtpStatisticsDAO;
import ocean.acs.models.dao.ThreeDSVerifiedDAO;
import ocean.acs.models.data_object.entity.OtpOperationLogDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;
import ocean.acs.models.data_object.entity.ThreeDSVerifiedOperationLogDO;
import ocean.acs.models.data_object.portal.ThreeDSVerifyDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class PanInfoService {

    private final PanInfoDAO panInfoDAO;
    private final PanOtpStatisticsDAO otpStatisticsDao;
    private final OtpOperationLogDAO otpOperationLogDao;
    private final ThreeDSVerifiedDAO threeDSVerifiedDao;
    private final AcsIntegratorManager acsIntegratorManager;

    @Autowired
    public PanInfoService(
      PanInfoDAO panInfoDAO,
      PanOtpStatisticsDAO otpStatisticsDao,
      OtpOperationLogDAO otpOperationLogDao,
      ThreeDSVerifiedDAO threeDSVerifiedDao,
      AcsIntegratorManager acsIntegratorManager) {
        this.panInfoDAO = panInfoDAO;
        this.otpStatisticsDao = otpStatisticsDao;
        this.otpOperationLogDao = otpOperationLogDao;
        this.threeDSVerifiedDao = threeDSVerifiedDao;
        this.acsIntegratorManager = acsIntegratorManager;
    }

    public Optional<ThreeDSVerifyDTO> getThreeDSVerifyDTOByPanInfoId(Long id)
      throws DatabaseException {
        return panInfoDAO.findById(id).map(ThreeDSVerifyDTO::valueOf);
    }

    public ThreeDSVerifyDTO update3DSVerifyStatus(ThreeDSVerifyDTO verifyDTO) {
        ThreeDSVerifyDO threeDSVerifyDO = ThreeDSVerifyDO.builder()
          .auditStatus(verifyDTO.getAuditStatus())
          .cardNumber(verifyDTO.getCardNumber())
          .cardType(verifyDTO.getCardType())
          .creator(verifyDTO.getCreator())
          .fileContent(verifyDTO.getFileContent())
          .fileName(verifyDTO.getFileName())
          .id(verifyDTO.getPanId())
          .issuerBankId(verifyDTO.getIssuerBankId())
          .otpLock(verifyDTO.getOtpLock())
          .panId(verifyDTO.getPanId())
          .updater(verifyDTO.getUpdater())
          .user(verifyDTO.getUser())
          .verifyEnabled(verifyDTO.getVerifyEnabled())
          .version(verifyDTO.getVersion())
          .build();

        ThreeDSVerifyDO dto = panInfoDAO.update(threeDSVerifyDO).map(e -> {
            threeDSVerifyDO.setId(e.getId());
            return threeDSVerifyDO;
        }).orElse(threeDSVerifyDO);

        // 審核通過，新增3ds驗證啟用紀錄
        if (AuditStatus.PUBLISHED.equals(verifyDTO.getAuditStatus())) {
            threeDSVerifiedDao.save(ThreeDSVerifiedOperationLogDO.newInstance(threeDSVerifyDO));
        }
        verifyDTO.setId(threeDSVerifyDO.getId());
        return verifyDTO;
    }

    public ThreeDSVerifyDTO updateAudit3DSVerifyStatus(ThreeDSVerifyDTO verifyDTO) {
        ThreeDSVerifyDO threeDSVerifyDO = ThreeDSVerifyDO.builder()
          .version(verifyDTO.getVersion())
          .verifyEnabled(verifyDTO.getVerifyEnabled())
          .user(verifyDTO.getUser())
          .updater(verifyDTO.getUpdater())
          .panId(verifyDTO.getPanId())
          .otpLock(verifyDTO.getOtpLock())
          .issuerBankId(verifyDTO.getIssuerBankId())
          .id(verifyDTO.getId())
          .fileName(verifyDTO.getFileName())
          .fileContent(verifyDTO.getFileContent())
          .creator(verifyDTO.getCreator())
          .cardType(verifyDTO.getCardType())
          .cardNumber(verifyDTO.getCardNumber())
          .auditStatus(verifyDTO.getAuditStatus())
          .build();
        return panInfoDAO.update(threeDSVerifyDO).map(
          e -> {
              verifyDTO.setId(e.getId());
              return verifyDTO;
          })
          .orElse(verifyDTO);
    }

    public UnlockOtpVerifyDTO unlockOtp(UnlockOtpVerifyDTO dto) throws DatabaseException {
        // PanOtpStatistics OTP錯誤次數歸零
        PanOtpStatisticsDO entity = findOtpStatisticsByPanInfoId(dto.getPanId());
        entity.setAuditStatus(dto.getAuditStatus().getSymbol());
        entity.setVerifyOtpCount(dto.getVerifyOtpCount());
        entity.setUpdater(dto.getUser());
        entity.setUpdateMillis(System.currentTimeMillis());
        otpStatisticsDao.saveOrUpdate(entity);

        // PanInfo card status改為normal
        PanInfoDO panInfoEntity = findPanInfoById(dto.getPanId());
        panInfoEntity.setAuditStatus(dto.getAuditStatus().getSymbol());
        panInfoEntity.setCardStatus(CardStatus.NORMAL);
        panInfoEntity.setUpdater(dto.getUser());
        panInfoEntity.setUpdateMillis(System.currentTimeMillis());
        panInfoDAO.save(panInfoEntity);

        dto.setId(entity.getId());

        // 紀錄otp解鎖
        long now = System.currentTimeMillis();
        otpOperationLogDao.save(
          OtpOperationLogDO.builder()
            .id(dto.getId())
            .issuerBankId(dto.getIssuerBankId())
            .otpEnabled(true)
            .panId(dto.getPanId())
            .creator(dto.getUser())
            .createMillis(now)
            .build());
        return dto;
    }

    private PanInfoDO getPanInfoById(Long panInfoId) throws DatabaseException {
        return panInfoDAO.findById(panInfoId)
          .orElseThrow(() -> new NoSuchDataException("Pan info not found by panId=" + panInfoId));
    }

    private PanOtpStatisticsDO findOtpStatisticsByPanInfoId(Long panInfoId)
      throws DatabaseException {
        return otpStatisticsDao
          .findByPanInfoId(panInfoId)
          .orElseThrow(() ->
            new NoSuchDataException("PanOtpStatistics not found by pan info id=" + panInfoId));

    }

    private PanInfoDO findPanInfoById(Long panInfoId) throws DatabaseException {
        return panInfoDAO
                .findById(panInfoId)
                .orElseThrow(() -> new NoSuchDataException("PanInfo not found by id=" + panInfoId));
    }

    public Optional<UnlockOtpVerifyDTO> findOnePanOtpStatistics(Long issuerBankId, Long panInfoId)
      throws OceanException {
        try {
            return otpStatisticsDao
              .findByPanInfoId(panInfoId)
              .map(panOtpStatistics -> UnlockOtpVerifyDTO.valueOf(issuerBankId, panOtpStatistics));
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    public void updatePanOtpStatistics(UnlockOtpVerifyDTO dto) {
        try {
            PanOtpStatisticsDO panOtpStatistics = findOtpStatisticsByPanInfoId(dto.getPanId());
            panOtpStatistics.setAuditStatus(dto.getAuditStatus().getSymbol());
            panOtpStatistics.setUpdater(dto.getUser());
            panOtpStatistics.setUpdateMillis(System.currentTimeMillis());
            otpStatisticsDao.saveOrUpdate(panOtpStatistics);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }
}
