package ocean.acs.commons.enumerator;

import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum CardStatus {
    NORMAL("N"),
    LOCK("L"),
    UNKNOWN("U");

    private final String code;

    CardStatus(String code) {
        this.code = code;
    }

    public static CardStatus getByCode(String code) {
        return Stream.of(CardStatus.values())
                .filter(cardStatus -> cardStatus.getCode().equals(code))
                .findFirst()
                .orElse(CardStatus.UNKNOWN);
    }
}
