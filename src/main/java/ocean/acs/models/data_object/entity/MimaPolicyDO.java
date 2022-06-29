package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * MimaPolicyDO
 *
 * @author Alan Chen
 */
@Data
@Builder
@AllArgsConstructor
public class MimaPolicyDO {

    private Long id;
    private Long issuerBankId;
    private Integer loginRetryCount;
    private Integer mimaFreshInterval;
    private Integer accountMaxIdleDay;
    private Integer mimaMaxLength;
    private Integer mimaMinLength;
    private Integer mimaAlphabetCount;
    private Integer mimaMinLowerCase;
    private Integer mimaMinUpperCase;
    private Integer mimaMinNumeric;
    private Integer mimaMinSpecialChar;
    private Integer mimaHistoryDupCount;
    private long createTime;
    private long updateTime;

    public static MimaPolicyDO valueOf(ocean.acs.models.oracle.entity.MimaPolicy e) {
        return MimaPolicyDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .loginRetryCount(e.getLoginRetryCount())
          .mimaFreshInterval(e.getMimaFreshInterval())
          .accountMaxIdleDay(e.getAccountMaxIdleDay())
          .mimaMaxLength(e.getMimaMaxLength())
          .mimaMinLength(e.getMimaMinLength())
          .mimaAlphabetCount(e.getMimaAlphabetCount())
          .mimaMinLowerCase(e.getMimaMinLowerCase())
          .mimaMinUpperCase(e.getMimaMinUpperCase())
          .mimaMinNumeric(e.getMimaMinNumeric())
          .mimaMinSpecialChar(e.getMimaMinSpecialChar())
          .mimaHistoryDupCount(e.getMimaHistoryDupCount())
          .createTime(e.getCreateTime())
          .updateTime(e.getUpdateTime())
          .build();
    }

    public static MimaPolicyDO valueOf(ocean.acs.models.sql_server.entity.MimaPolicy e) {
        return MimaPolicyDO.builder()
          .id(e.getId())
          .issuerBankId(e.getIssuerBankId())
          .loginRetryCount(e.getLoginRetryCount())
          .mimaFreshInterval(e.getMimaFreshInterval())
          .accountMaxIdleDay(e.getAccountMaxIdleDay())
          .mimaMaxLength(e.getMimaMaxLength())
          .mimaMinLength(e.getMimaMinLength())
          .mimaAlphabetCount(e.getMimaAlphabetCount())
          .mimaMinLowerCase(e.getMimaMinLowerCase())
          .mimaMinUpperCase(e.getMimaMinUpperCase())
          .mimaMinNumeric(e.getMimaMinNumeric())
          .mimaMinSpecialChar(e.getMimaMinSpecialChar())
          .mimaHistoryDupCount(e.getMimaHistoryDupCount())
          .createTime(e.getCreateTime())
          .updateTime(e.getUpdateTime())
          .build();
    }

}
