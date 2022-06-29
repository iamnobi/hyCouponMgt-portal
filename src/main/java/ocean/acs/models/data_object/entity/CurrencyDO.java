package ocean.acs.models.data_object.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.models.oracle.entity.Currency;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyDO extends SysOperatorInfoDO {
    private String code;
    private String alpha;
    private Integer exponent;
    private Double usdExchangeRate;

    public static CurrencyDO valueOf(Currency entity) {
        CurrencyDO dataObj = new CurrencyDO();
        dataObj.setAlpha(entity.getAlpha());
        dataObj.setCode(entity.getCode());
        dataObj.setExponent(entity.getExponent());
        dataObj.setUsdExchangeRate(entity.getUsdExchangeRate());
        dataObj.setSysCreator(entity.getSysCreator());
        dataObj.setCreateMillis(entity.getCreateMillis());
        dataObj.setSysUpdater(entity.getSysUpdater());
        dataObj.setUpdateMillis(entity.getUpdateMillis());
        return dataObj;
    }

    public static CurrencyDO valueOf(ocean.acs.models.sql_server.entity.Currency entity) {
        CurrencyDO dataObj = new CurrencyDO();
        dataObj.setAlpha(entity.getAlpha());
        dataObj.setCode(entity.getCode());
        dataObj.setExponent(entity.getExponent());
        dataObj.setUsdExchangeRate(entity.getUsdExchangeRate());
        dataObj.setSysCreator(entity.getSysCreator());
        dataObj.setCreateMillis(entity.getCreateMillis());
        dataObj.setSysUpdater(entity.getSysUpdater());
        dataObj.setUpdateMillis(entity.getUpdateMillis());
        return dataObj;
    }

}