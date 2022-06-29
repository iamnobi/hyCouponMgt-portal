package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.dao.AuditingDAO;
import ocean.acs.models.data_object.entity.AuditingDO;
import ocean.acs.models.oracle.entity.Auditing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class AuditingDAOImpl implements AuditingDAO {

  private final ocean.acs.models.oracle.repository.AuditingRepository repo;
  private final DeleteIssuerBankRelativeDataHelper deleteIssuerBankRelativeDataHelper;

  @Override
  public Optional<Page<AuditingDO>> findByFunctionTypeAndAuditStatusExcludeSelf(Long issuerBankId,
    AuditFunctionType functionType, AuditStatus auditStatus, String applicant,
    Pageable paging) {
    Page<Auditing> resultPage = repo
      .findByIssuerBankIdAndAuditStatusAndCreatorIsNotAndFunctionTypeAndDeleteFlagFalseOrderByCreateMillisDesc(
        issuerBankId, auditStatus.getSymbol(), applicant,
        functionType.getTypeSymbol(), paging);

    Page<AuditingDO> doPage = new PageImpl<>(
      resultPage.stream().map(AuditingDO::valueOf).collect(Collectors.toList()), paging, resultPage.getTotalElements());
    return Optional.of(doPage);
  }

  @Override
  public Optional<Page<AuditingDO>> findAllAuditStatusExcludeSelf(Long issuerBankId,
    AuditStatus auditStatus, String applicant, Pageable paging) {
    Page<Auditing> resultPage = repo
      .findByIssuerBankIdAndDeleteFlagFalseAndAuditStatusAndCreatorIsNotOrderByCreateMillisDesc(
        issuerBankId, auditStatus.getSymbol(), applicant, paging);
    List<AuditingDO> doList = resultPage.stream().map(AuditingDO::valueOf)
      .collect(Collectors.toList());
    Page<AuditingDO> doPage = new PageImpl<>(doList, paging, resultPage.getTotalElements());
    return Optional.of(doPage);
  }

  @Override
  public Optional<Page<AuditingDO>> findByFunctionTypeListAndAuditStatusExcludeSelf(
    Long issuerBankId, List<AuditFunctionType> functionTypeList, AuditStatus auditStatus,
    String applicant, Pageable paging) {
    List<String> functionTypeStringList = functionTypeList.stream()
      .map(AuditFunctionType::getTypeSymbol).collect(Collectors.toList());
    Page<Auditing> resultPage = repo
      .findByIssuerBankIdAndAuditStatusAndCreatorIsNotAndFunctionTypeInAndDeleteFlagIsFalseOrderByCreateMillisDesc(
        issuerBankId, auditStatus.getSymbol(), applicant, functionTypeStringList,
        paging);
    Page<AuditingDO> doPage = new PageImpl<>(
      resultPage.stream().map(AuditingDO::valueOf).collect(Collectors.toList()), paging, resultPage.getTotalElements());
    return Optional.of(doPage);
  }

  @Override
  public Optional<AuditingDO> save(AuditingDO auditingDO) {
    Auditing auditing = Auditing.valueOf(auditingDO);
    return Optional.of(AuditingDO.valueOf(repo.save(auditing)));
  }

  @Override
  public Optional<AuditingDO> findById(Long id) {
    Optional<Auditing> auditingOptional = repo.findById(id);
    return auditingOptional.map(AuditingDO::valueOf);
  }

  @Override
  public Optional<Page<AuditingDO>> getLogByUser(String user, AuditStatus auditStatus,
    Long startTime, Long endTime, Pageable paging) {
    Page<Auditing> resultPage = repo
      .findByCreatorAndAuditStatusAndDeleteFlagFalseAndCreateMillisBetweenOrderByCreateMillisDesc(
        user, auditStatus.getSymbol(), startTime, endTime, paging);
    Page<AuditingDO> doPage = new PageImpl<>(
      resultPage.stream().map(AuditingDO::valueOf).collect(Collectors.toList()), paging, resultPage.getTotalElements());

    return Optional.of(doPage);
  }

  @Override
  public List<AuditingDO> findDraftForNewWhitelistSetting(Long issuerBankId, Long panId) {
    String queryByDraftData =
      String.format("{\"issuerBankId\":%d,\"panId\":%d,%%", issuerBankId, panId);
    return repo.findNewWhitelistSettingByDraftDataLike(queryByDraftData)
      .stream().map(AuditingDO::valueOf).collect(Collectors.toList());
  }

  @Override
  public int deleteByIssuerBankId(long issuerBankId, String deleter, long deleteMillis) {
    return deleteIssuerBankRelativeDataHelper.deleteByIssuerBankId(Auditing.class, issuerBankId, deleter, deleteMillis);
  }

}
