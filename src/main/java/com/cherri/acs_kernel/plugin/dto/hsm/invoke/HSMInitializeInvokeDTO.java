package com.cherri.acs_kernel.plugin.dto.hsm.invoke;

import com.cherri.acs_kernel.plugin.dto.HSMLoginInfo;
import com.cherri.acs_kernel.plugin.dto.InvokeDTO;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class HSMInitializeInvokeDTO extends InvokeDTO {
  private List<HSMLoginInfo> hsmLoginInfoList;

  @Builder
  public HSMInitializeInvokeDTO(
      List<HSMLoginInfo> hsmLoginInfoList, Map<String, String> systemPropertiesMap) {
    super(systemPropertiesMap);
    this.hsmLoginInfoList = hsmLoginInfoList;
  }
}
