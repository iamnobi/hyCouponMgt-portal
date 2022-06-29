package ocean.acs.commons.enumerator;

public enum TdsoIReqCode {

  ISO_CODE_NOT_VALID_PER_ISO_TABLES("54", "Iso code not valid per iso tables."),
  TRANSACTION_DATA_NOT_VALID("55", "Transaction data not valid."),
  PAN_IS_NOT_IN_RANGE_OR_UNEXPECTED_PAREQ("56", "Pan is not in range or unexpected PAReq."),
  TRANSIENT_SYSTEM_FAILURE("98", "Transient system failure."),
  PERMANENT_SYSTEM_FAILURE("99", "Permanent system failure.");

  private final String iReqCode;
  private final String iReqMessage;

  TdsoIReqCode(String iReqCode, String iReqMessage) {
    this.iReqCode = iReqCode;
    this.iReqMessage = iReqMessage;
  }

  public String getIReqCode() {
    return iReqCode;
  }

  public String getIReqMessage() {
    return iReqMessage;
  }
}
