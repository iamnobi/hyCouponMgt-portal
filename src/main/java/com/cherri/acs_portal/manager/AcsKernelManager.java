package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.dto.report.SystemHealthResponseDTO;

public interface AcsKernelManager {
  SystemHealthResponseDTO.KernelHealth.ApServerStatus[] ping();
}
