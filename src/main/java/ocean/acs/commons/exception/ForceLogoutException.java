package ocean.acs.commons.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ocean.acs.commons.enumerator.ResultStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForceLogoutException extends RuntimeException {

  private static final long serialVersionUID = -2098289865775224993L;
  protected ResultStatus status;
  protected String message;

  public ForceLogoutException(ResultStatus status) {
    super(status.toString());
    this.status = status;
    this.message = status.toString();
  }

  /**
   * 自定義錯誤訊息內容
   */
  public ForceLogoutException(ResultStatus status, String errorMessage) {
    super(errorMessage);
    this.status = status;
    this.message = errorMessage;
  }

  public ResultStatus getResultStatus() {
    return status;
  }
}

