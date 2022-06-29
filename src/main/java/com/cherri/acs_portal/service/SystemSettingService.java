package com.cherri.acs_portal.service;

import com.cherri.acs_portal.annotation.LogInfo;
import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.controller.response.LanguageResDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.bank.IssuerLogoUpdateDto;
import com.cherri.acs_portal.dto.hsm.DecryptResultDTO;
import com.cherri.acs_portal.dto.system.*;
import com.cherri.acs_portal.dto.system.ChallengeViewMessageDTO.VerifyPage.OtpSetting;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.model.enumerator.CavvKeyType;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ChallengeViewMessageCategory;
import ocean.acs.commons.enumerator.KeyStoreKeyName;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.*;
import ocean.acs.models.data_object.entity.*;
import ocean.acs.models.data_object.portal.BinRangeQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBinRangeDO;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SystemSettingService {

    private final Environment environment;

    private final BinRangeDAO binRangeDao;
    private final CardBrandLogoDAO cardBrandLogoDao;
    private final IssuerLogoDAO issuerLogoDao;
    private final IssuerBankDAO issuerBankDao;
    private final SystemSettingDAO sysDao;
    private final ChallengeViewMessageDAO challengeViewMessageDao;
    private final ChallengeViewOtpSettingDAO challengeViewOtpSettingDao;
    private final SmsTemplateDAO smsTemplateDao;
    private final ErrorCodeDAO errorCodeDAO;
    private final SecretKeyDAO secretKeyDao;
    private final KeyStoreDAO keyStoreDao;
    private final LangDAO langDao;

    private final AcsIntegratorManager acsiMgr;
    private final CardBrandConfigurationHelper cardBrandConfigurationHelper;
    private final HsmPlugin hsmPlugin;

    @Autowired
    public SystemSettingService(
            Environment environment,
            BinRangeDAO binRangeDao,
            CardBrandLogoDAO cardBrandLogoDao,
            IssuerLogoDAO issuerLogoDao,
            IssuerBankDAO issuerBankDao,
            SystemSettingDAO sysDao,
            ChallengeViewMessageDAO challengeViewMessageDao,
            ChallengeViewOtpSettingDAO challengeViewOtpSettingDao,
            ErrorCodeDAO errorCodeDAO,
            SmsTemplateDAO smsTemplateDao,
            SecretKeyDAO secretKeyDao,
            KeyStoreDAO keyStoreDao,
            LangDAO langDao,
            AcsIntegratorManager acsiMgr,
            CardBrandConfigurationHelper cardBrandConfigurationHelper,
            HsmPlugin hsmPlugin) {
        this.environment = environment;
        this.binRangeDao = binRangeDao;
        this.cardBrandLogoDao = cardBrandLogoDao;
        this.issuerLogoDao = issuerLogoDao;
        this.issuerBankDao = issuerBankDao;
        this.sysDao = sysDao;
        this.challengeViewMessageDao = challengeViewMessageDao;
        this.challengeViewOtpSettingDao = challengeViewOtpSettingDao;
        this.errorCodeDAO = errorCodeDAO;
        this.smsTemplateDao = smsTemplateDao;
        this.secretKeyDao = secretKeyDao;
        this.keyStoreDao = keyStoreDao;
        this.langDao = langDao;
        this.acsiMgr = acsiMgr;
        this.cardBrandConfigurationHelper = cardBrandConfigurationHelper;
        this.hsmPlugin = hsmPlugin;
    }

    public PagingResultDTO<BinRangeDTO> queryBinRange(BinRangeQueryDTO queryDto) {
        BinRangeQueryDO queryDO = new BinRangeQueryDO();
        queryDO.setCardBrand(queryDto.getCardBrand());
        queryDO.setIssuerBankId(queryDto.getIssuerBankId());
        queryDO.setPage(queryDto.getPage());
        queryDO.setPageSize(queryDto.getPageSize());
        PagingResultDO<PortalBinRangeDO> pagingResultDO = binRangeDao.query(queryDO);
        if (pagingResultDO.getData().isEmpty()) {
            return PagingResultDTO.empty();
        }

        List<BinRangeDTO> binRangeDTOList =
                pagingResultDO.getData().stream()
                        .map(BinRangeDTO::valueOf)
                        .collect(Collectors.toList());

        PagingResultDTO<BinRangeDTO> response = new PagingResultDTO<>();
        response.setData(binRangeDTOList);
        response.setTotalPages(pagingResultDO.getTotalPages());
        response.setTotal(pagingResultDO.getTotal());
        response.setCurrentPage(pagingResultDO.getCurrentPage());
        return response;
    }

    public Optional<BinRangeDTO> getBinRangeById(Long binRangeId, Long bankId) {
        Optional<BinRangeDO> binOpt = binRangeDao.findById(binRangeId, bankId);
        if (binOpt.isPresent()) {
            BinRangeDTO binRangeDTO = BinRangeDTO.valueOf(binOpt.get());
            return Optional.of(binRangeDTO);
        } else {
            return Optional.empty();
        }
    }

    /** 檢查BinRange，是否與現存的BinRange衝突 */
    public void validateBinRange(BinRangeDTO binRangeDTO) throws OceanException {
        if (binRangeDTO == null) {
            return;
        }

        BinRangeDO binRangeDO =
                BinRangeDO.newInstance(
                        binRangeDTO.getId(),
                        binRangeDTO.getIssuerBankId(),
                        binRangeDTO.getCardType(),
                        binRangeDTO.getEndRange(),
                        binRangeDTO.getStartRange(),
                        binRangeDTO.getCardBrand(),
                        binRangeDTO.getAuditStatus());
        binRangeDao
                .existingBinRangeConflict(binRangeDO)
                .ifPresent(
                        bin -> {
                            log.warn(
                                    "[validateBinRange] Conflicted with existing binRange, user input binRange={}, db binRange={}",
                                StringUtils.normalizeSpace(binRangeDTO.toString()),
                                StringUtils.normalizeSpace(bin.toString())
                            );
                            // 與其他間銀行的binRange衝突
                            if (!bin.getIssuerBankId().equals(binRangeDTO.getIssuerBankId())) {
                                String bankName =
                                        issuerBankDao
                                                .findById(bin.getIssuerBankId())
                                                .map(IssuerBankDO::getName)
                                                .orElse("");
                                throw new OceanException(
                                        ResultStatus.DUPLICATE_DATA_ELEMENT,
                                        String.format(
                                                "[ACS 2.0] Conflicted with the Bank:[%s] binRange, cardBrand=%s, start bin=%d, end bin=%d",
                                                bankName,
                                                bin.getCardBrand(),
                                                bin.getStartBin(),
                                                bin.getEndBin()));
                            }
                            // 與自行的binRange衝突
                            throw new OceanException(
                                    ResultStatus.DUPLICATE_DATA_ELEMENT,
                                    String.format(
                                            "[ACS 2.0] Conflicted with existing binRange, cardBrand:%s, start bin=%d, end bin=%d",
                                            bin.getCardBrand(),
                                            bin.getStartBin(),
                                            bin.getEndBin()));
                        });
    }

    public BinRangeDTO addBinRange(BinRangeDTO binRangeDTO) throws OceanException {
        validateBinRange(binRangeDTO);

        BinRangeDO binRangeDO =
                BinRangeDO.newInstance(
                        binRangeDTO.getId(),
                        binRangeDTO.getIssuerBankId(),
                        binRangeDTO.getCardType(),
                        binRangeDTO.getEndRange(),
                        binRangeDTO.getStartRange(),
                        binRangeDTO.getCardBrand(),
                        binRangeDTO.getAuditStatus());
        binRangeDO.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        binRangeDO.setCreator(binRangeDTO.getUser());
        binRangeDO.setCreateMillis(System.currentTimeMillis());

        BinRangeDO binResult = binRangeDao.add(binRangeDO);
        binRangeDTO.setId(binResult.getId());
        return binRangeDTO;
    }

    public BinRangeDTO updateBinRange(BinRangeDTO binRangeDTO) throws OceanException {
        validateBinRange(binRangeDTO);

        BinRangeDO binRangeDO =
                BinRangeDO.newInstance(
                        binRangeDTO.getId(),
                        binRangeDTO.getIssuerBankId(),
                        binRangeDTO.getCardType(),
                        binRangeDTO.getEndRange(),
                        binRangeDTO.getStartRange(),
                        binRangeDTO.getCardBrand(),
                        binRangeDTO.getAuditStatus());
        binRangeDO.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        binRangeDO.setUpdater(binRangeDTO.getUser());
        binRangeDO.setUpdateMillis(System.currentTimeMillis());
        binRangeDao.update(binRangeDO);
        return binRangeDTO;
    }

    public BinRangeDTO updateBinRangeAuditStatus(BinRangeDTO binRangeDTO) throws OceanException {
        BinRangeDO binRangeDO =
                binRangeDao
                        .findById(binRangeDTO.getId(), binRangeDTO.getIssuerBankId())
                        .orElseThrow(() -> new NoSuchDataException("Does not exists in binRange"));
        binRangeDO.setAuditStatus(binRangeDTO.getAuditStatus().getSymbol());
        binRangeDO.setUpdateMillis(System.currentTimeMillis());
        binRangeDO.setUpdater(binRangeDTO.getUser());
        binRangeDao.update(binRangeDO);
        return binRangeDTO;
    }

    public boolean deleteBinRange(DeleteDataDTO deleteDataDTO) {
        return binRangeDao.delete(deleteDataDTO.getId(), deleteDataDTO.getUser());
    }

    public SimpleEntry<AuditStatus, byte[]> getCardBrandLogoAndAuditByCardBrand(int version, String cardBrand) {
        Optional<CardBrandLogoDO> cardBrandLogoOpt =
                cardBrandLogoDao.findByCardBrandAndNotDelete(version, cardBrand);
        if (cardBrandLogoOpt.isPresent()) {
            CardBrandLogoDO logo = cardBrandLogoOpt.get();
            return new SimpleEntry<>(
                    AuditStatus.getStatusBySymbol(logo.getAuditStatus()), logo.getImageContent());
        }
        return new SimpleEntry<>(AuditStatus.PUBLISHED, new byte[0]);
    }

    public Optional<Long> findIdByCardBrandAndNotDelete(int version, String cardBrand) {
        return cardBrandLogoDao.findIdByCardBrandAndNotDelete(version, cardBrand);
    }

    public CardBrandLogoUpdateDTO createCardBrandLogo(CardBrandLogoUpdateDTO cardBrandLogoUpdateDTO)
            throws OceanException {
        final long currentMillis = System.currentTimeMillis();
        CardBrandLogoDO cardBrandLogoDO = new CardBrandLogoDO();
        cardBrandLogoDO.setThreeDSVersion(cardBrandLogoUpdateDTO.getVersion());
        cardBrandLogoDO.setCardBrand(cardBrandLogoUpdateDTO.getCardBrand());
        cardBrandLogoDO.setImageName(
                this.getFileName(currentMillis, cardBrandLogoUpdateDTO.getFileName()));
        cardBrandLogoDO.setImageContent(cardBrandLogoUpdateDTO.getFileContent());
        cardBrandLogoDO.setImageSize(Math.toIntExact(cardBrandLogoUpdateDTO.getFileSize()));
        cardBrandLogoDO.setAuditStatus(cardBrandLogoUpdateDTO.getAuditStatus().getSymbol());
        cardBrandLogoDO.setCreator(cardBrandLogoUpdateDTO.getUser());
        cardBrandLogoDO.setCreateMillis(currentMillis);
        cardBrandLogoDO = cardBrandLogoDao.save(cardBrandLogoDO);

        if (null != cardBrandLogoDO.getId()) {
            return CardBrandLogoUpdateDTO.valueOf(cardBrandLogoDO);
        } else {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Save card_brand_logo error");
        }
    }

    public CardBrandLogoUpdateDTO updateCardBrandLogo(CardBrandLogoUpdateDTO cardBrandLogoUpdateDTO)
            throws OceanException {
        final long currentMillis = System.currentTimeMillis();
        Optional<CardBrandLogoDO> cardBrandLogoOpt =
                cardBrandLogoDao.findByCardBrandAndNotDelete(cardBrandLogoUpdateDTO.getVersion(), cardBrandLogoUpdateDTO.getCardBrand());
        if (!cardBrandLogoOpt.isPresent()) {
            // new CardBrandLogo
            return createCardBrandLogo(cardBrandLogoUpdateDTO);
        }

        // update CardBrandLogo
        CardBrandLogoDO cardBrandLogo = cardBrandLogoOpt.get();
        cardBrandLogo.setImageName(
                this.getFileName(currentMillis, cardBrandLogoUpdateDTO.getFileName()));
        cardBrandLogo.setImageContent(cardBrandLogoUpdateDTO.getFileContent());
        cardBrandLogo.setImageSize(Math.toIntExact(cardBrandLogoUpdateDTO.getFileSize()));
        cardBrandLogo.setAuditStatus(cardBrandLogoUpdateDTO.getAuditStatus().getSymbol());
        cardBrandLogo.setUpdater(cardBrandLogoUpdateDTO.getUser());
        cardBrandLogo.setUpdateMillis(currentMillis);
        cardBrandLogo = cardBrandLogoDao.save(cardBrandLogo);

        if (null != cardBrandLogo.getId()) {
            return CardBrandLogoUpdateDTO.valueOf(cardBrandLogo);
        } else {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, "Save card_brand_logo error");
        }
    }

    public SimpleEntry<AuditStatus, byte[]> findIssuerLogoInBytes(final Long issuerBankId) {
        Optional<IssuerLogoDO> issuerLogoOpt =
                issuerLogoDao.findTopOneByIssuerBankIdAndNotDelete(issuerBankId);
        if (issuerLogoOpt.isPresent()) {
            if (null != issuerLogoOpt.get().getImageContent()) {
                IssuerLogoDO issuerLogo = issuerLogoOpt.get();
                return new SimpleEntry<AuditStatus, byte[]>(
                        AuditStatus.getStatusBySymbol(issuerLogo.getAuditStatus()),
                        issuerLogo.getImageContent());
            } else {
                throw new NoSuchDataException("Issuer bank logo disappeared, please upload again");
            }
        } else {
            throw new NoSuchDataException("Issuer bank logo is not existed, Please upload first");
        }
    }

    public String getFileName(long currentTimeMillis, String originalFilename) {
        return String.format("%d_%s", currentTimeMillis, originalFilename);
    }

    public IssuerLogoUpdateDto updateIssuerLogo(IssuerLogoUpdateDto updateDto) {
        final Long issuerBankId = updateDto.getIssuerBankId();
        IssuerLogoDO issuerLogo =
                issuerLogoDao
                        .findByIssuerBankId(issuerBankId)
                        .map(
                                entity -> {
                                    entity.setAuditStatus(updateDto.getAuditStatus().getSymbol());
                                    entity.setUpdater(updateDto.getUser());
                                    entity.setUpdateMillis(System.currentTimeMillis());
                                    if (AuditStatus.AUDITING.equals(updateDto.getAuditStatus())) {
                                        return entity;
                                    }
                                    entity.setImageName(updateDto.getFileName());
                                    entity.setImageSize(updateDto.getImageSize());
                                    entity.setImageContent(updateDto.getFileContent());
                                    return entity;
                                })
                        .map(issuerLogoDao::save)
                        .orElseThrow(() -> new NoSuchDataException("Issuer logo has not setup."));
        return IssuerLogoUpdateDto.valueOf(issuerLogo);
    }

    public Optional<IssuerLogoUpdateDto> getIssuerLogoByIssuerBankId(Long issuerBankId) {
        return issuerLogoDao.findByIssuerBankId(issuerBankId).map(IssuerLogoUpdateDto::valueOf);
    }

    public List<SystemSettingDTO> getAcsOperatorIds() {
        String className = "acs operator id";
        List<SystemSettingDO> data = sysDao.findByClassName(className);
        if (data == null || data.isEmpty()) {
            return Collections.emptyList();
        }
        List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();
        return data.stream()
                .map(SystemSettingDTO::valueOf)
                .sorted(
                        Comparator.comparingLong(
                                dto -> {
                                    CardBrandDTO cardBrandDTO =
                                            cardBrandList.stream()
                                                    .filter(
                                                            cardBrand ->
                                                                    cardBrand
                                                                            .getName()
                                                                            .equals(
                                                                                    dto
                                                                                            .getCategory()))
                                                    .findFirst()
                                                    .orElse(null);
                                    if (cardBrandDTO == null) {
                                        return Long.MAX_VALUE;
                                    } else {
                                        return cardBrandDTO.getDisplayOrder();
                                    }
                                }))
                .collect(Collectors.toList());
    }

    /** 修改 Operator ID */
    @Transactional(rollbackFor = Exception.class)
    public OperatorIdUpdateDto updateOperatorId(OperatorIdUpdateDto updateDto) {
        List<SystemSettingDTO> systemSettingDTOList =
                updateDto.getValueList().stream()
                        .map(
                                e ->
                                        sysDao.update(
                                                        SystemSettingDO.newInstance(
                                                                e.getId(),
                                                                e.getCategory(),
                                                                e.getClassName(),
                                                                e.getGroupName(),
                                                                e.getItem(),
                                                                e.getKey(),
                                                                e.getValue(),
                                                                e.getAuditStatus()),
                                                        updateDto.getUser())
                                                .orElse(null))
                        .collect(Collectors.toList())
                        .stream()
                        .map(SystemSettingDTO::valueOf)
                        .collect(Collectors.toList());

        updateDto.setValueList(systemSettingDTOList);
        return updateDto;
    }

    /** 取得修改前 Operator ID資料 */
    public Optional<OperatorIdUpdateDto> getOperatorIdUpdateDTOByAuditDraftData(
            AuditableDTO auditableDTO) {
        OperatorIdUpdateDto draftDto = null;
        if (auditableDTO instanceof OperatorIdUpdateDto) {
            draftDto = (OperatorIdUpdateDto) auditableDTO;
        }

        List<SystemSettingDTO> systemSettingDTOList =
                draftDto.getValueList().stream()
                        .map(e -> sysDao.findByID(e.getId()).orElse(null))
                        .collect(Collectors.toList())
                        .stream()
                        .map(SystemSettingDTO::valueOf)
                        .collect(Collectors.toList());
        OperatorIdUpdateDto originData = new OperatorIdUpdateDto();
        originData.setValueList(systemSettingDTOList);
        return Optional.of(originData);
    }

    /** 查詢基本設定 */
    public List<SystemSettingDO> getGeneralSetting() {
        return sysDao.findByClassName("timeout");
    }

    /** 修改基本設定 */
    public void updateGeneralSetting(GeneralSettingUpdateDTO updateDto) {
        List<SystemSettingDO> generalSettingList = getGeneralSetting();
        generalSettingList.forEach(generalSetting -> {
            if ("preq.resend.interval".equals(generalSetting.getKey())) {
                generalSetting.setValue(updateDto.getPreqResendInterval().toString());
            } else if ("areq.connection.timeout".equals(generalSetting.getKey())) {
                generalSetting.setValue(updateDto.getAreqConnectionTimeout().toString());
            }  else if ("rreq.connection.timeout".equals(generalSetting.getKey())) {
                generalSetting.setValue(updateDto.getRreqConnectionTimeout().toString());
            }  else if ("areq.read.timeout".equals(generalSetting.getKey())) {
                generalSetting.setValue(updateDto.getAreqReadTimeout().toString());
            }
            generalSetting.setUpdater(updateDto.getUser());
            generalSetting.setUpdateMillis(System.currentTimeMillis());
        });
        sysDao.updateAll(generalSettingList);
    }

    /** 更新驗證畫面設定 */
    public ChallengeViewMessageDTO updateChallengeViewMessage(ChallengeViewMessageDTO updateDto) {
        Long issuerBankId = updateDto.getIssuerBankId();
        String languageCode = updateDto.getLanguageCode();
        ChallengeViewMessageDTO.VerifyPage phoneVerifyPageDto = updateDto.getPhoneVerifyPage();
        ChallengeViewMessageDTO.VerifyPage otpVerifyPageDto = updateDto.getOtpVerifyPage();

        ChallengeViewMessageDO challengeViewMessageDO = null;
        if (phoneVerifyPageDto != null) {
            // DB中的欄位限制為not null
            if (phoneVerifyPageDto.getBtnBodyCancelButton() == null) {
                phoneVerifyPageDto.setBtnBodyCancelButton(
                        otpVerifyPageDto.getBtnBodyCancelButton());
            }

            challengeViewMessageDO =
                    saveChallengeViewMessage(
                            ChallengeViewMessageCategory.phoneVerifyPage,
                            phoneVerifyPageDto.getId(),
                            issuerBankId,
                            languageCode,
                            phoneVerifyPageDto,
                            updateDto.getUser());
        }
        if (otpVerifyPageDto != null) {
            challengeViewMessageDO =
                    saveChallengeViewMessage(
                            ChallengeViewMessageCategory.otpVerifyPage,
                            otpVerifyPageDto.getId(),
                            issuerBankId,
                            languageCode,
                            otpVerifyPageDto,
                            updateDto.getUser());
            if (otpVerifyPageDto.getOtpVerifySetting() != null) {
                saveChallengeViewOtpSetting(
                        issuerBankId,
                        languageCode,
                        otpVerifyPageDto.getOtpVerifySetting(),
                        updateDto.getUser());
            }
        }
        if (challengeViewMessageDO != null) {
            updateDto.setId(challengeViewMessageDO.getId());
        }
        return updateDto;
    }

    /** 儲存驗證畫面設定 */
    private ChallengeViewMessageDO saveChallengeViewMessage(
            ChallengeViewMessageCategory category,
            Long id,
            Long issuerBankId,
            String languageCode,
            ChallengeViewMessageDTO.VerifyPage dto,
            String user) {

        ChallengeViewMessageDO challengeViewMessage =
                challengeViewMessageDao
                        .findById(id)
                        .map(
                                c -> {
                                    c.setUpdateMillis(System.currentTimeMillis());
                                    c.setUpdater(user);
                                    return c;
                                })
                        .orElseGet(
                                () ->
                                        ChallengeViewMessageDO.builder()
                                                .issuerBankId(issuerBankId)
                                                .category(
                                                        ChallengeViewMessageCategory.valueOf(
                                                                dto.getCategory()))
                                                .creator(user)
                                                .build());

        replaceJsNewLineToHtmlBrSymbol(dto);

        challengeViewMessage.setLanguageCode(languageCode);

        updateChallengeViewMessageByVerifyPageDTO(category, challengeViewMessage, dto);

        try {
            return challengeViewMessageDao.saveOrUpdate(challengeViewMessage);
        } catch (DatabaseException e) {
            log.error(
                    "[saveChallengeViewMessage] database error, ChallengeViewMessage. id={}, verifyPage={}, updater={}",
                    id,
                    dto,
                    user,
                    e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    private void updateChallengeViewMessageByVerifyPageDTO(
            ChallengeViewMessageCategory category,
            ChallengeViewMessageDO challengeViewMessage,
            ChallengeViewMessageDTO.VerifyPage dto) {
        challengeViewMessage.setMainBodyTitle(dto.getMainBodyTitle());
        challengeViewMessage.setMainBodyMessage(dto.getMainBodyMessage());
        challengeViewMessage.setNpaMainBodyMessage(dto.getNpaMainBodyMessage());

        challengeViewMessage.setWebRemarkBodyMessage(dto.getWebRemarkBodyMessage());
        challengeViewMessage.setWebRemarkBodyColor(dto.getWebRemarkBodyColor());
        challengeViewMessage.setAppRemarkBodyMessage(dto.getAppRemarkBodyMessage());

        challengeViewMessage.setVerifyInputPlaceholder(dto.getVerifyInputPlaceholder());

        challengeViewMessage.setBtnBodySubmitButton(dto.getBtnBodySubmitButton());
        challengeViewMessage.setBtnBodyPhoneErrorButton(dto.getBtnBodyPhoneErrorButton());
        challengeViewMessage.setBtnBodyOtpResendButton(dto.getBtnBodyOtpResendButton());
        challengeViewMessage.setBtnBodyCancelButton(dto.getBtnBodyCancelButton());

        challengeViewMessage.setPhoneErrorBodyMessage(dto.getPhoneErrorBodyMessage());
        challengeViewMessage.setPhoneErrorBodySymbol(dto.getPhoneErrorBodySymbol());

        challengeViewMessage.setFooterLabel1(dto.getFooterLabel1());
        challengeViewMessage.setFooterMessage1(dto.getFooterMessage1());
        challengeViewMessage.setFooterLabel2(dto.getFooterLabel2());
        challengeViewMessage.setFooterMessage2(dto.getFooterMessage2());

        if (ChallengeViewMessageCategory.otpVerifyPage == category) {
            OtpSetting otpSetting = dto.getOtpVerifySetting();
            challengeViewMessage.setVerifyFailedMessage(otpSetting.getVerifyFailMessage());
            challengeViewMessage.setResendMessage(otpSetting.getResendMessage());
            challengeViewMessage.setNotResendMessage(otpSetting.getNotResendMessage());
        }

        challengeViewMessage.setAuditStatus(dto.getAuditStatus().getSymbol());
    }

    public void replaceJsNewLineToHtmlBrSymbol(ChallengeViewMessageDTO.VerifyPage dto) {
        String[] jsNewLineAry = new String[] {"\n", "\r", "\r\n"};
        String htmlNewLine = "<br>";
        for (String jsNewLine : jsNewLineAry) {
            if (null != dto.getMainBodyMessage()) {
                dto.setMainBodyMessage(dto.getMainBodyMessage().replaceAll(jsNewLine, htmlNewLine));
            }
            if (null != dto.getNpaMainBodyMessage()) {
                dto.setNpaMainBodyMessage(dto.getNpaMainBodyMessage().replaceAll(jsNewLine, htmlNewLine));
            }
            if (null != dto.getWebRemarkBodyMessage()) {
                dto.setWebRemarkBodyMessage(
                        dto.getWebRemarkBodyMessage().replaceAll(jsNewLine, htmlNewLine));
            }
            if (null != dto.getAppRemarkBodyMessage()) {
                dto.setAppRemarkBodyMessage(
                        dto.getAppRemarkBodyMessage().replaceAll(jsNewLine, "<br>"));
            }
            if (null != dto.getPhoneErrorBodyMessage()) {
                dto.setPhoneErrorBodyMessage(
                        dto.getPhoneErrorBodyMessage().replaceAll(jsNewLine, "<br>"));
            }
            if (null != dto.getFooterMessage1()) {
                dto.setFooterMessage1(dto.getFooterMessage1().replaceAll(jsNewLine, "<br>"));
            }
            if (null != dto.getFooterMessage2()) {
                dto.setFooterMessage2(dto.getFooterMessage2().replaceAll(jsNewLine, "<br>"));
            }
        }
    }

    /** 儲存OTP驗證設定 */
    private Optional<ChallengeViewOtpSettingDO> saveChallengeViewOtpSetting(
            Long issuerBankId, String languageCode, OtpSetting otpSettingDto, String user) {
        if (otpSettingDto == null) {
            return Optional.empty();
        }

        ChallengeViewOtpSettingDO otpSetting =
                challengeViewOtpSettingDao
                        .findById(otpSettingDto.getId())
                        .map(
                                c -> {
                                    c.setUpdater(user);
                                    c.setUpdateMillis(System.currentTimeMillis());
                                    return c;
                                })
                        .orElseGet(
                                () ->
                                        ChallengeViewOtpSettingDO.builder()
                                                .issuerBankId(issuerBankId)
                                                .languageCode(languageCode)
                                                .creator(user)
                                                .build());
        otpSetting.setMaxResendTimes(otpSettingDto.getMaxResend());
        otpSetting.setMaxChallengeTimes(otpSettingDto.getMaxVerify());
        otpSetting.setAuditStatus(otpSettingDto.getAuditStatus().getSymbol());
        try {
            return Optional.of(challengeViewOtpSettingDao.saveOrUpdate(otpSetting));
        } catch (DatabaseException e) {
            log.error("[saveChallengeViewOtpSetting] database error", e);
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage());
        }
    }

    /** 取得語系清單 */
    public List<LanguageResDTO> listLanguage() {
        List<LangDO> result = langDao.findAll();
        return result.stream().map(LanguageResDTO::valueOf).collect(Collectors.toList());
    }

    /** 取得驗證設定畫面（手機號碼確認畫面內容，簡訊驗證碼確認畫面內容，OTP驗證設定） */
    public Optional<ChallengeViewMessageDTO> getChallengeViewMessage(
            Long issuerBankId, String languageCode) {
        List<ChallengeViewMessageDO> list =
                challengeViewMessageDao.findByIssuerBankIdAndLanguageCode(
                        issuerBankId, languageCode);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }

        ChallengeViewMessageDTO resDto = new ChallengeViewMessageDTO();
        for (ChallengeViewMessageDO viewMsg : list) {
            if (ChallengeViewMessageCategory.phoneVerifyPage.equals(viewMsg.getCategory())) {
                ChallengeViewMessageDTO.VerifyPage phoneVerifyPage =
                        ChallengeViewMessageDTO.VerifyPage.valueOfPhoneVerifyPage(viewMsg);

                resDto.setPhoneVerifyPage(phoneVerifyPage);
            } else if (ChallengeViewMessageCategory.otpVerifyPage.equals(viewMsg.getCategory())) {
                OtpSetting otpSetting =
                        challengeViewOtpSettingDao
                                .findByIssuerBankId(issuerBankId)
                                .map(otpSettingDO-> OtpSetting.valueOf(viewMsg, otpSettingDO))
                                .orElse(null);

                ChallengeViewMessageDTO.VerifyPage otpVerifyPage =
                        ChallengeViewMessageDTO.VerifyPage.valueOfOtpVerifyPage(viewMsg, otpSetting);
                resDto.setOtpVerifyPage(otpVerifyPage);
            }
        }
        return Optional.of(resDto);
    }

    public List<ErrorIssueGroupDTO> getErrorIssueGroup() {
        List<ErrorIssueGroupDO> groupList = errorCodeDAO.getGroupList();
        List<ErrorIssueGroupDTO> groupDTOList = new ArrayList<>();
        for (ErrorIssueGroupDO issueGroup : groupList) {
            ErrorIssueGroupDTO groupDTO = ErrorIssueGroupDTO.valueOf(issueGroup);
            groupDTOList.add(groupDTO);
        }

        return groupDTOList;
    }

    public Optional<ErrorIssueGroupDTO> getErrorCodeByGroupId(Long groupId) {
        Optional<ErrorIssueGroupDO> errorCodeGroupOpt = errorCodeDAO.getByGroupId(groupId);

        if (!errorCodeGroupOpt.isPresent()) return Optional.empty();

        ErrorIssueGroupDO errorCodeGroup = errorCodeGroupOpt.get();
        return Optional.of(ErrorIssueGroupDTO.valueOf(errorCodeGroup));
    }

    public ErrorIssueGroupDTO updateErrorCodeMessageGroup(ErrorIssueGroupDTO errorCodeGroupDTO) {

        List<ErrorCodeMappingDO> codeMappingDOList = new ArrayList<>();
        for (ErrorCodeMappingDTO codeMappingDTO : errorCodeGroupDTO.getCodeMappingList()) {
            ErrorCodeMappingDO codeMappingDO =
                    ErrorCodeMappingDO.newInstance(
                            codeMappingDTO.getId(),
                            codeMappingDTO.getErrorCode(),
                            codeMappingDTO.getErrorMsg());
            codeMappingDOList.add(codeMappingDO);
        }

        ErrorIssueGroupDO issueGroup =
                ErrorIssueGroupDO.newInstance(
                        errorCodeGroupDTO.getId(),
                        errorCodeGroupDTO.getAuditStatus(),
                        errorCodeGroupDTO.getGroupName(),
                        errorCodeGroupDTO.getUser(),
                        codeMappingDOList);

        ErrorIssueGroupDO updateResult = errorCodeDAO.updateGroupMessage(issueGroup);
        return ErrorIssueGroupDTO.valueOf(updateResult);
    }

    public Optional<SecretKeyDO> findOneSecretKey(Long secretKeyId) throws OceanException {
        try {
            return secretKeyDao.findOneSecretKey(secretKeyId);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public List<SecretKeyDTO> getKeyList(Long issuerBankId) throws OceanException {
        try {
            List<SecretKeyDTO> secretKeyDTOList =
                    secretKeyDao.findByIssuerBankId(issuerBankId).stream()
                            .map(SecretKeyDTO::valueOf)
                            .collect(Collectors.toList());

            List<CardBrandDTO> cardBrandList = cardBrandConfigurationHelper.findCardBrandList();

            cardBrandList.forEach(
                    cardBrandDTO -> {
                        Optional<SecretKeyDTO> existsSecretKey =
                                secretKeyDTOList.stream()
                                        .filter(
                                                secretKey ->
                                                        secretKey
                                                                .getCardBrand()
                                                                .equals(cardBrandDTO.getName()))
                                        .findFirst();
                        if (!existsSecretKey.isPresent()) {
                            secretKeyDTOList.add(createEmptySecretKeyDTO(cardBrandDTO.getName()));
                        }
                    });
            return secretKeyDTOList;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    private SecretKeyDTO createEmptySecretKeyDTO(String cardBrand) {
        return new SecretKeyDTO(null, null, cardBrand, "", "", AuditStatus.UNKNOWN);
    }

    public void saveOrUpdateKeyOneKey(
            Long issuerBankId,
            String cardBrand,
            String key,
            CavvKeyType cavvKeyType,
            String updater) {

        try {
            // find key
            SecretKeyDO secretKeyDO = secretKeyDao.getKey(issuerBankId, cardBrand).orElse(null);

            if (secretKeyDO == null) {
                // create new SecretKeyDO
                secretKeyDO = SecretKeyDO.newInstance(null, issuerBankId, cardBrand, null, null);
                secretKeyDO.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                secretKeyDO.setCreator(updater);
                secretKeyDO.setCreateMillis(System.currentTimeMillis());
            }

            // update
            if (cavvKeyType == CavvKeyType.KEY_A) {
                secretKeyDO.setKeyA(key);
            } else {
                secretKeyDO.setKeyB(key);
            }
            secretKeyDO.setUpdater(updater);
            secretKeyDO.setUpdateMillis(System.currentTimeMillis());

            // save
            secretKeyDao.saveOrUpdate(secretKeyDO);
        } catch (DatabaseException e) {
            throw new OceanException(ResultStatus.DB_SAVE_ERROR, e.getMessage(), e);
        }
    }

    public SecretKeyDTO saveOrUpdateKey(@Valid SecretKeyDTO reqDto) throws OceanException {
        boolean isCreated = reqDto.getId() == null;
        try {
            boolean existsSecretKey =
                    secretKeyDao.existsSecretKey(reqDto.getIssuerBankId(), reqDto.getCardBrand());

            if (isCreated && !existsSecretKey) {
                // 新增
                SecretKeyDO entity =
                        SecretKeyDO.newInstance(
                                reqDto.getId(),
                                reqDto.getIssuerBankId(),
                                reqDto.getCardBrand(),
                                reqDto.getKeyA(),
                                reqDto.getKeyB());
                entity.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
                entity.setCreator(reqDto.getOperator());
                entity.setCreateMillis(System.currentTimeMillis());
                secretKeyDao.saveOrUpdate(entity);
            } else if (isCreated && existsSecretKey) {
                // 有帶銀行id和cardBrand卻沒帶secretKey id，則拋出異常
                throw new NoSuchDataException("Missing required argument:id");
            } else {
                // 更新
                SecretKeyDO entity =
                        secretKeyDao
                                .findOneSecretKey(reqDto.getId())
                                .orElseThrow(
                                        () ->
                                                new NoSuchDataException(
                                                        "Id=" + reqDto.getId() + " not found."));
                entity.setKeyA(reqDto.getKeyA());
                entity.setKeyB(reqDto.getKeyB());
                entity.setAuditStatus(reqDto.getAuditStatus().getSymbol());
                entity.setUpdater(reqDto.getOperator());
                entity.setUpdateMillis(System.currentTimeMillis());
                secretKeyDao.saveOrUpdate(entity);
            }
            return reqDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus());
        }
    }

    public Optional<SmsTemplateDTO> findOneSmsTemplate(Long issuerBankId) throws OceanException {
        try {
            Optional<SmsTemplateDO> opt = smsTemplateDao.findByIssuerBankId(issuerBankId);
            return opt.map(SmsTemplateDTO::valueOf);
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    public SmsTemplateDTO updateSmsTemplate(SmsTemplateDTO reqDto) throws OceanException {
        try {
            SmsTemplateDO entity =
                    smsTemplateDao
                            .findById(reqDto.getId())
                            .orElseThrow(
                                    () ->
                                            new NoSuchDataException(
                                                    "Sms-template not found by issuerBankId="
                                                            + reqDto.getIssuerBankId()));
            if (null != reqDto.getExpireMillis()) {
                entity.setExpireMillis(reqDto.getExpireMillis());
            }
            if (null != reqDto.getVerifyMessage()) {
                entity.setVerifyMessage(reqDto.getVerifyMessage());
            }
            if (null != reqDto.getExceedLimitMessage()) {
                entity.setExceedLimitMessage(reqDto.getExceedLimitMessage());
            }
            if (null != reqDto.getAuditStatus()) {
                entity.setAuditStatus(reqDto.getAuditStatus().getSymbol());
            }
            entity.setUpdater(reqDto.getOperator());
            entity.setUpdateMillis(System.currentTimeMillis());
            entity = smsTemplateDao.saveOrUpdate(entity);
            reqDto.setId(entity.getId());
            return reqDto;
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }

    /** 呼叫integrator解密HMAC */
    @LogInfo(end = true)
    public void loadKeyStore() throws Exception {
        int retryInitValue = 0;
        doLoadHmacKey(retryInitValue);
        //doLoadCavvEncryptKey();
    }

    private void doLoadCavvEncryptKey() {
        // load private key
        KeyStoreDO cavvEncryptPrivateKey =
            keyStoreDao.findByKeyName(KeyStoreKeyName.CAVV_ENC_KEY_PRI.name()).orElse(null);
        if (cavvEncryptPrivateKey == null) {
            throw new OceanException(
                ResultStatus.SERVER_ERROR, "CAVV_ENC_KEY_PRI not found in KEY_STORE");
        }
        EnvironmentConstants.cavvEncryptPrivateKey = cavvEncryptPrivateKey.getKeyBody();

        // load public key
        KeyStoreDO cavvEncryptPublicKey =
            keyStoreDao.findByKeyName(KeyStoreKeyName.CAVV_ENC_KEY_PUB.name()).orElse(null);
        if (cavvEncryptPublicKey == null) {
            throw new OceanException(
                ResultStatus.SERVER_ERROR, "CAVV_ENC_KEY_PUB not found in KEY_STORE");
        }
        EnvironmentConstants.cavvEncryptPublicKey = cavvEncryptPublicKey.getKeyBody();
    }

    private void doLoadHmacKey(int retryCount) throws Exception {
        int decryptErrorRetryCount =
                Integer.parseInt(environment.getProperty("decrypt.error.retry.count", "3"));
        long decryptErrorRetryGap =
                Long.parseLong(environment.getProperty("decrypt.error.retry.gap", "5000"));
        if (retryCount >= 1) {
            Thread.sleep(decryptErrorRetryGap);
        }
        if (retryCount >= decryptErrorRetryCount) {
            throw new OceanException(
                    ResultStatus.SERVER_ERROR, "[doLoadHmacKey] decrypt HMAC-Key error.");
        }
        Optional<KeyStoreDO> keyStoreOpt = keyStoreDao.findByKeyName(KeyStoreKeyName.HMAC.name());
        String noSuchHmacKeyErrMsg = "Please provide an encryption HMAC Key.";
        String hamcKeyEncryptBase64Str;

        if (keyStoreOpt.isPresent()) {
            hamcKeyEncryptBase64Str = keyStoreOpt.get().getKeyBody();
        } else {
            throw new NoSuchElementException(noSuchHmacKeyErrMsg);
        }
        if (StringUtils.isBlank(hamcKeyEncryptBase64Str)) {
            throw new NoSuchElementException(noSuchHmacKeyErrMsg);
        }

        // decrypt hmac key
        String hamcKeyHexStr = null;
        DecryptResultDTO decryptResultDTO = null;
        try {
            decryptResultDTO =
                    hsmPlugin
                            .decryptWithIssuerBankId(
                                    hamcKeyEncryptBase64Str,
                                    EnvironmentConstants.ORG_ISSUER_BANK_ID);
            hamcKeyHexStr = decryptResultDTO.getString();
        } catch (Exception e) {
            log.error("[doLoadHmacKey] decrypt hmac key failed.", e);
            // retry
            doLoadHmacKey(++retryCount);
            return;
        } finally {
            if (decryptResultDTO != null) {
                decryptResultDTO.clearPlainText();
            }
        }

        EnvironmentConstants.hmacHashKey = Hex.decodeHex(hamcKeyHexStr);
    }
}
