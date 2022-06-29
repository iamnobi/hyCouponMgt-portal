package com.cherri.acs_portal.service;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toMap;

import com.cherri.acs_portal.component.HsmPlugin;
import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchImportDTO;
import com.cherri.acs_portal.dto.blackList.input.BatchQueryDTO;
import com.cherri.acs_portal.manager.AcsIntegratorManager;
import com.cherri.acs_portal.service.validator.CardImportValidator;
import com.cherri.acs_portal.util.FileUtils;
import com.cherri.acs_portal.util.HmacUtils;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.BlackListAuthStatusCategory;
import ocean.acs.commons.enumerator.CardStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.commons.utils.MaskUtils;
import ocean.acs.models.dao.BatchImportDAO;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.BlackListAuthStatusDAO;
import ocean.acs.models.dao.BlackListPanBatchDAO;
import ocean.acs.models.dao.BlackListPanDAO;
import ocean.acs.models.dao.PanInfoDAO;
import ocean.acs.models.dao.PanOtpStatisticsDAO;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.data_object.entity.BlackListPanDO;
import ocean.acs.models.data_object.entity.PanInfoDO;
import ocean.acs.models.data_object.entity.PanOtpStatisticsDO;
import ocean.acs.models.data_object.portal.BatchQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class BatchImportService {

    private final BatchImportDAO batchImportDAO;
    private final BlackListPanDAO blackListPanDao;
    private final PanInfoDAO panInfoDAO;
    private final PanOtpStatisticsDAO panOtpStatisticsDao;
    private final AcsIntegratorManager acsIntegratorManager;
    private final BinRangeDAO binRangeDao;
    private final HsmPlugin hsmPlugin;
    private final CardImportValidator cardImportValidator;
    private final BlackListAuthStatusDAO authStatusDao;
    private final BlackListPanBatchDAO panBatchDao;

    @Autowired
    public BatchImportService(
      BatchImportDAO batchImportDAO,
      BlackListPanDAO blackListPanDao,
      PanInfoDAO panInfoDAO,
      PanOtpStatisticsDAO panOtpStatisticsDao,
      AcsIntegratorManager acsIntegratorManager,
      BinRangeDAO binRangeDao,
      HsmPlugin hsmPlugin,
        CardImportValidator cardImportValidator,
        BlackListAuthStatusDAO authStatusDao,
        BlackListPanBatchDAO panBatchDao) {
        this.batchImportDAO = batchImportDAO;
        this.blackListPanDao = blackListPanDao;
        this.panInfoDAO = panInfoDAO;
        this.panOtpStatisticsDao = panOtpStatisticsDao;
        this.acsIntegratorManager = acsIntegratorManager;
        this.binRangeDao = binRangeDao;
        this.hsmPlugin = hsmPlugin;
        this.cardImportValidator = cardImportValidator;
        this.authStatusDao = authStatusDao;
        this.panBatchDao = panBatchDao;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public long addBlackListByBatch(BatchImportDTO importDTO) {
        Set<String> cards = FileUtils.extractCardList(importDTO.getFileContent());

        // 檢查是否有BinRange資料
        if (!binRangeDao.existsByIssuerBankId(importDTO.getIssuerBankId())) {
            throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
        }
        // 檢查卡號是否符合自行的BinRange
        String notInBinRangeCardList =
          extractNotExistsInBinRangeCards(importDTO.getIssuerBankId(), cards);
        if (StringUtils.isNotEmpty(notInBinRangeCardList)) {
            throw new OceanException(
              ResultStatus.NO_SELF_BANK_BIN_RANGE,
              "Does not exists in Bin-Range:" + notInBinRangeCardList);
        }

        // 檢查是否包含其他銀行的卡號
        String otherBankCardsText = extractOtherBankCards(importDTO.getIssuerBankId(), cards);
        if (StringUtils.isNotEmpty(otherBankCardsText)) {
            throw new OceanException(
              ResultStatus.NON_SELF_BANK_CARD_NUMBER,
              "The batch file contains other bank cards:" + otherBankCardsText);
        }

        importDTO.setPanNumber(cards.size());

        TransStatus authStatus = authStatusDao
            .getAuthStatus(importDTO.getIssuerBankId(), BlackListAuthStatusCategory.BLACK_LIST_PAN);
        Long singleAddedBatchId = panBatchDao.getSingleAddedBatchId(importDTO.getIssuerBankId());

        long addCount = 0;
        for (String cardNumber : cards) {
            // 卡號是否已存在於黑名單中
            boolean existsInBlacklist =
                cardImportValidator.isCardNumberExistedInBlackList(
                    importDTO.getIssuerBankId(), cardNumber);
            if (existsInBlacklist) {
                continue;
            }
            String cardBrand =
                binRangeDao.findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(
                    importDTO.getIssuerBankId(), cardNumber);
            String maskCardNumber = MaskUtils.acctNumberMask(cardNumber);
            String encryptCardNumber =
                hsmPlugin.encryptWithIssuerBankId(cardNumber, importDTO.getIssuerBankId()).getBase64();
            String hashCardNumber = ocean.acs.commons.utils.HmacUtils
                .encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
            PanInfoDO panInfo =
                panInfoDAO.findOrCreatePanInfo(
                    importDTO.getIssuerBankId(),
                    cardBrand,
                    maskCardNumber,
                    encryptCardNumber,
                    hashCardNumber,
                    importDTO.getUser());
            blackListPanDao.add(panInfo, singleAddedBatchId, importDTO.getUser(), authStatus);
            addCount += 1;
        }
        return addCount;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public long deleteBlackListByBatch(BatchImportDTO importDTO) {
        Set<String> cards = FileUtils.extractCardList(importDTO.getFileContent());

        // 檢查是否有BinRange資料
        if (!binRangeDao.existsByIssuerBankId(importDTO.getIssuerBankId())) {
            throw new OceanException(ResultStatus.NO_SELF_BANK_BIN_RANGE, "No Bin-Range data");
        }

        // 檢查是否包含其他銀行的卡號
        String otherBankCardsText = extractOtherBankCards(importDTO.getIssuerBankId(), cards);
        if (StringUtils.isNotEmpty(otherBankCardsText)) {
            throw new OceanException(
                ResultStatus.NON_SELF_BANK_CARD_NUMBER,
                "The batch file contains other bank cards:" + otherBankCardsText);
        }

        long deletedCount = 0;
        for (String cardNumber : cards) {
            String hashCardNumber = ocean.acs.commons.utils.HmacUtils
                .encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
            PanInfoDO panInfoDO = panInfoDAO
                .findByCardNumber(importDTO.getIssuerBankId(), hashCardNumber).orElse(null);
            if (panInfoDO == null) {
                continue;
            }
            deletedCount += blackListPanDao.deleteByPanInfoId(panInfoDO.getId(), importDTO.getUser(),
                System.currentTimeMillis());
        }
        return deletedCount;
    }

    public PagingResultDTO<BatchImportDTO> queryPanBlackListBatch(BatchQueryDTO queryDTO) {
        BatchQueryDO batchQueryDO = BatchQueryDO.builder()
          .page(queryDTO.getPage()).pageSize(queryDTO.getPageSize())
          .authStatus(queryDTO.getAuthStatus()).transStatus(queryDTO.getTransStatus())
          .batchName(queryDTO.getBatchName())
          .startTime(queryDTO.getStartTime()).endTime(queryDTO.getEndTime())
          .issuerBankId(queryDTO.getIssuerBankId()).id(queryDTO.getId())
          .pan(queryDTO.getPan())
          .build();

        PagingResultDO<BatchImportDO> batchImportResult =
          batchImportDAO.queryPanBlackListBatch(batchQueryDO);
        List<BatchImportDTO> resultDTOList =
          batchImportResult.getData().stream()
            .map(BatchImportDTO::valueOf)
            .collect(Collectors.toList());

        PagingResultDTO<BatchImportDTO> batchImportDTOResult = new PagingResultDTO<>();
        batchImportDTOResult.setTotalPages(batchImportResult.getTotalPages());
        batchImportDTOResult.setTotal(batchImportResult.getTotal());
        batchImportDTOResult.setCurrentPage(batchImportResult.getCurrentPage());

        batchImportDTOResult.setData(resultDTOList);

        return batchImportDTOResult;
    }

    public BatchImportDTO updatePanBlackListBatch(BatchImportDTO batchImportDTO) {
        Optional<BatchImportDO> repoContentOpt = batchImportDAO.getById(batchImportDTO.getId());

        if (!repoContentOpt.isPresent()) {
            String errMsg = String
              .format("[updatePanBlackListBatch] Update target does not exist. batchId=%d",
                batchImportDTO.getId());
            log.warn(errMsg);
            throw new NoSuchDataException(errMsg);
        }
        BatchImportDO repoContent = repoContentOpt.get();

        if (StringCustomizedUtils.isNotEmpty(batchImportDTO.getBatchName())) {
            repoContent.setBatchName(batchImportDTO.getBatchName());
        }

        if (StringCustomizedUtils.isNotEmpty(batchImportDTO.getAuthStatus())) {
            TransStatus transStatus = TransStatus.codeOf(batchImportDTO.getAuthStatus());
            repoContent.setTransStatus(transStatus.getCode());
        }
        repoContent.setAuditStatus(batchImportDTO.getAuditStatus().getSymbol());
        repoContent.setUpdateMillis(System.currentTimeMillis());
        repoContent.setUpdater(batchImportDTO.getUser());

        BatchImportDO batchImport = batchImportDAO.update(repoContent);

        boolean isUpdatePanSuccess =
          blackListPanDao.updateTransStatusByBlackListPanBatchId(
            batchImportDTO.getTransStatus(), batchImportDTO.getUser(), repoContent.getId());
        if (isUpdatePanSuccess) {
            return BatchImportDTO.valueOf(batchImport);
        }
        throw new OceanException(ResultStatus.DB_SAVE_ERROR);
    }

    public DeleteDataDTO deletePanBlackListBatch(DeleteDataDTO deleteDataDTO) {
        Optional<BatchImportDO> repoContentOpt = batchImportDAO.getById(deleteDataDTO.getId());

        if (!repoContentOpt.isPresent()) {
            String errMsg = String
              .format("[deletePanBlackListBatch] Removed target does not exist. batchId=%d",
                deleteDataDTO.getId());
            log.warn(errMsg);
            throw new NoSuchDataException(errMsg);
        }
        BatchImportDO repoContent = repoContentOpt.get();

        repoContent.setDeleteFlag(true);
        repoContent.setDeleteMillis(System.currentTimeMillis());
        repoContent.setDeleter(deleteDataDTO.getUser());

        BatchImportDO batchImport = batchImportDAO.update(repoContent);
        blackListPanDao.deleteByBlackListPanBatchId(deleteDataDTO.getUser(), batchImport.getId());
        deleteDataDTO.setId(batchImport.getId());
        return deleteDataDTO;
    }

    public Optional<BatchImportDTO> getPanBlackListBatchById(Long batchId) {
        Optional<BatchImportDO> batchImport = batchImportDAO.getById(batchId);
        if (batchImport.isPresent()) {
            BatchImportDTO batchImportDTO = BatchImportDTO.valueOf(batchImport.get());
            return Optional.of(batchImportDTO);
        }
        return Optional.empty();
    }

    /**
     * 檢查卡號是否存在於自行的BinRange
     *
     * @param issuerBankId 銀行ID
     * @param cards        批次卡號黑名單
     * @return 若為空字串，則卡號皆在BinRange範圍中，否則反之
     */
    public String extractNotExistsInBinRangeCards(Long issuerBankId, Set<String> cards)
      throws OceanException {
        return cards.stream()
          .filter(
            cardNumber -> {
                try {
                    // 找出不存在於BinRange的卡號
                    return !binRangeDao.existsByIssuerBankIdAndCardNumberInBinRange(
                      issuerBankId, cardNumber);
                } catch (DatabaseException e) {
                    throw new OceanException(e.getResultStatus(), e.getMessage());
                }
            })
          .collect(Collectors.joining(", "));
    }

    /**
     * 檢查批次卡號黑名單中是否有包含其他銀行的卡
     *
     * @param issuerBankId 銀行ID
     * @param cards        批次卡號黑名單
     * @return 若為空字串，則該檔案皆是自行的卡號，否則反之
     */
    public String extractOtherBankCards(Long issuerBankId, Set<String> cards) {
        return cards.stream()
          .filter(cardNumber -> isOtherBankCard(issuerBankId, cardNumber))
          .collect(Collectors.joining(", "));
    }

    private boolean isOtherBankCard(Long selfBankId, String cardNumber) {
        try {
            cardNumber = StringUtils.rightPad(cardNumber, 19, "0");
            return binRangeDao.findIssuerBankIdByCardNumber(new BigInteger(cardNumber)).stream()
              .anyMatch(bankId -> !bankId.equals(selfBankId));
        } catch (DatabaseException e) {
            throw new OceanException(e.getResultStatus(), e.getMessage());
        }
    }
}
