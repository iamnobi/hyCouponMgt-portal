package ocean.acs.models.data_object.kernel;

import lombok.Data;
import ocean.acs.models.oracle.entity.VELog;

@Data
public class VELogDO {
  private String id;
  private Long issuerBankID;
  private Long panInfoID;
  private String cardBrand;
  /** only exists if CH_ENROLLED != Y (error or bin range not found) */
  private String maskCardNumber;
  private String messageId;
  private String version;
  private String merchantAcquirerBIN;
  private String merchantMerId;
  private String browserDeviceCategory;
  private String browserAccept;
  private String browserUserAgent;
  private String extensions;
  private String chEnrolled;
  private String chAcctId;
  private String url;
  private String errorCode;
  private String errorDetail;
  private String apId;
  private String sysCreator;
  private Long createMillis = System.currentTimeMillis();
  private String sysUpdater;
  private Long updateMillis;

  public static VELogDO valueOf(VELog veLog) {
    VELogDO veLogDO = new VELogDO();
    veLogDO.setId(veLog.getId());
    veLogDO.setIssuerBankID(veLog.getIssuerBankID());
    veLogDO.setPanInfoID(veLog.getPanInfoID());
    veLogDO.setCardBrand(veLog.getCardBrand());
    veLogDO.setMaskCardNumber(veLog.getMaskCardNumber());
    veLogDO.setMessageId(veLog.getMessageId());
    veLogDO.setVersion(veLog.getVersion());
    veLogDO.setMerchantAcquirerBIN(veLog.getMerchantAcquirerBIN());
    veLogDO.setMerchantMerId(veLog.getMerchantMerId());
    veLogDO.setBrowserDeviceCategory(veLog.getBrowserDeviceCategory());
    veLogDO.setBrowserAccept(veLog.getBrowserAccept());
    veLogDO.setBrowserUserAgent(veLog.getBrowserUserAgent());
    veLogDO.setExtensions(veLog.getExtensions());
    veLogDO.setChEnrolled(veLog.getChEnrolled());
    veLogDO.setChAcctId(veLog.getChAcctId());
    veLogDO.setUrl(veLog.getUrl());
    veLogDO.setErrorCode(veLog.getErrorCode());
    veLogDO.setErrorDetail(veLog.getErrorDetail());
    veLogDO.setSysCreator(veLog.getSysCreator());
    veLogDO.setCreateMillis(veLog.getCreateMillis());
    veLogDO.setSysUpdater(veLog.getSysUpdater());
    veLogDO.setUpdateMillis(veLog.getUpdateMillis());
    return veLogDO;
  }
}
