package ocean.acs.models.oracle.dao.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanExceptionForPortal;
import ocean.acs.models.dao.ErrorCodeDAO;
import ocean.acs.models.data_object.entity.ErrorCodeMappingDO;
import ocean.acs.models.data_object.entity.ErrorIssueGroupDO;
import ocean.acs.models.oracle.entity.ErrorCodeMapping;
import ocean.acs.models.oracle.entity.ErrorIssueGroup;

@Log4j2
@Repository
@AllArgsConstructor
public class ErrorCodeDAOImpl implements ErrorCodeDAO {

    private final ocean.acs.models.oracle.repository.ErrorCodeMappingRepository repo;
    private final ocean.acs.models.oracle.repository.ErrorIssueGroupRepository errorIssueGroupRepo;

    @Override
    public List<ErrorCodeMappingDO> getErrorCodeMappingByErrorGroupId(Long id) {
        return repo.findByErrorGroupId(id).stream().map(ErrorCodeMappingDO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<ErrorIssueGroupDO> getGroupList() {
        return errorIssueGroupRepo.findByDeleteFlagIsFalse().stream()
                .map(ErrorIssueGroupDO::valueOf).collect(Collectors.toList());
    }

    @Override
    public Optional<ErrorIssueGroupDO> getByGroupId(Long id) {
        return errorIssueGroupRepo.findByIdAndDeleteFlagIsFalse(id).map(ErrorIssueGroupDO::valueOf);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErrorIssueGroupDO updateGroupMessage(ErrorIssueGroupDO updateIssueGroupDO) {
        Optional<ErrorIssueGroup> oriIssueGroupOpt =
                errorIssueGroupRepo.findByIdAndDeleteFlagIsFalse(updateIssueGroupDO.getId());
        if (!oriIssueGroupOpt.isPresent()) {
            log.error(
                    "[updateGroupMessage] unknown exception, failed in update error group message. Missing target group. id={}",
                    updateIssueGroupDO.getId());
            throw new OceanExceptionForPortal(ResultStatus.NO_SUCH_DATA);
        }

        ErrorIssueGroup oriIssueGroup = oriIssueGroupOpt.get();
        updateCodeList(oriIssueGroup.getCodeList(), updateIssueGroupDO.getCodeList());
        oriIssueGroup.setAuditStatus(updateIssueGroupDO.getAuditStatus());
        oriIssueGroup.setUpdateMillis(System.currentTimeMillis());
        oriIssueGroup.setUpdater(updateIssueGroupDO.getUpdater());

        return ErrorIssueGroupDO.valueOf(errorIssueGroupRepo.save(oriIssueGroup));
    }

    private void updateCodeList(List<ErrorCodeMapping> oriCodeList, List<ErrorCodeMappingDO> updateCodeList) {
        for (ErrorCodeMappingDO updateCode : updateCodeList) {
            for (ErrorCodeMapping oriCode : oriCodeList) {
                if (!updateCode.getCode().equalsIgnoreCase(oriCode.getCode()))
                    continue;

                oriCode.setMessage(updateCode.getMessage());
                oriCode.setUpdateMillis(System.currentTimeMillis());
                break;
            }
        }
    }

}
