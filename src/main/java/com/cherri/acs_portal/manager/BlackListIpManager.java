package com.cherri.acs_portal.manager;

import com.cherri.acs_portal.controller.request.BlackListIpQueryDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import java.util.List;

public interface BlackListIpManager {

    PagingResultDTO<BlackListIpGroupDTO> query(BlackListIpQueryDTO queryDto);

    List<BlackListIpGroupDTO> findByIds(IdsQueryDTO queryDto);
}
