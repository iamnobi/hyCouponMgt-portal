package ocean.acs.commons.okhttp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Response
 *
 * @author Alan Chen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Response {

    boolean isSuccess = false;
    String body = "";
    String message = "";
    Integer code;

}
