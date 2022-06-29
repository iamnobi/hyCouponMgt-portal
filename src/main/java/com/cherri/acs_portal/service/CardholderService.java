package com.cherri.acs_portal.service;

import com.cherri.acs_kernel.plugin.dto.cardholder.CardInfo;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardListResultDTO;
import com.cherri.acs_kernel.plugin.dto.cardholder.result.CardholderGetCardholderInfoResultDTO;
import com.cherri.acs_portal.component.CardholderPlugin;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.constant.SessionAttributes;
import com.cherri.acs_portal.dto.acs_integrator.OtpLockStatusDto;
import com.cherri.acs_portal.dto.cardholder.HolderQueryDTO;
import com.cherri.acs_portal.dto.cardholder.HolderSummaryDTO;
import com.cherri.acs_portal.dto.hsm.EncryptResultDTO;
import com.cherri.acs_portal.util.HmacUtils;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.*;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.MaskUtils;
import ocean.acs.models.dao.*;
import ocean.acs.models.data_object.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.*;

@Log4j2
@Service
public class CardholderService {

    private final IssuerBankDAO issuerBankDao;
    private final BinRangeDAO binRangeDao;
    private final PanInfoDAO panInfoDao;
    private final PanOtpStatisticsDAO panOtpStatisticsDao;
    private final ChallengeViewOtpSettingDAO otpSettingDao;
    private final AuditingDAO auditingDao;

    private final CardholderPlugin cardholderPlugin;
    private final HsmPlugin hsmPlugin;

    private final HttpSession httpSession;

    public CardholderService(
      IssuerBankDAO issuerBankDao,
      BinRangeDAO binRangeDao,
      PanInfoDAO panInfoDao,
      PanOtpStatisticsDAO panOtpStatisticsDao,
      ChallengeViewOtpSettingDAO otpSettingDao,
      AuditingDAO auditingDao,
      CardholderPlugin cardholderPlugin,
      HsmPlugin hsmPlugin,
      HttpSession httpSession) {
        this.issuerBankDao = issuerBankDao;
        this.binRangeDao = binRangeDao;
        this.panInfoDao = panInfoDao;
        this.panOtpStatisticsDao = panOtpStatisticsDao;
        this.otpSettingDao = otpSettingDao;
        this.auditingDao = auditingDao;
        this.cardholderPlugin = cardholderPlugin;
        this.hsmPlugin = hsmPlugin;
        this.httpSession = httpSession;
    }

    public HolderSummaryDTO getCardholderSummary(HolderQueryDTO queryDto) {
        Long issuerBankId = queryDto.getIssuerBankId();
        String bankCode = getBankCode(issuerBankId);

        String cardType = null;
        if (StringUtils.isNotBlank(queryDto.getRealCardNumber())) {
            cardType = getCardType(issuerBankId, queryDto.getRealCardNumber());
        }

        // Query cardholder info
        CardholderGetCardholderInfoResultDTO cardholderInfo =
          cardholderPlugin.getCardHolderInfo(
            issuerBankId,
            bankCode,
            queryDto.getIdentityNumber(),
            cardType,
            queryDto.getRealCardNumber());
        if (null == cardholderInfo) {
            log.warn("[getHolderSummary] cardholder info data not found");
            return null;
        }

        // Set cardholder info data
        HolderSummaryDTO cardholderSummary = new HolderSummaryDTO();
        cardholderSummary.setName(MaskUtils.cardholderNameMask(cardholderInfo.getCardHolderName()));
        cardholderSummary
          .setIdentifyNumber(MaskUtils.identityNumberMask(cardholderInfo.getCardHolderId()));

        // Query card list
        CardholderGetCardListResultDTO cardList =
          cardholderPlugin.getCardList(
            issuerBankId, bankCode, cardType, cardholderInfo.getCardHolderId());

        if (!cardList.isCardListExists()) {
            cardholderSummary.setCardList(Collections.emptyList());
            return cardholderSummary;
        }

        // Set card list
        attachSettingByCard(issuerBankId, cardList, cardholderSummary);

        return cardholderSummary;
    }

