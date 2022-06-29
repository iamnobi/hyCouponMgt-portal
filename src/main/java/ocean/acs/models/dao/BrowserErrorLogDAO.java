package ocean.acs.models.dao;

import java.util.List;
import ocean.acs.models.data_object.portal.BrowserErrorLogResultDO;

public interface BrowserErrorLogDAO {

    /** 產生每日（執行前一日00:00-23:59:59）瀏覽器異常紀錄資料 */
    boolean generateDailyBrowserErrorLog(String operator, List<Integer> errorCodes);

    /** 
     * The original report query method before time zone problem.
     * 查詢瀏覽器錯誤紀錄 by 銀行id，年，月 
     */
    @Deprecated
    List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonth(Long issuerBankId,
            long startMillis, long endMillis);

    /**
     * The original report query method before time zone problem.
     * 查詢瀏覽器錯誤紀錄 by 銀行id，年，月，日 
     */
    @Deprecated
    List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonthAndDay(Long issuerBankId,
            long startMillis, long endMillis);

    /** 月報表，查詢瀏覽器錯誤紀錄 by 銀行id，年，月 */
    List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonth(long issuerBankId, int year,
            int month);

    /** 日報表，查詢瀏覽器錯誤紀錄 by 銀行id，年，月，日 */
    List<BrowserErrorLogResultDO> findByIssuerBankIdAndYearAndMonthAndDay(long issuerBankId,
            int year, int month, int day);

}
