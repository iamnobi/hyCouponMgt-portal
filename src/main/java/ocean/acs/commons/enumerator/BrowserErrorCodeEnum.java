package ocean.acs.commons.enumerator;

import java.util.Arrays;
import lombok.Getter;

/** 瀏覽器異常紀錄錯誤代碼<br>
 *  請對照ERROR_CODE_MAPPING資料表
 * */
public enum BrowserErrorCodeEnum {

    /** Client端發起第一次驗證(Challenge)請求逾時 */
    CLIENT_FIRST_CREQ_TIMEOUT(4000),
    /** 使用者驗證(challenge)操作逾時 */
    CHALLENGE_OPERATION_TIMEOUT(4001),
    /** CReq傳入無效的參數 */
    CREQ_INVALID_ARGS(4002),
    /** 驗證(Challenge)時系統異常 */
    CHALLENGE_SYSTEM_ABNORMAL(4003);

    @Getter
    private Integer errorCode;

    BrowserErrorCodeEnum(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /** 是否為瀏覽器錯誤紀錄的錯誤代碼 */
    public static boolean isBrowserErrorCode(Integer errorCode) {
        return Arrays.stream(BrowserErrorCodeEnum.values())
                .anyMatch(e -> e.getErrorCode().equals(errorCode));
    }

}
