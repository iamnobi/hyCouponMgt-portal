package ocean.acs.commons.enumerator;

/** 給前端Renew判斷用的 */
public enum RenewStatus {

  /** 不顯示renew按鈕 */
  NONE,

  /** 顯示renew按鈕 */
  INIT,

  /** 憑證正在申請中，顯示上傳憑證按鈕 */
  PROCESSING
}
