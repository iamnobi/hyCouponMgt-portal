package com.cherri.acs_portal.controller.response;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.cherri.acs_portal.dto.usermanagement.UserGroupDto;
import com.cherri.acs_portal.service.PermissionService;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.annotation.Role;
import ocean.acs.commons.enumerator.Permission;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.data_object.entity.UserGroupDO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 如果有修改權限相關請記得同步更改以下Class及{@Table user_group}
 * 1. {@link ocean.acs.models.oracle.entity.UserGroup} Field
 * 2. {@link ocean.acs.models.oracle.entity.UserGroup} All args constructor
 * 3. {@link ocean.acs.models.oracle.entity.UserGroup#valueOf(UserGroupDO)}
 * 4. {@link ocean.acs.models.sql_server.entity.UserGroup} Field
 * 5. {@link ocean.acs.models.sql_server.entity.UserGroup} All args constructor
 * 6. {@link ocean.acs.models.sql_server.entity.UserGroup#valueOf(UserGroupDO)
 * 7. {@link ocean.acs.models.entity.DBKey}}
 * 8. {@link UserGroupDO}
 * 9. {@link UserGroupDO#valueOf(ocean.acs.models.oracle.entity.UserGroup)}
 * 10. {@link UserGroupDO#valueOf(ocean.acs.models.sql_server.entity.UserGroup)}
 * 11. {@link UserGroupDto}
 * 12. {@link UserGroupDto#valueOf(UserGroupDO)}
 * 13. {@link Permission}
 * 14. {@link PermissionDto}
 * 15. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto}
 * 16. {@link com.cherri.acs_portal.controller.request.UpdateGroupPermissionReqDto#checkUpdatingPermissionRule}
 * 17. {@link PermissionService#updateByUserGroupDto(UserGroupDO, UserGroupDto)}
 * 18. {@link PermissionService#createBankGroup(UserGroupDto)}
 */
@Log4j2
@Data
@NoArgsConstructor
public class PermissionDto {

    private Boolean moduleReport = Boolean.FALSE;
    private Boolean reportQuery = Boolean.FALSE;
    private Boolean moduleTx = Boolean.FALSE;
    private Boolean moduleHealthCheck = Boolean.FALSE;
    private Boolean healthCheckQuery = Boolean.FALSE;
    private Boolean txQuery = Boolean.FALSE;
    private Boolean moduleCard = Boolean.FALSE;
    private Boolean cardQuery = Boolean.FALSE;
    private Boolean cardModify = Boolean.FALSE;
    private Boolean cardAudit = Boolean.FALSE;
    private Boolean moduleRiskBlackList = Boolean.FALSE;
    private Boolean blackListQuery = Boolean.FALSE;
    private Boolean blackListModify = Boolean.FALSE;
    private Boolean blackListAudit = Boolean.FALSE;
    private Boolean moduleRiskWhiteList = Boolean.FALSE;
    private Boolean whiteListQuery = Boolean.FALSE;
    private Boolean whiteListModify = Boolean.FALSE;
    private Boolean whiteListAudit = Boolean.FALSE;
    private Boolean moduleRiskControl = Boolean.FALSE;
    private Boolean riskControlQuery = Boolean.FALSE;
    private Boolean riskControlModify = Boolean.FALSE;
    private Boolean riskControlAudit = Boolean.FALSE;
    private Boolean moduleUserGroup = Boolean.FALSE;
    private Boolean userGroupQuery = Boolean.FALSE;
    private Boolean userGroupModify = Boolean.FALSE;
    private Boolean userGroupAudit = Boolean.FALSE;
    private Boolean moduleUserUnlock = Boolean.FALSE;
    private Boolean unlockQuery = Boolean.FALSE;
    private Boolean unlockModify = Boolean.FALSE;
    private Boolean unlockAudit = Boolean.FALSE;
    private Boolean moduleUserAuditLog = Boolean.FALSE;
    private Boolean auditLogQuery = Boolean.FALSE;
    private Boolean moduleBankManage = Boolean.FALSE;
    private Boolean bankManageQuery = Boolean.FALSE;
    private Boolean bankManageModify = Boolean.FALSE;
    private Boolean bankManageAudit = Boolean.FALSE;
    private Boolean moduleBankLogo = Boolean.FALSE;
    private Boolean bankLogoQuery = Boolean.FALSE;
    private Boolean bankLogoModify = Boolean.FALSE;
    private Boolean bankLogoAudit = Boolean.FALSE;
    private Boolean moduleBankChannel = Boolean.FALSE;
    private Boolean bankChannelQuery = Boolean.FALSE;
    private Boolean bankChannelModify = Boolean.FALSE;
    private Boolean bankChannelAudit = Boolean.FALSE;
    private Boolean moduleBankFee = Boolean.FALSE;
    private Boolean bankFeeQuery = Boolean.FALSE;
    private Boolean bankFeeModify = Boolean.FALSE;
    private Boolean bankFeeAudit = Boolean.FALSE;
    private Boolean moduleBankOtpSending = Boolean.FALSE;
    private Boolean bankOtpSendingQuery = Boolean.FALSE;
    private Boolean bankOtpSendingModify = Boolean.FALSE;
    private Boolean bankOtpSendingAudit = Boolean.FALSE;
    private Boolean moduleSysBinRange = Boolean.FALSE;
    private Boolean sysBinRangeQuery = Boolean.FALSE;
    private Boolean sysBinRangeModify = Boolean.FALSE;
    private Boolean sysBinRangeAudit = Boolean.FALSE;
    private Boolean moduleGeneralSetting = Boolean.FALSE;
    private Boolean generalSettingQuery = Boolean.FALSE;
    private Boolean generalSettingModify = Boolean.FALSE;
    private Boolean moduleSysCardLogo = Boolean.FALSE;
    private Boolean sysCardLogoQuery = Boolean.FALSE;
    private Boolean sysCardLogoModify = Boolean.FALSE;
    private Boolean sysCardLogoAudit = Boolean.FALSE;
    private Boolean moduleSysChallengeView = Boolean.FALSE;
    private Boolean sysChallengeViewQuery = Boolean.FALSE;
    private Boolean sysChallengeViewModify = Boolean.FALSE;
    private Boolean sysChallengeViewAudit = Boolean.FALSE;
    private Boolean moduleSysChallengeSmsMsg = Boolean.FALSE;
    private Boolean sysChallengeSmsMsgQuery = Boolean.FALSE;
    private Boolean sysChallengeSmsMsgModify = Boolean.FALSE;
    private Boolean sysChallengeSmsMsgAudit = Boolean.FALSE;
    private Boolean moduleSysAcsOperatorId = Boolean.FALSE;
    private Boolean sysAcsOperatorIdQuery = Boolean.FALSE;
    private Boolean sysAcsOperatorIdModify = Boolean.FALSE;
    private Boolean sysAcsOperatorIdAudit = Boolean.FALSE;
    private Boolean moduleSysTimeout = Boolean.FALSE;
    private Boolean sysTimeoutQuery = Boolean.FALSE;
    private Boolean sysTimeoutModify = Boolean.FALSE;
    private Boolean sysTimeoutAudit = Boolean.FALSE;
    private Boolean moduleSysErrorCode = Boolean.FALSE;
    private Boolean sysErrorCodeQuery = Boolean.FALSE;
    private Boolean sysErrorCodeModify = Boolean.FALSE;
    private Boolean sysErrorCodeAudit = Boolean.FALSE;
    private Boolean moduleSysKey = Boolean.FALSE;
    private Boolean sysKeyQuery = Boolean.FALSE;
    private Boolean sysKeyModify = Boolean.FALSE;
    private Boolean sysKeyAudit = Boolean.FALSE;
    private Boolean moduleCert = Boolean.FALSE;
    private Boolean certQuery = Boolean.FALSE;
    private Boolean certModify = Boolean.FALSE;
    private Boolean certAudit = Boolean.FALSE;
    private Boolean moduleClassicRba = Boolean.FALSE;
    private Boolean classicRbaQuery = Boolean.FALSE;
    private Boolean classicRbaModify = Boolean.FALSE;
    private Boolean modulePluginIssuerProperty = Boolean.FALSE;
    private Boolean pluginIssuerPropertyQuery = Boolean.FALSE;
    private Boolean pluginIssuerPropertyModify = Boolean.FALSE;
    private Boolean classicRbaAudit = Boolean.FALSE;
    private Boolean moduleBankDataKey = Boolean.FALSE;
    private Boolean bankDataKeyQuery = Boolean.FALSE;
    private Boolean bankDataKeyModify = Boolean.FALSE;
    private Boolean moduleVelog = Boolean.FALSE;
    private Boolean velogQuery = Boolean.FALSE;

    private Boolean accessMultiBank = Boolean.FALSE;

    private Boolean mimaPolicyQuery = Boolean.FALSE;
    private Boolean mimaPolicyModify = Boolean.FALSE;
    private Boolean moduleMimaPolicy = Boolean.FALSE;

    private Boolean acquirerBankQuery = Boolean.FALSE;
    private Boolean acquirerBankModify = Boolean.FALSE;
    private Boolean moduleAcquirerBank = Boolean.FALSE;

    private Boolean moduleCanSeePan = Boolean.FALSE;
    private Boolean canSeePanQuery = Boolean.FALSE;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
    }

    public static PermissionDto valueOf(UserGroupDO userGroup)
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        PermissionDto permissionDto = new PermissionDto();
        Class<? extends PermissionDto> permissionDtoClazz = permissionDto.getClass();

        Class<? extends UserGroupDO> userGroupClazz = userGroup.getClass();
        Field[] fields = userGroupClazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Role.class)) {
                String userGroupFieldName = field.getName();
                String userGroupFieldGetter = String
                  .format("get%S%s", userGroupFieldName.substring(0, 1),
                    userGroupFieldName.substring(1));
                Method userGroupMethod = userGroupClazz.getMethod(userGroupFieldGetter);
                Boolean value = (Boolean) userGroupMethod.invoke(userGroup);

                Role role = field.getAnnotation(Role.class);
                String permissionDtoFieldName = role.value().getPermissionDtoField();
                String permissionDtoFieldSetter = String
                  .format("set%S%s", permissionDtoFieldName.substring(0, 1),
                    permissionDtoFieldName.substring(1));
                Method permissionDtoMethod;
                try {
                    permissionDtoMethod = permissionDtoClazz
                      .getMethod(permissionDtoFieldSetter, Boolean.class);
                } catch (NoSuchMethodException e) {
                    // 被這段錯誤訊息拯救的請 +1
                    log.error("[valueOf] 請在 PermissionDto 增加 UserGroup 中新增的權限欄位={}",
                      field.getName());
                    throw new OceanException(ResultStatus.SERVER_ERROR);
                }
                permissionDtoMethod.invoke(permissionDto, value);
            }
        }

        return permissionDto;
    }

}
