package ocean.acs.models.oracle.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.constant.KernelEnvironmentConstant;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.commons.utils.PortalUtil;
import ocean.acs.commons.utils.StringCustomizedUtils;
import ocean.acs.models.dao.BinRangeDAO;
import ocean.acs.models.data_object.entity.BinRangeDO;
import ocean.acs.models.data_object.portal.BinRangeQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBinRangeDO;
import ocean.acs.models.oracle.entity.BinRange;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@AllArgsConstructor
public class BinRangeDAOImpl implements BinRangeDAO {

    private final ocean.acs.models.oracle.repository.BinRangeRepository repo;
    private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public PagingResultDO<PortalBinRangeDO> query(BinRangeQueryDO queryDO) {
        PageRequest page = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
          Sort.Direction.ASC, "cardBrand", "cardType", "startBin", "endBin");

        Page<BinRange> pageResult = repo.findAll((binRangeRoot, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predList = new ArrayList<>();
            Predicate pred;

            if (queryDO.getIssuerBankId() != null) {
                pred = criteriaBuilder.equal(
                  binRangeRoot.get("issuerBankId"), queryDO.getIssuerBankId());
                predList.add(pred);
            }

            if (queryDO.getCardBrand() != null) {
                pred = criteriaBuilder.equal(binRangeRoot.get("cardBrand"), queryDO.getCardBrand());
                predList.add(pred);
            }

            pred = criteriaBuilder.equal(binRangeRoot.get("deleteFlag"), 0);
            predList.add(pred);

            Predicate[] predicates = new Predicate[predList.size()];
            return criteriaBuilder.and(predList.toArray(predicates));
        }, page);

        PagingResultDO<PortalBinRangeDO> pagingResultDO = new PagingResultDO<>();
        if (pageResult.getTotalElements() == 0) {
            return PagingResultDO.empty();
        }
        List<PortalBinRangeDO> dtoList = pageResult.getContent().stream()
          .map(PortalBinRangeDO::valueOf).collect(Collectors.toList());
        pagingResultDO.setData(dtoList);
        pagingResultDO.setTotal(pageResult.getTotalElements());
        pagingResultDO.setCurrentPage(pageResult.getNumber() + 1);
        pagingResultDO.setTotalPages(pageResult.getTotalPages());
        return pagingResultDO;
    }

    @Override
    public List<BinRangeDO> findByStartBinBetweenEndBin(Long issuerBankId, BigInteger cardNumber)
      throws DatabaseException {
        try {
            return repo.findByStartBinBetweenEndBin(issuerBankId, cardNumber).stream()
              .map(BinRangeDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByStartBinBetweenEndBin] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public boolean existsByIssuerBankIdAndCardNumberInBinRange(Long issuerBankId, String cardNumber)
      throws DatabaseException {
        try {
            BigInteger cardNumberInt = new BigInteger(cardNumber);
            return repo.existsByIssuerBankIdAndCardNumberInBinRange(issuerBankId, cardNumberInt);
        } catch (Exception e) {
            log.error(
              "[existsByIssuerBankIdAndCardNumberInBinRange] unknown exception, issuerBankId={}",
              issuerBankId, e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public String findCardBrandByIssuerBankIdAndCardNumberWithoutPadding(Long issuerBankId,
      String cardNumberWithoutPadding) {
        BigInteger cardNumberWithPadding = new BigInteger(cardNumberWithoutPadding);
        return findCardBrandByIssuerBankIdAndCardNumber(issuerBankId, cardNumberWithPadding);
    }

    @Override
    public String findCardBrandByIssuerBankIdAndCardNumber(Long issuerBankId,
      BigInteger cardNumberWithPadding) {
        List<BinRange> binRangeList;
        try {
            binRangeList = repo.findByStartBinBetweenEndBin(issuerBankId, cardNumberWithPadding);
        } catch (Exception e) {
            log.error("[findCardBrandByIssuerBankIdAndCardNumber] database error.", e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, e.getMessage());
        }

        if (CollectionUtils.isEmpty(binRangeList)) {
            return KernelEnvironmentConstant.UNKNOWN_CARD_BRAND;
        }

        return binRangeList.get(0).getCardBrand();
    }

    @Override
    public List<Long> findIssuerBankIdByCardNumber(BigInteger cardNumber) throws DatabaseException {
        try {
            return repo.findByCardNumberBetweenStartBinAndEndBin(cardNumber);
        } catch (Exception e) {
            log.error("[findIssuerBankIdByCardNumber] unknown exception", e);
            throw new DatabaseException(ResultStatus.DB_READ_ERROR, e.getMessage());
        }
    }

    @Override
    public Optional<BinRangeDO> existingBinRangeConflict(BinRangeDO binRangeDO) {
        return repo.existingBinRangeConflict(binRangeDO.getStartBin(), binRangeDO.getEndBin())
          .stream().filter(bin -> !bin.getId().equals(binRangeDO.getId())).findFirst()
          .map(BinRangeDO::valueOf);
    }

    @Override
    public BinRangeDO add(BinRangeDO binRangeDO) {
        if (binRangeDO == null) {
            throw new IllegalArgumentException(
              "Failed in add bin range due to missing binRange content.");
        }
        return BinRangeDO.valueOf(repo.save(BinRange.valueOf(binRangeDO)));
    }

    @Override
    public BinRangeDO update(BinRangeDO binRangeDO) {
        if (binRangeDO == null || binRangeDO.getId() <= 0) {
            throw new IllegalArgumentException(
              "Failed in add bin range due to missing binRange content.");
        }

        Optional<BinRange> tgtContent = repo.findById(binRangeDO.getId());
        if (!tgtContent.isPresent()) {
            log.error("[update] Failed in update bin range due to unknown bin content with id={}",
              binRangeDO.getId());
            throw new OceanExceptionForPortal("Command failed in missing target content.");
        }
        binRangeDO.setCreator(tgtContent.get().getCreator());
        binRangeDO.setCreateMillis(tgtContent.get().getCreateMillis());

        BinRange binRange = BinRange.valueOf(binRangeDO);
        return BinRangeDO.valueOf(repo.save(binRange));
    }

    @Override
    public boolean delete(long id, String deleter) {
        Optional<BinRange> tgtContent = repo.findById(id);
        if (!tgtContent.isPresent()) {
            return false;
        }

        BinRange tgtBin = tgtContent.get();
        tgtBin.setDeleteMillis(System.currentTimeMillis());
        tgtBin.setDeleter(deleter);
        tgtBin.setDeleteFlag(true);
        repo.save(tgtBin);

        return true;
    }

    @Override
    public Optional<BinRangeDO> findById(Long binRangeId, Long issuerBankId) {
        return repo.findByIdAndIssuerBankIdAndDeleteFlagIsFalse(binRangeId, issuerBankId)
          .map(BinRangeDO::valueOf);
    }

    @Override
    public List<BinRangeDO> listByIssuerBankId(Long issuerBankId) {
        return repo.findByIssuerBankIdAndDeleteFlagFalse(issuerBankId).stream()
          .map(BinRangeDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public Boolean existsByIssuerBankId(Long issuerBankId) {
        return repo.existsByIssuerBankIdAndDeleteFlagFalse(issuerBankId);
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(BinRange.class, issuerBankId, deleter, deleteMillis);
    }

}