    /** 取得卡片各種狀態（卡種，3DS驗證，人工彈性授權，OTP解鎖） */
    private void attachSettingByCard(
      Long issuerBankId,
      CardholderGetCardListResultDTO cardList,
      HolderSummaryDTO holderSummaryDto)
      throws NoSuchDataException {

        if (CollectionUtils.isEmpty(cardList.getCardInfoList())) {
            return;
        }

        Map<String, PanInfoDO> panInfoMap = createOrGetPanInfoMap(issuerBankId,
          cardList.getCardInfoList());

        List<HolderSummaryDTO.CardSetting> cardSettingList =
          new ArrayList<>(cardList.getCardInfoList().size());
        for (CardInfo cardInfo : cardList.getCardInfoList()) {
            HolderSummaryDTO.CardSetting cardSetting = new HolderSummaryDTO.CardSetting();
            String realCardNumber = cardInfo.getCardNumber();
            PanInfoDO panInfo = panInfoMap.get(realCardNumber);
            cardSetting.setPanId(panInfo.getId());
            cardSetting.setCardNumber(MaskUtils.acctNumberMask(panInfo.getCardNumber()));
            String cardType = getCardType(issuerBankId, realCardNumber);

            cardSetting.setCardType(cardType);
            cardSetting.setAttemptAuditStatus(
              getAttemptGrantedAuditStatus(issuerBankId, panInfo.getId()));
            cardSetting.setVerifyEnabled(panInfo.getThreeDSVerifyEnable());
            cardSetting.setVerifyEnabledAuditStatus(
              AuditStatus.getStatusBySymbol(panInfo.getAuditStatus()));
            OtpLockStatusDto otpLockStatusDto = getOtpLockStatus(panInfo);
            cardSetting.setOtpLock(otpLockStatusDto.getOtpLock());
            cardSetting.setOtpLockAuditStatus(otpLockStatusDto.getOtpLockAuditStatus());

            cardSettingList.add(cardSetting);
        }

        holderSummaryDto.setCardList(cardSettingList);
    }

