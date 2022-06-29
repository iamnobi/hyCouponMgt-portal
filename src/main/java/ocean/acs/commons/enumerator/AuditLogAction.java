package ocean.acs.commons.enumerator;

/**
 * AuditLog.action type
 */
public enum AuditLogAction {
    
    Y("success"), N("fail"), T("timeout");

    private String desc;

    AuditLogAction(String desc) {
        this.desc = desc;
    }

    public static AuditLogAction getByBoolean(boolean value) {
        return value ? Y : N;
    }

    public String getDesc() {
        return desc;
    }
}
