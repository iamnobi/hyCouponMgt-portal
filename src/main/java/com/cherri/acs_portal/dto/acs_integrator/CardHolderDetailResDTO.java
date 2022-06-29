package com.cherri.acs_portal.dto.acs_integrator;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class CardHolderDetailResDTO extends BaseResDTO {

  private String name;
  private String identityNumber;
  private String birthday;
  private EmailPhoneNumber credit;
  private EmailPhoneNumber debit;
  private Set<String> cardList;

  @Data
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  public static class EmailPhoneNumber {
    private String email;
    private String phoneNumber;
  }
}
