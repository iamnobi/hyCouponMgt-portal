package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ocean.acs.commons.enumerator.AuditStatus;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ErrorIssueGroupDO extends OperatorInfoDO {

    private Long id;
    private String name;
    private String auditStatus;
    private List<ErrorCodeMappingDO> codeList;

    public ErrorIssueGroupDO(
            Long id,
            String name,
            String auditStatus,
            List<ErrorCodeMappingDO> codeList,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.name = name;
        this.auditStatus = auditStatus;
        this.codeList = codeList;
    }

    public static ErrorIssueGroupDO valueOf(ocean.acs.models.oracle.entity.ErrorIssueGroup e) {
        return new ErrorIssueGroupDO(
                e.getId(),
                e.getName(),
                e.getAuditStatus(),
                e.getCodeList().stream()
                        .map(ErrorCodeMappingDO::valueOf)
                        .collect(Collectors.toList()),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
    
    public static ErrorIssueGroupDO valueOf(ocean.acs.models.sql_server.entity.ErrorIssueGroup e) {
        return new ErrorIssueGroupDO(
                e.getId(),
                e.getName(),
                e.getAuditStatus(),
                e.getCodeList().stream()
                        .map(ErrorCodeMappingDO::valueOf)
                        .collect(Collectors.toList()),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static ErrorIssueGroupDO newInstance(
            Long id,
            AuditStatus auditStatus,
            String name,
            String updater,
            List<ErrorCodeMappingDO> codeMappingDOList) {

        ErrorIssueGroupDO issueGroup = ErrorIssueGroupDO.builder().build();
        issueGroup.setId(id);
        issueGroup.setAuditStatus(auditStatus.getSymbol());
        issueGroup.setName(name);
        issueGroup.setUpdater(updater);

        issueGroup.setCodeList(codeMappingDOList);
        return issueGroup;
    }
}
