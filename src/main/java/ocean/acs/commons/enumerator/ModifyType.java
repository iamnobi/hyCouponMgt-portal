package ocean.acs.commons.enumerator;

import lombok.Getter;

public enum ModifyType {

    AUDITING("auditing"), PRIME("prime");

    @Getter
    private String description;

    ModifyType(String description) {
        this.description = description;
    }
}
