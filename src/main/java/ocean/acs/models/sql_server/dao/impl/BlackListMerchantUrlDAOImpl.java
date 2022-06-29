package ocean.acs.models.sql_server.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.constant.PortalMessageConstants;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.BlackListMerchantUrlDAO;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;
import ocean.acs.models.data_object.kernel.KernelBlackListMerchantUrlDO;
import ocean.acs.models.data_object.portal.BlackListMerchantUrlQueryDO;
import ocean.acs.models.data_object.portal.DeleteDataDO;
import ocean.acs.models.data_object.portal.IdsQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.data_object.portal.PortalBlackListMerchantUrlDO;
import ocean.acs.models.sql_server.entity.BlackListMerchantUrl;

@Log4j2
@Repository
@AllArgsConstructor
public class BlackListMerchantUrlDAOImpl implements BlackListMerchantUrlDAO {

    private final ocean.acs.models.sql_server.repository.BlackListMerchantUrlRepository repo;

    @Override
    public List<KernelBlackListMerchantUrlDO> findByIssuerBankID(Long issuerBankID)
            throws DatabaseException {
        try {
            List<KernelBlackListMerchantUrlDO> list = repo.findAllByIssuerBankID(issuerBankID);
            log.debug("[findByIssuerBankID] issuerBankID={}, url list={}", issuerBankID,
                    list.stream().map(p -> p.getUrl()).collect(Collectors.toList()));

            return list;
        } catch (Exception e) {
            log.error("[findByIssuerBankID] unknown exception, issuerBankID={}", issuerBankID, e);
            throw new DatabaseException(ResultStatus.DATABASE_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(DeleteDataDO deleteDto) {
        repo.deleteById(deleteDto.getId(), deleteDto.getUser(), System.currentTimeMillis());
    }

    @Override
    public PagingResultDO<BlackListMerchantUrlDO> findByUrlLike(
            BlackListMerchantUrlQueryDO queryDO) {
        try {
            PageRequest page = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                    Sort.Direction.DESC, "createMillis");

            Page<BlackListMerchantUrl> merchantUrlPage =
                    repo.findAll((Specification<BlackListMerchantUrl>) (merchantUrlRoot,
                            criteriaQuery, criteriaBuilder) -> {
                        List<Predicate> predList = new ArrayList<>();
                        Predicate pred;

                        pred = criteriaBuilder.equal(merchantUrlRoot.get("issuerBankId"),
                                queryDO.getIssuerBankId());
                        predList.add(pred);

                        Long startTime = queryDO.getStartTime();
                        Long endTime = queryDO.getEndTime();
                        if (startTime != null && endTime == null) {
                            pred = criteriaBuilder.ge(merchantUrlRoot.get("createMillis"),
                                    startTime);
                            predList.add(pred);
                        } else if (startTime == null && endTime != null) {
                            pred = criteriaBuilder.le(merchantUrlRoot.get("createMillis"), endTime);
                            predList.add(pred);
                        } else if (startTime != null && endTime != null) {
                            pred = criteriaBuilder.between(merchantUrlRoot.get("createMillis"),
                                    startTime, endTime);
                            predList.add(pred);
                        }

                        String merchantUrl = queryDO.getMerchantUrl();
                        if (StringUtils.isNotBlank(merchantUrl)) {
                            String urlLikeQuery = "%" + merchantUrl + "%";
                            pred = criteriaBuilder.like(merchantUrlRoot.get("url"), urlLikeQuery);
                            predList.add(pred);
                        }

                        pred = criteriaBuilder.equal(merchantUrlRoot.get("deleteFlag"), 0);
                        predList.add(pred);

                        Predicate[] predicates = new Predicate[predList.size()];
                        return criteriaBuilder.and(predList.toArray(predicates));
                    }, page);

            Page<BlackListMerchantUrlDO> merchantUrlDoPage =
                    merchantUrlPage.map(BlackListMerchantUrlDO::valueOf);
            return PagingResultDO.valueOf(merchantUrlDoPage);

        } catch (Exception e) {
            String errMsg = PortalMessageConstants.DB_READ_ERROR;
            errMsg = String.format("Query merchantUrl=%s, queryDto=%s", errMsg, queryDO);
            log.warn("[findByUrlLike] unknown exception, {}", errMsg, e);
            throw new OceanExceptionForPortal(ResultStatus.DB_READ_ERROR, errMsg);
        }
    }

    @Override
    public List<BlackListMerchantUrlDO> findByIds(IdsQueryDO queryDO, Pageable pageable) {
        try {
            if (queryDO == null || queryDO.getIds() == null || queryDO.getIds().isEmpty()) {
                return Collections.emptyList();
            }

            List<BlackListMerchantUrl> blackMerchantUrls =
                    repo.findByIdIn(queryDO.getIds(), pageable);
            return blackMerchantUrls.stream().map(BlackListMerchantUrlDO::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findByIds] unknown exception, queryDto={}", queryDO, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<BlackListMerchantUrlDO> findById(Long blackListMerchantUrlId) {
        try {
            Optional<BlackListMerchantUrl> blackMerchantUrlOpt =
                    repo.findById(blackListMerchantUrlId);
            return blackMerchantUrlOpt.map(BlackListMerchantUrlDO::valueOf);
        } catch (Exception e) {
            log.error("[findById] unknown exception, blackListMerchantUrlId={}",
                    blackListMerchantUrlId, e);
            return Optional.empty();
        }
    }

    @Override
    public BlackListMerchantUrlDO create(PortalBlackListMerchantUrlDO createDto,
            TransStatus transStatus) {
        BlackListMerchantUrl blackListMerchantUrl =
                BlackListMerchantUrl.valueOf(createDto, transStatus);
        return BlackListMerchantUrlDO.valueOf(repo.save(blackListMerchantUrl));
    }

    @Override
    public BlackListMerchantUrlDO update(BlackListMerchantUrlDO updateDO) {
        BlackListMerchantUrl blackListMerchantUrl = BlackListMerchantUrl.valueOf(updateDO);
        return BlackListMerchantUrlDO.valueOf(repo.save(blackListMerchantUrl));
    }

    @Override
    public boolean isUrlExist(Long issuerBankId, String url) {
        return repo.isUrlExist(issuerBankId, url.trim());
    }

    @Override
    public void updateTransStatusByIssuerBankId(Long issuerBankId, TransStatus transStatus,
            String updater) {
        repo.updateTransStatusByIssuerBankId(issuerBankId, transStatus.getCode(), updater,
                System.currentTimeMillis());
    }

    @Override
    public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
        //TODO Implement method
        return 0;
    }

}
