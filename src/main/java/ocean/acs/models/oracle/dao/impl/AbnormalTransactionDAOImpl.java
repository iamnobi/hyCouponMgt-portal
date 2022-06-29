package ocean.acs.models.oracle.dao.impl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.utils.StringCustomizedUtils;
import ocean.acs.models.dao.AbnormalTransactionDAO;
import ocean.acs.models.data_object.entity.AbnormalTransactionDO;
import ocean.acs.models.data_object.portal.AbnormalTransactionQueryDO;
import ocean.acs.models.oracle.entity.AbnormalTransaction;

@Log4j2
@Repository
@AllArgsConstructor
public class AbnormalTransactionDAOImpl implements AbnormalTransactionDAO {

    private final ocean.acs.models.oracle.repository.AbnormalTransactionRepository repo;

    @Override
    public List<AbnormalTransactionDO> saveAll(
            List<AbnormalTransactionDO> abnormalTransactionDoList) {
        try {
            List<AbnormalTransaction> abnormalTransList = abnormalTransactionDoList.stream()
                    .map(AbnormalTransaction::valueOf).collect(Collectors.toList());
            Iterable<AbnormalTransaction> abnormalTransIter = repo.saveAll(abnormalTransList);
            return StreamSupport.stream(abnormalTransIter.spliterator(), false)
                    .map(AbnormalTransactionDO::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[saveAll] unknown exception, request params={}", abnormalTransactionDoList,
                    e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<AbnormalTransactionDO> findByYearAndMonthAndDayAndIssuerBankIdAndMerchantId(int year,
            int month, int day, Long issuerBankId, String merchantId) {
        try {
            Optional<AbnormalTransaction> abnormalTransOpt =
                    repo.findByYearAndMonthAndDayOfMonthAndIssuerBankIdAndMerchantId(year, month, day, issuerBankId,
                            merchantId);
            return abnormalTransOpt.map(AbnormalTransactionDO::valueOf);
        } catch (Exception e) {
            log.error(
                    "[findByYearAndMonthAndDayAndIssuerBankIdAndMerchantId] unknown exception, year={}, month={}, day={}, merchantID={}",
                    year, month, day, merchantId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Page<AbnormalTransactionDO>> query(AbnormalTransactionQueryDO queryDO) {

        Pageable pageRequest = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                Direction.DESC, "nRate");
        Specification<AbnormalTransaction> specification =
                new QueryAbnormalTransactionSpecification(queryDO);
        try {
            Page<AbnormalTransaction> abnormalPage = repo.findAll(specification, pageRequest);
            return Optional.of(abnormalPage.map(AbnormalTransactionDO::valueOf));
        } catch (Exception e) {
            log.error("[query] unknown exception, request params={}", queryDO, e);
            return Optional.empty();
        }
    }

    /** 查詢商店異常交易統計查詢條件 */
    @AllArgsConstructor
    private static class QueryAbnormalTransactionSpecification
            implements Specification<AbnormalTransaction> {

        private AbnormalTransactionQueryDO queryDO;

        @Override
        public Predicate toPredicate(Root<AbnormalTransaction> abnormalRoot, CriteriaQuery<?> query,
                CriteriaBuilder criteriaBuilder) {
            List<Predicate> predList = new ArrayList<>();
            predList.add(criteriaBuilder.equal(abnormalRoot.get("year"), queryDO.getYear()));
            predList.add(criteriaBuilder.equal(abnormalRoot.get("month"), queryDO.getMonth()));
            predList.add(criteriaBuilder.equal(abnormalRoot.get("dayOfMonth"), 0));

            if (queryDO.getIssuerBankId() != null) {
                predList.add(criteriaBuilder.equal(abnormalRoot.get("issuerBankId"),
                        queryDO.getIssuerBankId()));
            }

            if (StringCustomizedUtils.isNotEmpty(queryDO.getMerchantId())) {
                predList.add(criteriaBuilder.equal(abnormalRoot.get("merchantId"),
                        queryDO.getMerchantId()));
            }
            if (StringCustomizedUtils.isNotEmpty(queryDO.getMerchantName())) {
                predList.add(criteriaBuilder.like(abnormalRoot.get("merchantName"),
                        "%" + queryDO.getMerchantName() + "%"));
            }
            Predicate[] predicates = new Predicate[predList.size()];
            return criteriaBuilder.and(predList.toArray(predicates));
        }
    }
}
