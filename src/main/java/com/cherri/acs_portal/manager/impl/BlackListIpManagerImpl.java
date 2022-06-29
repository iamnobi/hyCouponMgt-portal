package com.cherri.acs_portal.manager.impl;

import com.cherri.acs_portal.constant.MessageConstants;
import com.cherri.acs_portal.controller.request.BlackListIpQueryDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.blackList.output.BlackListIpGroupDTO;
import com.cherri.acs_portal.dto.common.IdsQueryDTO;
import com.cherri.acs_portal.manager.BlackListIpManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.BlackListIpGroupDAO;
import ocean.acs.models.dao.TransactionLogDAO;
import ocean.acs.models.data_object.entity.BlackListIpGroupDO;
import ocean.acs.models.oracle.entity.BlackListIpGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BlackListIpManagerImpl implements BlackListIpManager {

    private final BlackListIpGroupDAO blackListIpGroupDAO;
    private final TransactionLogDAO transactionLogDAO;

    @Autowired
    public BlackListIpManagerImpl(
      BlackListIpGroupDAO ipGroupRepo, TransactionLogDAO transactionLogDAO) {
        this.blackListIpGroupDAO = ipGroupRepo;
        this.transactionLogDAO = transactionLogDAO;
    }

    @Override
    public PagingResultDTO<BlackListIpGroupDTO> query(BlackListIpQueryDTO queryDto) {
        Page<BlackListIpGroup> ipGroupPage = doQuery(queryDto);
        List<Integer> reasonCodeList = getAresResultReasonCodeList();
        List<BlackListIpGroupDTO> data =
          ipGroupPage.getContent().stream()
            .map(
              entity -> {
                  BlackListIpGroupDTO dto = new BlackListIpGroupDTO();
                  dto.setId(entity.getId());
                  dto.setIssuerBankId(entity.getIssuerBankId());
                  dto.setOriginVersion(entity.getOriginVersion());
                  dto.setIp(entity.getIp());
                  dto.setCidr(entity.getCidr());
                  dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
                  dto.setCreator(entity.getCreator());
                  dto.setCreateMillis(entity.getCreateMillis());
                  dto.setUpdater(entity.getUpdater());
                  Long ipGroupId = entity.getId();

                  Integer blockTimes =
                    transactionLogDAO.countByBlackListIpGroupIdAndAresResultReasonCodeIn(
                      ipGroupId, reasonCodeList);
                  dto.setBlockTimes(blockTimes);
                  dto.setAuditStatus(AuditStatus.getStatusBySymbol(entity.getAuditStatus()));
                  return dto;
              })
            .collect(Collectors.toList());
        PagingResultDTO response = PagingResultDTO.valueOf(ipGroupPage);
        response.setData(data);
        return response;
    }

    private List<Integer> getAresResultReasonCodeList() {
        return Arrays.asList(ResultStatus.BLACK_IP_C.getCode(), ResultStatus.BLACK_IP_N.getCode());
    }

    private Page<BlackListIpGroup> doQuery(BlackListIpQueryDTO queryDto) {
        PageRequest page = PageRequest.of(queryDto.getPage() - 1, queryDto.getPageSize(),
          Sort.Direction.DESC, "createMillis");

        BlackListIpGroupDO blackListIpGroupDO = new BlackListIpGroupDO();
        blackListIpGroupDO.setStartTime(queryDto.getStartTime());
        blackListIpGroupDO.setEndTime(queryDto.getEndTime());
        blackListIpGroupDO.setIssuerBankId(queryDto.getIssuerBankId());
        blackListIpGroupDO.setIp(queryDto.getIp());

        try {
            Page<BlackListIpGroupDO> ipGroupDoPage = blackListIpGroupDAO
              .findAll(blackListIpGroupDO, page);
            if (ipGroupDoPage.isEmpty()) {
                return Page.empty();
            }
            List<BlackListIpGroup> list = ipGroupDoPage.getContent().stream()
              .map(BlackListIpGroup::valueOf).collect(Collectors.toList());
            return new PageImpl<>(list, page, ipGroupDoPage.getTotalElements());
        } catch (Exception e) {
            log.error("[doQuery] unknown exception, queryDto={}", queryDto, e);
            String errMsg =
              String.format(
                "Query ip failed, error message: %s query params:%s", e.getMessage(), queryDto);
            throw new OceanException(ResultStatus.DB_READ_ERROR, errMsg);
        }
    }

    @Override
    public List<BlackListIpGroupDTO> findByIds(IdsQueryDTO queryDto) {
        PageRequest page = PageRequest.of(0, 1000, Sort.Direction.DESC, "createMillis");
        List<BlackListIpGroupDO> dataList = blackListIpGroupDAO.findByIdIn(queryDto.getIds(), page);
        if (dataList == null || dataList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<Integer> reasonCodeList = getAresResultReasonCodeList();
        List<BlackListIpGroupDTO> resultList = new ArrayList<>();
        for (BlackListIpGroupDO entity : dataList) {
            BlackListIpGroupDTO dto = BlackListIpGroupDTO.valueOf(entity);
            Integer blockTimes = transactionLogDAO
              .countByBlackListIpGroupIdAndAresResultReasonCodeIn(dto.getId(), reasonCodeList);
            dto.setBlockTimes(blockTimes);
            resultList.add(dto);
        }
        return resultList;
    }
}
