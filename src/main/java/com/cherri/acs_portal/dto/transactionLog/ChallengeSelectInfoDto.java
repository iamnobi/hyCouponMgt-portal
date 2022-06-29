package com.cherri.acs_portal.dto.transactionLog;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ocean.acs.models.data_object.entity.ChallengeSelectInfoLogDO;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSelectInfoDto {

    private String key;
    private String value;

    public static List<ChallengeSelectInfoDto> valueOfChallengeSelectInfoLogList(
      List<ChallengeSelectInfoLogDO> challengeSelectInfoLogList) {
        List<ChallengeSelectInfoDto> result = new ArrayList<>();
        challengeSelectInfoLogList.stream()
          .forEach(
            selectInfoLog ->
              result.add(
                new ChallengeSelectInfoDto(selectInfoLog.getKey(), selectInfoLog.getValue())));
        return result;
    }
}
