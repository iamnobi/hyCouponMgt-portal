package ocean.acs.models.oracle.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ocean.acs.models.oracle.entity.BrowserErrorLog;

@Repository
public interface BrowserErrorLogRepository extends JpaRepository<BrowserErrorLog, Long> {

    /** 從KERNEL_TRANSACTION_LOG查詢前一日的資料並新增至瀏覽器異常紀錄資料表BROWSER_ERROR_LOG */
    @Modifying
    @Query(value =
            "insert into BROWSER_ERROR_LOG (\n" +
                    "    ID,\n" +
                    "    ISSUER_BANK_ID, \n" +
                    "    YEAR,\n" +
                    "    MONTH,\n" +
                    "    DAY_OF_MONTH,\n" +
                    "    BROWSER_TYPE,\n" +
                    "    ERROR_CODE,\n" +
                    "    ERROR_CODE_DAY_COUNT," +
                    "    DATA_MILLIS,\n" +
                    "    SYS_CREATOR,\n" +
                    "    CREATE_MILLIS\n" +
                    ")\n" +
                    "select \n" +
                    "    BROWSER_ERROR_LOG_SEQ.NEXTVAL,\n" +
                    "    ISSUER_BANK_ID,\n" +
                    "    EXTRACT(YEAR FROM TRUNC(SYSDATE - 1)) AS YEAR,\n" +
                    "    EXTRACT(MONTH FROM TRUNC(SYSDATE - 1)) AS MONTH,\n" +
                    "    EXTRACT(DAY FROM TRUNC(SYSDATE - 1)) AS DAY,\n" +
                    "    BROWSER_TYPE, \n" +
                    "    CHALLENGE_ERROR_REASON_CODE, \n" +
                    "    ERROR_COUNT, \n" +
                    "    (TRUNC(SYSDATE - 1)  - TO_DATE('1970/01/01','YYYY/MM/DD')) * (24 * 60 * 60 * 1000) + TO_NUMBER(TO_CHAR(SYSTIMESTAMP,'FF6')) AS DATA_MILLIS, \n" +
                    "    ?1,\n" +
                    "    (SYSDATE - TO_DATE('1970/01/01','YYYY/MM/DD')) * (24 * 60 * 60 * 1000) + TO_NUMBER(TO_CHAR(SYSTIMESTAMP,'FF6')) AS TODAY_MILLIS\n" +
                    "from ( \n" +
                    "    select\n" +
                    "        ISSUER_BANK_ID,\n" +
                    "        BROWSER_TYPE,\n" +
                    "        CHALLENGE_ERROR_REASON_CODE,\n" +
                    "        count(CHALLENGE_ERROR_REASON_CODE) AS ERROR_COUNT\n" +
                    "    from (\n" +
                    "        select\n" +
                    "            K.ISSUER_BANK_ID,\n" +
                    "                case \n" +
                    "                when BROWSER_TYPE not in ('IE','Chrome', 'Firefox','Safari','Edge') then 'Others'\n" +
                    "                when BROWSER_TYPE is null then 'Others'\n" +
                    "                else BROWSER_TYPE\n" +
                    "                end\n" +
                    "                as BROWSER_TYPE,\n" +
                    "            BROWSER_MAJOR_VERSION,\n" +
                    "            CHALLENGE_ERROR_REASON_CODE\n" +
                    "        from KERNEL_TRANSACTION_LOG K \n" +
                    "        join ISSUER_BANK I on K.ISSUER_BANK_ID = I.ID\n" +
                    "        where 1 = 1\n" +
                    "        and I.DELETE_FLAG = 0\n" +
                    "        and (\n" +
                    "                case\n" +
                    "                    when BROWSER_TYPE = 'IE' and BROWSER_MAJOR_VERSION >= 11 then 1\n" +
                    "                    when BROWSER_TYPE in ('Chrome', 'Firefox','Safari','Edge') and BROWSER_MAJOR_VERSION >= 50 then 1\n" +
                    "                    else 1\n" +
                    "                end\n" +
                    "            ) = 1\n" +
                    "        and to_char(trim(CHALLENGE_ERROR_REASON_CODE)) in ( ?2 )\n" +
                    "        and to_date('1970/01/01', 'YYYY/MM/DD') + (K.CREATE_MILLIS)/1000/60/60/24 between trunc(SYSDATE - 1) and trunc(SYSDATE)\n" +
                    "    )\n" +
                    "    group by ISSUER_BANK_ID, BROWSER_TYPE, CHALLENGE_ERROR_REASON_CODE\n" +
                    ")", nativeQuery = true)
    void generateDailyBrowserErrorLog(String operator, List<Integer> codes);

}
