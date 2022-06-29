package com.cherri.acs_portal.dto.transactionLog;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.ChallengeCodeLogDO;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude
public class ChallengeCodeDto {

    @Column(name = "VERIFY_CODE")
    private String verifyCode;

    @Column(name = "VERIFY_STATUS")
    private Integer verifyStatus;

    @Column(name = "AUTH_ID")
    private String authID;

    public static List<ChallengeCodeDto> valueOf(List<ChallengeCodeLogDO> challengeCodeLogList) {
        List<ChallengeCodeDto> result = new ArrayList<>();
        challengeCodeLogList.stream()
          .map(
            codeLog ->
              result.add(
                new ChallengeCodeDto(
                  codeLog.getVerifyCode(), codeLog.getVerifyStatus(), codeLog.getAuthID())))
          .collect(Collectors.toList());
        return result;
    }
}
