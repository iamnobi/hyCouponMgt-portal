package com.cherri.acs_portal.service.audit;

import com.cherri.acs_portal.controller.request.BlackListDeviceOperationReqDto;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.service.BlackListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.models.dao.BlackListDeviceInfoDAO;
import ocean.acs.models.data_object.entity.BlackListDeviceInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListDeviceInfoAuditWrapper implements
  AuditServiceWrapper<BlackListDeviceOperationReqDto> {

    private final BlackListService blackListService;
    private final BlackListDeviceInfoDAO blackListDeviceInfoDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public BlackListDeviceInfoAuditWrapper(BlackListService blackListService,
      BlackListDeviceInfoDAO blackListDeviceInfoDao, ObjectMapper objectMapper) {
        this.blackListService = blackListService;
        this.blackListDeviceInfoDao = blackListDeviceInfoDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<BlackListDeviceOperationReqDto> add(BlackListDeviceOperationReqDto draftData) {
        return Optional.of(blackListService.createBlackListDevice(draftData));
    }

    @Override
    public boolean delete(DeleteDataDTO draftData) {
        blackListService.deleteBlackListDevice(draftData);
        return true;
    }

    @Override
    public Optional<BlackListDeviceOperationReqDto> update(
      BlackListDeviceOperationReqDto draftData) {
        return Optional.empty();
    }

    @Override
    public Optional<BlackListDeviceOperationReqDto> convertJsonToConcreteDTO(String draftInJson)
      throws IOException {
        return Optional
          .of(objectMapper.readValue(draftInJson, BlackListDeviceOperationReqDto.class));
    }

    @Override
    public Optional<BlackListDeviceOperationReqDto> getOriginalDataDTO(AuditableDTO draftData) {
        return Optional.empty(); // no need to implement currently.
    }

    @Override
    public Optional<AuditFileDTO> getOriginalFileDTO(BlackListDeviceOperationReqDto draftData) {
        return Optional.empty(); // no need to implement currently.
    }

    @Override
    public Optional<AuditFileDTO> getDraftFileDTO(BlackListDeviceOperationReqDto draftData) {
        return Optional.empty(); // no need to implement currently.
    }

    @Override
    public void filterForAuditUsed(BlackListDeviceOperationReqDto originalData) {
        originalData.setAuditStatus(null);
    }

    @Override
    public boolean isAuditingLockAvailable(AuditableDTO draftData) {
        Optional<BlackListDeviceInfoDO> deviceOpt = blackListDeviceInfoDao
          .findByIdAndNotDelete(draftData.getId());
        return deviceOpt
          .map(d -> AuditStatus.PUBLISHED == AuditStatus.getStatusBySymbol(d.getAuditStatus()))
          .orElse(false);
    }

    @Override
    public boolean lockDataAsAuditing(AuditableDTO draftData) {
        BlackListDeviceInfoDO blackListDeviceInfo = blackListDeviceInfoDao
          .findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "blackListDeviceInfo", draftData));

        blackListDeviceInfo.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        blackListDeviceInfoDao.save(blackListDeviceInfo);
        return true;
    }

    @Override
    public boolean unlockDataFromAuditing(AuditableDTO draftData) {
        BlackListDeviceInfoDO blackListDeviceInfo = blackListDeviceInfoDao
          .findByIdAndNotDelete(draftData.getId())
          .orElseThrow(() -> getMarkAuditingException("mark", "blackListDeviceInfo", draftData));

        blackListDeviceInfo.setAuditStatus(AuditStatus.PUBLISHED.getSymbol());
        blackListDeviceInfoDao.save(blackListDeviceInfo);
        return true;
    }

}
