package com.cherri.acs_portal.service.validator;

import com.cherri.acs_portal.constant.EnvironmentConstants;
import com.cherri.acs_portal.util.HmacUtils;
import com.google.common.collect.Sets;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.dao.BlackListPanBatchDAO;
import ocean.acs.models.dao.BlackListPanDAO;
import ocean.acs.models.dao.WhiteListPanDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 和卡號有關的功能新增時的防呆檢核條件驗證 */
@Log4j2
@Component
public class CardImportValidator {

  private final BinRangeDAO binRangeDao;
  private final BlackListPanDAO blackListPanDao;
  private final BlackListPanBatchDAO panBatchDao;
  private final WhiteListPanDAO whiteListPanDao;

  @Autowired
  public CardImportValidator(
      BinRangeDAO binRangeDao,
      BlackListPanDAO blackListPanDao,
      BlackListPanBatchDAO panBatchDao,
      WhiteListPanDAO whiteListPanDao) {
    this.binRangeDao = binRangeDao;
    this.blackListPanDao = blackListPanDao;
    this.panBatchDao = panBatchDao;
    this.whiteListPanDao = whiteListPanDao;
  }

  /** 檢查是否有建立自行的BinRange */
  public boolean isBinRangeExists(Long issuerBankId) {
    Boolean result = binRangeDao.existsByIssuerBankId(issuerBankId);
    return result == null ? false : result;
  }

  /**
   * 檢查卡號是否存在於自行的BinRange
   *
   * @param issuerBankId 銀行ID
   * @param cards 卡號清單
   * @return 若為空字串，則卡號皆在BinRange範圍中，否則反之
   */
  public String extractNotExistsInBinRangeCard(Long issuerBankId, Set<String> cards)
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
   * 檢查卡號是否存在於自行的BinRange
   *
   * @param issuerBankId 銀行ID
   * @param cardNumber 卡號
   * @return 若為空字串，則卡號皆在BinRange範圍中，否則反之
   */
  public String extractNotExistsInBinRangeCard(Long issuerBankId, String cardNumber)
      throws OceanException {
    return extractNotExistsInBinRangeCard(issuerBankId, Sets.newHashSet(cardNumber));
  }

  /** 判斷卡號是否已存在黑名單 */
  public boolean isCardNumberExistedInBlackList(Long issuerBankId, String cardNumber)
      throws OceanException {
    try {
      Long singleAddedBatchId = panBatchDao.getSingleAddedBatchId(issuerBankId);
      String cardNumberHash = HmacUtils.encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
      return blackListPanDao.existsByCardNumberHashAndBatchId(
          issuerBankId, cardNumberHash, singleAddedBatchId);
    } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }
  }

  /**判斷卡號是否已存在於其他銀行 */
  public boolean isCardNumberExistedInOtherBank(Long issuerBankId, String cardNumber)
      throws OceanException {
    try {
      List<Long> queryResult = binRangeDao.findIssuerBankIdByCardNumber(new BigInteger(cardNumber));
      return queryResult.stream()
          .anyMatch(
              binRangeBankId -> {
                boolean existsOtherBank = !issuerBankId.equals(binRangeBankId);
                if (existsOtherBank) {
                  log.warn(
                      "[isCardNumberExistedInOtherBank] the card number exists in other banks, bankId={}",
                      binRangeBankId);
                }
                return existsOtherBank;
              });
    } catch (DatabaseException e) {
      throw new OceanException(e.getResultStatus(), e.getMessage());
    }
  }

  /**
   * 檢查批次卡號黑名單中是否有包含其他銀行的卡
   *
   * @param issuerBankId 銀行ID
   * @param cards 批次卡號黑名單
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

    /** 判斷卡號是否已存在黑名單 */
    public boolean isCardNumberExistedInBlackList(Long issuerBankId, String cardNumber,
      Long batchId)
      throws DatabaseException {
        String cardNumberHash = HmacUtils.encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
        return blackListPanDao
          .existsByCardNumberHashAndBatchId(issuerBankId, cardNumberHash, batchId);
    }

    /** 判斷卡號是否已存在白名單 */
    public boolean isCardNumberExistedInWhiteList(Long issuerBankId, String cardNumber)
      throws OceanException {
        String cardNumberHash = HmacUtils.encrypt(cardNumber, EnvironmentConstants.hmacHashKey);
        return whiteListPanDao.existsCardNumberHash(issuerBankId, cardNumberHash);
    }
}
