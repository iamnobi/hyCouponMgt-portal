package ocean.acs.models.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.data_object.portal.BinRangeQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBinRangeDO;

public interface BinRangeDAO {

    PagingResultDO<PortalBinRangeDO> query(BinRangeQueryDO queryDO);

    List<BinRangeDO> findByStartBinBetweenEndBin(Long issuerBankId, BigInteger cardNumber)
            throws DatabaseException;

    boolean existsByIssuerBankIdAndCardNumberInBinRange(Long issuerBankId, String cardNumber)
            throws DatabaseException;

    String findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(Long issuerBankId,
            String cardNumberWithoutPadding);

    String findCardBrandByIssuerBankIdAndCardNumber(Long issuerBankId,
            BigInteger cardNumberWithPadding);

    List<Long> findIssuerBankIdByCardNumber(BigInteger cardNumber) throws DatabaseException;

    /**
     * 是否在現存的BinRange範圍中
     *
     * @param binRange
     */
    Optional<BinRangeDO> existingBinRangeConflict(BinRangeDO binRange);

    BinRangeDO add(BinRangeDO binRange);

    BinRangeDO update(BinRangeDO binRange);

    boolean delete(long id, String deleter);

    Optional<BinRangeDO> findById(Long binRangeId, Long issuerBankId);

    List<BinRangeDO> listByIssuerBankId(Long issuerBankId);

    Boolean existsByIssuerBankId(Long issuerBankId);

    int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis);

}
