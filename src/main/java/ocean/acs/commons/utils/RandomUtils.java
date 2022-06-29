package ocean.acs.commons.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * Random Utils
 *
 * @author Alan Chen
 */
@UtilityClass
public class RandomUtils {

    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    public int generateRandom(int maxNum) {
        return SECURE_RANDOM.nextInt(maxNum);
    }

    public List<Integer> generateRandomList(int maxItem, int maxNum) {
        List<Integer> list = new ArrayList<>(maxItem);
        for (int i = 0; i < maxItem; i++) {
            list.add(SECURE_RANDOM.nextInt(maxNum));
        }
        return list;
    }
}
