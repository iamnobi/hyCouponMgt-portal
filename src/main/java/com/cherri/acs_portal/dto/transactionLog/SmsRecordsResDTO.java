package com.cherri.acs_portal.dto.transactionLog;

import com.cherri.acs_portal.controller.response.SmsRecordDTO;
import com.cherri.acs_portal.dto.acs_integrator.BaseResDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsRecordsResDTO extends BaseResDTO {

  private List<SmsRecordDTO> data;
}
