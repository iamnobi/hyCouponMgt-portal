package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.BrowserType;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BROWSER_ERROR_LOG)
public class BrowserErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_ID)
    private Long id;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_YEAR)
    private Integer year;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_MONTH)
    private Integer month;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_DAY_OF_MONTH)
    private Integer dayOfMonth;

    @NonNull
    @Enumerated
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_BROWSER_TYPE)
    private BrowserType browserType;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_ERROR_CODE)
    private Integer errorCode;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_ERROR_CODE_DAY_COUNT)
    private Integer errorCodeDayCount;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_SYS_CREATOR)
    private String sysCreator;

    @NonNull
    @Column(name = DBKey.COL_BROWSER_ERROR_LOG_CREATE_MILLIS)
    private Long createMillis;

}
