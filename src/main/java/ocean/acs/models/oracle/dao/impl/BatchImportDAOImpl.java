package ocean.acs.models.oracle.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import lombok.AllArgsConstructor;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.utils.StringCustomizedUtils;
import ocean.acs.models.dao.BatchImportDAO;
import ocean.acs.models.data_object.entity.BatchImportDO;
import ocean.acs.models.data_object.portal.BatchQueryDO;
import ocean.acs.models.data_object.portal.PagingResultDO;
import ocean.acs.models.oracle.entity.BatchImport;

@Repository
@AllArgsConstructor
public class BatchImportDAOImpl implements BatchImportDAO {

    private final ocean.acs.models.oracle.repository.BatchRepository repo;
  private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

    @Override
    public Page<BatchImportDO> query(int pageIndex, int pageSize) {
        PageRequest page = PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "createMillis");
        Page<BatchImport> batchPage = repo.findAll(page);
        return batchPage.map(BatchImportDO::valueOf);
    }

    @Override
    public BatchImportDO add(BatchImportDO batchImportDO) {
        BatchImport batchImport = BatchImport.valueOf(batchImportDO);
        return BatchImportDO.valueOf(repo.save(batchImport));
    }

    @Override
    public PagingResultDO<BatchImportDO> queryPanBlackListBatch(BatchQueryDO queryDO) {
        PageRequest pagination = PageRequest.of(queryDO.getPage() - 1, queryDO.getPageSize(),
                Sort.Direction.DESC, "createMillis");

        Specification<BatchImport> spec =
                (Specification<BatchImport>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    // filter deleted
                    predicates.add(criteriaBuilder
                            .and(criteriaBuilder.equal(root.get("deleteFlag"), false)));

                    // filter 單筆新增
                    predicates
                            .add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("batchName"),
                                    PortalEnvironmentConstants.DEFAULT_BLACK_LIST_PAN_BATCH_NAME)));

                    if (StringCustomizedUtils.isNotEmpty(queryDO.getBatchName())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder
                                .like(root.get("batchName"), "%" + queryDO.getBatchName() + "%")));
                    }

                    if (queryDO.getTransStatus() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                root.get("transStatus"), queryDO.getTransStatus().getCode())));
                    }

                    if (queryDO.getStartTime() != null && queryDO.getStartTime() > 0) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder
                                .ge(root.get("createMillis"), queryDO.getStartTime())));
                    }

                    if (queryDO.getEndTime() != null && queryDO.getEndTime() > 0) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder
                                .le(root.get("createMillis"), queryDO.getEndTime())));
                    }

                    if (queryDO.getIssuerBankId() != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder
                                .equal(root.get("issuerBankId"), queryDO.getIssuerBankId())));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };

        Page<BatchImport> batchPagination = repo.findAll(spec, pagination);
        List<BatchImportDO> batchDoPagination = batchPagination.getContent().stream()
                .map(BatchImportDO::valueOf).collect(Collectors.toList());

        PagingResultDO<BatchImportDO> pagingResultDTO = new PagingResultDO<>(batchDoPagination);
        pagingResultDTO.setCurrentPage(batchPagination.getNumber() + 1);
        pagingResultDTO.setTotal(batchPagination.getTotalElements());
        pagingResultDTO.setTotalPages(batchPagination.getTotalPages());

        return pagingResultDTO;
    }

    @Override
    public Optional<BatchImportDO> getById(long id) {
        return repo.findByIdAndDeleteFlagIsFalse(id).map(BatchImportDO::valueOf);
    }

    @Override
    public BatchImportDO update(BatchImportDO batchImportDO) {
        BatchImport batchImport = BatchImport.valueOf(batchImportDO);
        return BatchImportDO.valueOf(repo.save(batchImport));
    }

  @Override
  public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
    return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(BatchImport.class, issuerBankId, deleter, deleteMillis);
  }

}
