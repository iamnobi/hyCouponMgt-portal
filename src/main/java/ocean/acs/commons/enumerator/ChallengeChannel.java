package ocean.acs.commons.enumerator;

import lombok.Getter;

/**
 * ChallengeChannel
 *
 * @author Alan Chen
 */
@Getter
public enum ChallengeChannel {
    /**
     * Challenge Channel
     */
    SMS(1, "SMS"),
    MAIL(2, "Mail"),
    SMART_OTP(3, "Smart OTP"),
    UNKNOWN(99, "UNKNOWN");

    private final Integer id;
    private final String name;

    ChallengeChannel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChallengeChannel findById(String id) {
        try {
            return findById(Integer.valueOf(id));
        } catch (NumberFormatException e) {
            return UNKNOWN;
        }
    }
    public static ChallengeChannel findById(Integer id) {
       for (ChallengeChannel c : ChallengeChannel.values()) {
           if (c.getId().equals(id)) {
               return c;
           }
       }
       return UNKNOWN;
    }
}
