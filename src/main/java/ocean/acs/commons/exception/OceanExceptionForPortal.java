package ocean.acs.commons.exception;


import ocean.acs.commons.enumerator.ResultStatus;


public class OceanExceptionForPortal extends RuntimeException {

  protected ResultStatus resultStatus = ResultStatus.UNKNOWN;

  public OceanExceptionForPortal() {
    super();
  }

  public OceanExceptionForPortal(String message) {
    super(message);
  }

  public OceanExceptionForPortal(String message, Throwable cause) {
    super(message, cause);
  }

  public OceanExceptionForPortal(ResultStatus issueStatus) {
    resultStatus = issueStatus;
  }

  /**
   * 自定義錯誤訊息內容
   *
   * @param issueStatus
   * @param errorMessage
   */
  public OceanExceptionForPortal(ResultStatus issueStatus, String errorMessage) {
    super(errorMessage);
    resultStatus = issueStatus;
  }

  public OceanExceptionForPortal(ResultStatus issueStatus, String errorMessage, Throwable cause) {
    super(errorMessage, cause);
    resultStatus = issueStatus;
  }

  public ResultStatus getResultStatus() {
    return resultStatus;
  }
}

//
//
//  ResultStatus status;
//  String message;
//  MessageType messageType;
//}
