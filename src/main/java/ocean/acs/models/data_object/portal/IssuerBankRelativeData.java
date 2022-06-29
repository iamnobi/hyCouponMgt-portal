package ocean.acs.models.data_object.portal;

public interface IssuerBankRelativeData {
  String COL_DELETE_FLAG = "deleteFlag";
  String COL_DELETER = "deleter";
  String COL_DELETE_MILLIS = "deleteMillis";
  String COL_ISSUER_BANK_ID = "issuerBankId";
  String COL_DELETE_FLAG1 = "deleteFlag";
  
  Long getIssuerBankId();
  Boolean getDeleteFlag();
  String getDeleter();
  Long getDeleteMillis();
}
