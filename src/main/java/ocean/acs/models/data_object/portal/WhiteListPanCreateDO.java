package ocean.acs.models.data_object.portal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ocean.acs.commons.constant.PortalEnvironmentConstants;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.exception.HmacException;
import ocean.acs.commons.utils.HmacUtils;
import ocean.acs.models.oracle.entity.WhiteListPan;

/** 新增白名單Request參數物件 */
public class WhiteListPanCreateDO extends ManualPanCreateDO {

    @JsonIgnore
    @Override
    public AuditFunctionType getFunctionType() {
        return AuditFunctionType.WHITE_LIST;
    }

    public static WhiteListPanCreateDO newInstance(
            Long id, Long issuerBankId
            ,String realCardNumber
            ,byte[] fileContent,
            String fileName,
            String user,
            Integer version,
            AuditStatus auditStatus
    ) {
        WhiteListPanCreateDO whiteListPanCreateDO = new WhiteListPanCreateDO();
        whiteListPanCreateDO.setId(id);
        whiteListPanCreateDO.setIssuerBankId(issuerBankId);
        whiteListPanCreateDO.setRealCardNumber(realCardNumber);
        whiteListPanCreateDO.setFileContent(fileContent);
        whiteListPanCreateDO.setFileName(fileName);
        whiteListPanCreateDO.setUser(user);
        whiteListPanCreateDO.setVersion(version);
        whiteListPanCreateDO.setAuditStatus(auditStatus);

        return whiteListPanCreateDO;
    }

}
