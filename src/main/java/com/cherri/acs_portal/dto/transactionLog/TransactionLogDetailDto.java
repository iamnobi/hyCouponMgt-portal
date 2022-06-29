package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude
public class TransactionLogDetailDto {

  private ThreeDSMethodDto threeDSMethod;
  private AuthRequestDto authReq;
  private AuthResponseDto authRes;
  private List<ChallengeRequestDto> challengeReq;
  private List<ChallengeResponseDto> challengeRes;
  private ResultRequestDto resultReq;
  private ResultResponseDto resultRes;
  private ErrorMessageDto error;
  @JsonProperty(value = "DDCA Result")
  private DdcaLogResultDto ddcaLogResultDto;
  @JsonProperty(value = "Classical RBA Result")
  private ClassicRbaResultDto classicRbaResultDto;
}
