package ocean.acs.commons.enumerator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public enum BackupTableEnum {

    // Portal
    KERNEL_TRANSACTION_LOG("KERNEL_TRANSACTION_LOG"),
    THREE_D_S_METHOD_LOG("THREE_D_S_METHOD_LOG"),
    AUTHENTICATION_LOG("AUTHENTICATION_LOG"),
    SDK_UI_TYPE_LOG("SDK_UI_TYPE_LOG"),
    AUTHENTICATION_ME_LOG("AUTHENTICATION_ME_LOG"),
    RESULT_LOG("RESULT_LOG"),
    RESULT_ME_LOG("RESULT_ME_LOG"),
    ERROR_MESSAGE_LOG("ERROR_MESSAGE_LOG"),
    CHALLENGE_LOG("CHALLENGE_LOG"),
    CHALLENGE_SELECT_INFO_LOG("CHALLENGE_SELECT_INFO_LOG"),
    CHALLENGE_ME_LOG("CHALLENGE_ME_LOG"),
    CHALLENGE_CODE_LOG("CHALLENGE_CODE_LOG"),

    AUDIT_LOG("AUDIT_LOG"),
    AUDITING("AUDITING"),

    // Integrator
    INTEGRATOR_TRANSACTION_LOG("INTEGRATOR_TRANSACTION_LOG"),
    OTP_GENERATED_LOG("OTP_GENERATED_LOG"),
    CAVV_LOG("CAVV_LOG");


    @Getter
    private String tableName;

    public final static Set<String> BACKUP_TABLE_SET = new HashSet<>();

    static {
        Arrays.stream(BackupTableEnum.values())
                .forEach(e -> BACKUP_TABLE_SET.add(e.getTableName()));
    }

    BackupTableEnum(String tableName) {
        this.tableName = tableName;
    }

}