    private AuditStatus getAttemptGrantedAuditStatus(
      Long issuerBankId, Long panInfoId) {
        try {
            List<AuditingDO> attemptDraftList =
              auditingDao.findDraftForNewWhitelistSetting(issuerBankId, panInfoId);
            if (CollectionUtils.isEmpty(attemptDraftList)) {
                return AuditStatus.PUBLISHED;
            }
            AuditingDO attemptDraftLatest = attemptDraftList.get(0);
            return AuditStatus.getStatusBySymbol(attemptDraftLatest.getAuditStatus());
        } catch (Exception e) {
            throw new OceanException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    /** OTP 解鎖審核狀態 */
    private OtpLockStatusDto getOtpLockStatus(PanInfoDO panInfo) {
        PanOtpStatisticsDO otpStatistics;
        try {
            otpStatistics = getOtpStatisticsAuditStatus(panInfo.getId());
        } catch (DatabaseException e) {
            throw new OceanException("Get 3DS 2.0 OTP lock status error!");
        }
        boolean isOtpLock = CardStatus.LOCK == panInfo.getCardStatus();

        return new OtpLockStatusDto(
          isOtpLock, AuditStatus.getStatusBySymbol(otpStatistics.getAuditStatus()));
    }

    /** 假如不存在則新增OtpStatistics */
    private PanOtpStatisticsDO getOtpStatisticsAuditStatus(Long panInfoId)
      throws DatabaseException {
        return panOtpStatisticsDao
          .findByPanInfoId(panInfoId)
          .orElseGet(
            () -> {
                String creator = this.getClass().getSimpleName();
                PanOtpStatisticsDO entity =
                  PanOtpStatisticsDO.newInstance(panInfoId, creator);
                try {
                    panOtpStatisticsDao.saveOrUpdate(entity);
                } catch (DatabaseException e) {
                    log.error("[getOtpStatisticsAuditStatus] modify database fail", e);
                }
                return entity;
            });
    }

    private Map<String, PanInfoDO> createOrGetPanInfoMap(
      Long issuerBankId, List<CardInfo> cardInfoList) {
        // 此Method回傳的PanInfo map
        Map<String, PanInfoDO> panInfoResultMap = new HashMap<>(); // key = cardNumber

        // 空的卡片清單
        if (CollectionUtils.isEmpty(cardInfoList)) {
            log.info("[getPanInfoMap] card list is empty");
            return panInfoResultMap;
        }

        for (CardInfo cardInfo : cardInfoList) {
            String realCardNumber = cardInfo.getCardNumber();
            String cardNumberHash = HmacUtils
              .encrypt(realCardNumber, EnvironmentConstants.hmacHashKey);

            Optional<PanInfoDO> panInfoOpt =
              panInfoDao.findByCardNumber(issuerBankId, cardNumberHash);

            // 不存在於PanInfo中的新卡，則新增panInfo和panOtpStatistics
            if (!panInfoOpt.isPresent()) {
                String cardBrand =
                  binRangeDao.findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(
                    issuerBankId, realCardNumber);

                EncryptResultDTO cardNumberEnResult =
                  hsmPlugin.encryptWithIssuerBankId(realCardNumber, issuerBankId);

                PanInfoDO panInfo = new PanInfoDO(issuerBankId, realCardNumber, cardBrand);
                panInfo.setCardNumberHash(
                  HmacUtils.encrypt(realCardNumber, EnvironmentConstants.hmacHashKey));
                panInfo.setCardNumberEn(cardNumberEnResult.getBase64());
                panInfo.setCardStatus(CardStatus.NORMAL);
                panInfo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                panInfo.setCreator(getUserAccount());
                panInfo.setCreateMillis(System.currentTimeMillis());
                panInfo = panInfoDao.save(panInfo);

                panInfoResultMap.put(realCardNumber, panInfo);
                try {
                    PanOtpStatisticsDO panOtpStatistics =
                      PanOtpStatisticsDO.newInstance(panInfo.getId(), "PanInfo");
                    panOtpStatisticsDao.save(panOtpStatistics);
                } catch (DatabaseException e) {
                    throw new OceanException(e.getResultStatus(), e.getMessage());
                }
            } else {
                panInfoResultMap.put(realCardNumber, panInfoOpt.get());
            }
        }
        return panInfoResultMap;
    }

    private String getUserAccount() {
        return (String) httpSession.getAttribute(SessionAttributes.ACCOUNT);
    }

    private String getCardType(Long issuerBankId, String realCardNumber)
      throws NoSuchDataException {
        BinRangeDO binRange =
          getBinRange(issuerBankId, realCardNumber)
            .orElseThrow(
              () -> new NoSuchDataException("The card is not in the bin-range"));

        log.debug("[getCardType] binRange={}", binRange);

        CardType cardType = CardType.getByCode(binRange.getCardType());
        if (cardType == null) {
            throw new IllegalArgumentException("Unknown cardType");
        }
        return cardType.name();
    }

    private Optional<BinRangeDO> getBinRange(Long issuerBankId, String realCardNumber)
      throws OceanException {
        try {
            BigInteger cardNumber = new BigInteger(realCardNumber);
            List<BinRangeDO> list = binRangeDao
              .findByStartBinBetweenEndBin(issuerBankId, cardNumber);
            if (list == null || list.isEmpty()) {
                return Optional.empty();
            }

            log.debug("[getBinRange] binRange list size={}", list.size());
            return Optional.of(list.get(0));
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    private String getBankCode(Long issuerBankId) {
        return issuerBankDao
          .findById(issuerBankId)
          .map(IssuerBankDO::getCode)
          .orElseThrow(
            () -> {
                String errMSg =
                  String.format(
                    "[getBankCode] not found by issuerBankId=%d",
                    issuerBankId);
                log.warn(errMSg);
                return new OceanException(errMSg);
            });
    }
}
