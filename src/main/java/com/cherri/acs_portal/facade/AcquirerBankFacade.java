package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.AcquirerBank3dsSdkRefNumCreateRequest;
import com.cherri.acs_portal.controller.request.AcquirerBank3dsSdkRefNumDeleteRequest;
import com.cherri.acs_portal.dto.AcquirerBank3dsRefNumDTO;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.service.AcquirerBank3dsRefNumService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class AcquirerBankFacade {
  private final AcquirerBank3dsRefNumService acquirerBank3dsRefNumService;

  public ApiResponse<List<AcquirerBank3dsRefNumDTO>> query3dsRefNumList() {
    try {
      return new ApiResponse<>(
          acquirerBank3dsRefNumService.query3dsRefNumList()
      );
    } catch (Exception e) {
      log.error("[query3dsRefNumList] Exception happened.", e);
      return new ApiResponse<>(ResultStatus.SERVER_ERROR);
    }
  }

  public ApiResponse<Boolean> create3dsRefNum(
      AcquirerBank3dsSdkRefNumCreateRequest request, String user) {
    try {
      String sdkRefNum = request.getSdkRefNumber();
      return new ApiResponse<>(
          acquirerBank3dsRefNumService.create3dsRefNum(sdkRefNum, user)
      );
    } catch (OceanException e) {
      return new ApiResponse<>(
          e.getResultStatus(),
          e.getMessage()
      );
    } catch (Exception e) {
      log.error("[create3dsRefNum] Exception happened.", e);
      return new ApiResponse<>(ResultStatus.SERVER_ERROR);
    }
  }

  public ApiResponse<Boolean> delete3dsRefNum(
      AcquirerBank3dsSdkRefNumDeleteRequest request) {
    try {
      long id = request.getId();
      return new ApiResponse<>(
          acquirerBank3dsRefNumService.delete3dsRefNum(id)
      );
    } catch (OceanException e) {
      return new ApiResponse<>(
          e.getResultStatus(),
          e.getMessage()
      );
    } catch (Exception e) {
      log.error("[delete3dsRefNum] Exception happened.", e);
      return new ApiResponse<>(ResultStatus.SERVER_ERROR);
    }
  }
}
