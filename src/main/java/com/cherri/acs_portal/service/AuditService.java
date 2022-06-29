package com.cherri.acs_portal.service;

import com.cherri.acs_portal.controller.ContextProvider;
import com.cherri.acs_portal.controller.response.DataEditResultDTO;
import com.cherri.acs_portal.dto.PagingResultDTO;
import com.cherri.acs_portal.dto.audit.AuditContentDTO;
import com.cherri.acs_portal.dto.audit.AuditFileDTO;
import com.cherri.acs_portal.dto.audit.AuditResultDTO;
import com.cherri.acs_portal.dto.audit.AuditableDTO;
import com.cherri.acs_portal.dto.audit.AuditingLogQueryDTO;
import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import com.cherri.acs_portal.manager.PermissionManager;
import com.cherri.acs_portal.service.audit.AccountGroupWrapper;
import com.cherri.acs_portal.service.audit.AttemptGrantedAuditWrapper;
import com.cherri.acs_portal.service.audit.AuditServiceWrapper;
import com.cherri.acs_portal.service.audit.BankAdminAuditWrapper;
import com.cherri.acs_portal.service.audit.BankManagementAuditWrapper;
import com.cherri.acs_portal.service.audit.BinRangeAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListAuthStatusAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListBatchImportAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListDeviceInfoAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListIpGroupAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListMerchantUrlAuditWrapper;
import com.cherri.acs_portal.service.audit.BlackListPanAuditWrapper;
import com.cherri.acs_portal.service.audit.CaCertAuditWrapper;
import com.cherri.acs_portal.service.audit.CardBrandLogoWrapper;
import com.cherri.acs_portal.service.audit.CertSigningAuditWrapper;
import com.cherri.acs_portal.service.audit.CertSslAuditWrapper;
import com.cherri.acs_portal.service.audit.CertSslP12AuditWrapper;
import com.cherri.acs_portal.service.audit.ChallengeViewMessageAuditWrapper;
import com.cherri.acs_portal.service.audit.ClassicRbaSettingAuditWrapper;
import com.cherri.acs_portal.service.audit.ErrorCodeGroupAuditWrapper;
import com.cherri.acs_portal.service.audit.IssuerBrandLogoAuditWrapper;
import com.cherri.acs_portal.service.audit.IssuerHandlingFeeAuditWrapper;
import com.cherri.acs_portal.service.audit.IssuerLogoUpdateAuditWrapper;
import com.cherri.acs_portal.service.audit.IssuerTradingChannelAuditWrapper;
import com.cherri.acs_portal.service.audit.OperatorIdUpdateWrapper;
import com.cherri.acs_portal.service.audit.OtpSendingKeyUploadWrapper;
import com.cherri.acs_portal.service.audit.OtpSendingSettingWrapper;
import com.cherri.acs_portal.service.audit.PermissionWrapper;
import com.cherri.acs_portal.service.audit.SecretKeyManagementAuditWrapper;
import com.cherri.acs_portal.service.audit.SmsTemplateAuditWrapper;
import com.cherri.acs_portal.service.audit.ThreeDSVerifyAuditWrapper;
import com.cherri.acs_portal.service.audit.UnlockOtpVerifyAuditWrapper;
import com.cherri.acs_portal.service.audit.UserGroupWrapper;
import com.cherri.acs_portal.service.audit.UserManagementAuditWrapper;
import com.cherri.acs_portal.service.audit.WhiteListPanAuditWrapper;
import com.cherri.acs_portal.util.StringCustomizedUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import ocean.acs.commons.enumerator.AuditActionType;
import ocean.acs.commons.enumerator.AuditFunctionType;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.DatabaseException;
import ocean.acs.commons.exception.NoSuchDataException;
import ocean.acs.commons.exception.OceanException;
import ocean.acs.models.dao.AuditingDAO;
import ocean.acs.models.dao.IssuerBankDAO;
import ocean.acs.models.dao.ModuleSettingDAO;
import ocean.acs.models.data_object.entity.AuditingDO;
import ocean.acs.models.data_object.entity.IssuerBankDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
public class AuditService extends ContextProvider {
  private final AuditingDAO auditingDAO;
  private final ModuleSettingDAO moduleSettingDAO;
  private final IssuerBankDAO issuerBankDAO;
  private final ObjectMapper jsonObjMapper;
  private final PermissionManager permissionManager;
  private Map<AuditFunctionType, AuditServiceWrapper> auditServiceMap;

  @Autowired
  public AuditService(
      AuditingDAO auditingDAO,
      ObjectMapper jsonObjMapper,
      BinRangeAuditWrapper binRangeAuditWrapper,
      UserManagementAuditWrapper userManagementAuditWrapper,
      BankManagementAuditWrapper bankManagementAuditWrapper,
      IssuerHandlingFeeAuditWrapper issuerHandlingFeeAuditWrapper,
      IssuerBrandLogoAuditWrapper issuerBrandLogoAuditWrapper,
      IssuerTradingChannelAuditWrapper issuerTradingChannelAuditWrapper,
      BlackListAuthStatusAuditWrapper blackListAuthStatusAuditWrapper,
      BlackListPanAuditWrapper blackListPanAuditWrapper,
      BlackListDeviceInfoAuditWrapper blackListDeviceInfoAuditWrapper,
      BlackListIpGroupAuditWrapper blackListIpGroupAuditWrapper,
      BlackListMerchantUrlAuditWrapper blackListMerchantUrlAuditWrapper,
      WhiteListPanAuditWrapper whiteListPanAuditWrapper,
      AttemptGrantedAuditWrapper attemptGrantedAuditWrapper,
      ThreeDSVerifyAuditWrapper threeDSVerifyAuditWrapper,
      UnlockOtpVerifyAuditWrapper unlockOtpVerifyAuditWrapper,
      BankAdminAuditWrapper bankAdminAuditWrapper,
      BlackListBatchImportAuditWrapper blackListBatchImportAuditWrapper,
      SecretKeyManagementAuditWrapper secretKeyManagementAuditWrapper,
      ChallengeViewMessageAuditWrapper challengeViewMessageAuditWrapper,
      SmsTemplateAuditWrapper smsTemplateAuditWrapper,
      CaCertAuditWrapper caCertAuditWrapper,
      CertSslP12AuditWrapper certSslP12AuditWrapper,
      CertSslAuditWrapper certSslAuditWrapper,
      CertSigningAuditWrapper certSigningAuditWrapper,
      IssuerLogoUpdateAuditWrapper issuerLogoUpdateAuditWrapper,
      OperatorIdUpdateWrapper operatorIdUpdateWrapper,
      ModuleSettingDAO moduleSettingDAO,
      PermissionManager permissionManager,
      ErrorCodeGroupAuditWrapper errorCodeGroupAuditWrapper,
      CardBrandLogoWrapper cardBrandLogoWrapper,
      UserGroupWrapper userGroupWrapper,
      PermissionWrapper permissionWrapper,
      AccountGroupWrapper accountGroupWrapper,
      IssuerBankDAO issuerBankDAO,
      OtpSendingSettingWrapper otpSendingSettingWrapper,
      OtpSendingKeyUploadWrapper otpSendingKeyUploadWrapper,
      ClassicRbaSettingAuditWrapper classicRbaSettingAuditWrapper
      ) {
    this.auditingDAO = auditingDAO;
    this.jsonObjMapper = jsonObjMapper;
    this.moduleSettingDAO = moduleSettingDAO;
    this.permissionManager = permissionManager;
    this.issuerBankDAO = issuerBankDAO;

    auditServiceMap = new HashMap<>();
    auditServiceMap.put(AuditFunctionType.USER_ACCOUNT, userManagementAuditWrapper);
    auditServiceMap.put(AuditFunctionType.UNLOCK_ACTION, userManagementAuditWrapper);

    auditServiceMap.put(AuditFunctionType.BLACK_LIST_AUTH_STATUS, blackListAuthStatusAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BLACK_LIST_PAN, blackListPanAuditWrapper);
    auditServiceMap.put(
        AuditFunctionType.BLACK_LIST_BATCH_IMPORT, blackListBatchImportAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BLACK_LIST_DEVICE, blackListDeviceInfoAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BLACK_LIST_IP, blackListIpGroupAuditWrapper);
    auditServiceMap.put(
        AuditFunctionType.BLACK_LIST_MERCHANT_URL, blackListMerchantUrlAuditWrapper);
    auditServiceMap.put(AuditFunctionType.WHITE_LIST, whiteListPanAuditWrapper);

    auditServiceMap.put(AuditFunctionType.BANK_MANAGE, bankManagementAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_ADMIN, bankAdminAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_FEE, issuerHandlingFeeAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_LOGO, issuerBrandLogoAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_CHANNEL, issuerTradingChannelAuditWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_OTP_SENDING, otpSendingSettingWrapper);
    auditServiceMap.put(AuditFunctionType.BANK_OTP_KEY_UPLOAD, otpSendingKeyUploadWrapper);

    auditServiceMap.put(AuditFunctionType.SYS_CA_CERT, caCertAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_SSL_P12_CERT, certSslP12AuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_SSL_CERT, certSslAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_SIGNING_CERT, certSigningAuditWrapper);

    auditServiceMap.put(AuditFunctionType.ATTEMPT, attemptGrantedAuditWrapper);
    auditServiceMap.put(AuditFunctionType.THREE_DS_VERIFY_ENABLED, threeDSVerifyAuditWrapper);
    auditServiceMap.put(AuditFunctionType.UNLOCK_OTP, unlockOtpVerifyAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_BIN_RANGE, binRangeAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_KEY, secretKeyManagementAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_CHALLENGE_VIEW, challengeViewMessageAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_CHALLENGE_SMS_MSG, smsTemplateAuditWrapper);
    auditServiceMap.put(AuditFunctionType.SYS_ISSUER_LOGO, issuerLogoUpdateAuditWrapper);
      auditServiceMap.put(AuditFunctionType.SYS_ACS_OPERATOR_ID, operatorIdUpdateWrapper);
      auditServiceMap.put(AuditFunctionType.SYS_ERROR_CODE, errorCodeGroupAuditWrapper);
      auditServiceMap.put(AuditFunctionType.SYS_CARD_BRAND_LOGO, cardBrandLogoWrapper);
      auditServiceMap.put(AuditFunctionType.USER_GROUP, userGroupWrapper);
      auditServiceMap.put(AuditFunctionType.PERMISSION, permissionWrapper);
      auditServiceMap.put(AuditFunctionType.ACCOUNT_GROUP, accountGroupWrapper);
      auditServiceMap.put(AuditFunctionType.CLASSIC_RBA, classicRbaSettingAuditWrapper);
  }

    public AuditServiceWrapper getAuditService(@NonNull AuditFunctionType functionType) {
        return auditServiceMap.get(functionType);
    }

    public boolean isAuditingOnDemand(AuditFunctionType functionType) {
      // NOTE: 本產品暫時一律不支援 audit
      return false;
//        boolean isAdmin = isSystemAdmin();
//
//        return !isAdmin && moduleSettingDAO.isAuditOnDemand(functionType);
    }

    @Transactional(rollbackFor = Exception.class)
    public DataEditResultDTO requestAudit(
      AuditFunctionType functionType, AuditActionType actionType, AuditableDTO requestDTO)
      throws DatabaseException {
        triggerAuditLock(functionType, actionType, requestDTO);
        AuditingDO auditing = addAuditRequest(functionType, actionType, requestDTO);

        return new DataEditResultDTO(auditing);
    }

    private boolean triggerAuditLock(
      AuditFunctionType functionType, AuditActionType actionType, AuditableDTO requestDTO)
      throws DatabaseException {
        if (actionType != AuditActionType.DELETE && actionType != AuditActionType.UPDATE) {
            return true;
        }

        AuditServiceWrapper serviceWrapper = auditServiceMap.get(functionType);

        if (!serviceWrapper.isAuditingLockAvailable(requestDTO)) {
            log.error(
              "[triggerAuditLock] Failed in get Audit lock. functionType={}, id={}, issuerBankId={}",
              functionType.getTypeSymbol(), requestDTO.getId(), requestDTO.getIssuerBankId());
            throw new OceanException(ResultStatus.AUDITING_LOCK_UNAVAILABLE);
        }

        return serviceWrapper.lockDataAsAuditing(requestDTO);
    }

    private AuditingDO addAuditRequest(
      AuditFunctionType functionType, AuditActionType actionType, AuditableDTO requestDTO) {
        String draftContent = "";

        try {
            draftContent = jsonObjMapper.writeValueAsString(requestDTO);
        } catch (JsonProcessingException e) {
            log.error(
              "[addAuditRequest] Failed in request auditing process due to exception in convert request to JSON.",
              e);
            throw new OceanException(ResultStatus.INVALID_FORMAT);
        }

        AuditingDO auditing = new AuditingDO(functionType, actionType, draftContent);
        auditing.setAuditStatus(AuditStatus.AUDITING.getSymbol());
        auditing.setCreator(getUserAccount());
        auditing.setIssuerBankId(getIssuerBankId());
        auditing.setCreateMillis(System.currentTimeMillis());

        if (actionType == AuditActionType.UPDATE || actionType == AuditActionType.ADD) {
            AuditServiceWrapper serviceWrapper = auditServiceMap.get(functionType);
            Optional<AuditFileDTO> attachmentOtp = serviceWrapper.getDraftFileDTO(requestDTO);

            if (attachmentOtp.isPresent()) {
                auditing.setFileContent(attachmentOtp.get().getContent());
                auditing.setFileName(attachmentOtp.get().getName());
            }
        }

        Optional<AuditingDO> addResultOpt = auditingDAO.save(auditing);

        return addResultOpt.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public DataEditResultDTO applyAuditResult(AuditResultDTO resultDTO) {
      Optional<AuditingDO> auditingOpt = auditingDAO.findById(resultDTO.getAuditId());
      if (!auditingOpt.isPresent()) {
          log.error(
            "[applyAuditResult] Failed in get auditing draft content with id={}",
            resultDTO.getAuditId());
          throw new OceanException(ResultStatus.NO_SUCH_DATA);
      }

      AuditingDO auditingData = auditingOpt.get();

      // if request function type is different from function type record in audition draft, response access denied.
      if (!resultDTO.getFunctionType()
        .equalsIgnoreCase(auditingData.getFunctionType().getTypeSymbol())) {
          log.error(
            "[applyAuditResult] Failed in auditing draft due to permission not match. Request audit function={}, Target audit function={}",
              StringUtils.normalizeSpace(resultDTO.getFunctionType()),
              StringUtils.normalizeSpace(auditingData.getFunctionType().getTypeSymbol())
          );
          throw new OceanException(ResultStatus.FORBIDDEN);
      }

      AuditStatus auditStatus = AuditStatus.getStatusBySymbol(resultDTO.getAuditStatus());
      DataEditResultDTO editResultDTO = null;
      try {
          if (auditStatus == AuditStatus.PUBLISHED) {
              editResultDTO = doApprovalProcess(auditingData);
          } else {
              editResultDTO = doReleaseAuditLock(auditingData);
          }
      } catch (IOException | DatabaseException ioe) {
          log.error(
            "[applyAuditResult] Failed in process apply audit result. auditId={}, auditor={}"
            , resultDTO.getAuditId()
            , getUserAccount());
          throw new OceanException(
            "Failed in process apply audit result. AuditId: "
              + resultDTO.getAuditId()
              + ", auditor: "
              + getUserAccount());
      }

      // save auditing result
      auditingData.setUpdater(getUserAccount());
      auditingData.setUpdateMillis(System.currentTimeMillis());
      auditingData.setAuditComment(resultDTO.getComment());
      auditingData.setAuditStatus(resultDTO.getAuditStatus());
      auditingDAO.save(auditingData);

      return editResultDTO;
  }

    private DataEditResultDTO doApprovalProcess(AuditingDO auditingData)
      throws IOException, DatabaseException {
        AuditServiceWrapper serviceWrapper = auditServiceMap.get(auditingData.getFunctionType());
        Optional<AuditableDTO> draftContentOpt =
          serviceWrapper.convertJsonToConcreteDTO(auditingData.getDraftContent());

        if (!draftContentOpt.isPresent()) {
            log.error(
              "[doApprovalProcess] Failed in get draft content as approval process. audit id={}, issuerBankId={}"
              , auditingData.getId()
              , auditingData.getIssuerBankId());
        }

        AuditableDTO draftDTO = draftContentOpt.get();
        AuditableDTO resultDTO = null;

        if (auditingData.getFileContent() != null
          && StringCustomizedUtils.isNotEmpty(auditingData.getFileName())) {
            draftDTO.setFileContent(auditingData.getFileContent());
            draftDTO.setFileName(auditingData.getFileName());
        }

        AuditActionType actionType = AuditActionType.getBySymbol(auditingData.getActionType());
        switch (actionType) {
            case ADD:
                draftDTO.setAuditStatus(AuditStatus.PUBLISHED);
                Optional<AuditableDTO> addResultOpt = serviceWrapper.add(draftDTO);
                resultDTO = addResultOpt.get();
                break;
            case DELETE:
                resultDTO = draftContentOpt.get();
                resultDTO.setAuditStatus(AuditStatus.PUBLISHED);
                DeleteDataDTO deleteDataDTO =
                  new DeleteDataDTO(
                    resultDTO.getId(),
                    resultDTO.getIssuerBankId(),
                    AuditStatus.PUBLISHED,
                    auditingData.getCreator(),
                    resultDTO.getVersion());
                serviceWrapper.delete(deleteDataDTO);
                break;
            case UPDATE:
                draftDTO.setAuditStatus(AuditStatus.PUBLISHED);
                Optional<AuditableDTO> updateResultOpt = serviceWrapper.update(draftDTO);
                resultDTO = updateResultOpt.get();
                break;
            default:
                break;
        }

        return new DataEditResultDTO(resultDTO);
    }

    private DataEditResultDTO doReleaseAuditLock(AuditingDO auditingData)
      throws IOException, DatabaseException {
        AuditServiceWrapper serviceWrapper = auditServiceMap.get(auditingData.getFunctionType());
        Optional<AuditableDTO> draftContentOpt =
          serviceWrapper.convertJsonToConcreteDTO(auditingData.getDraftContent());

        if (!draftContentOpt.isPresent()) {
            log.error(
              "[doReleaseAuditLock] Failed in get draft content as release audit lock. audit id={}, issuerBankId={}"
              , auditingData.getId(), auditingData.getIssuerBankId());
        }

        AuditActionType actionType = AuditActionType.getBySymbol(auditingData.getActionType());
        if (actionType != AuditActionType.UPDATE && actionType != AuditActionType.DELETE) {
            return new DataEditResultDTO(draftContentOpt.get());
        }

        if (!serviceWrapper.unlockDataFromAuditing(draftContentOpt.get())) {
            throw new OceanException(
              "Failed in execute release audit lock process. AuditId: " + auditingData.getId());
        }

        return new DataEditResultDTO(draftContentOpt.get());
    }

  /** 取得待審核項目清單 */
  public PagingResultDTO<AuditContentDTO> getPendingListWithoutSelf(
      Long issuerBankId, AuditFunctionType functionType, Pageable paging) {
      String user = getUserAccount();
      Optional<Page<AuditingDO>> auditingListOpt;

      if (functionType.equals(AuditFunctionType.UNKNOWN)) {
          auditingListOpt = getUserAuditFunctionList().map(
            functionTypeList -> auditingDAO.findByFunctionTypeListAndAuditStatusExcludeSelf(
              issuerBankId, functionTypeList, AuditStatus.AUDITING, user, paging)).orElse(null);
      } else {
          auditingListOpt =
            auditingDAO.findByFunctionTypeAndAuditStatusExcludeSelf(
              issuerBankId, functionType, AuditStatus.AUDITING, user, paging);
      }

      if (auditingListOpt == null || !auditingListOpt.isPresent()) {
          return PagingResultDTO.empty();
      }

      return convertAuditingToPageResult(auditingListOpt.get());
  }


    private PagingResultDTO<AuditContentDTO> convertAuditingToPageResult(
      Page<AuditingDO> auditingPage) {
        List<AuditContentDTO> auditContentDTOList = new ArrayList<>();
        PagingResultDTO<AuditContentDTO> queryResultDTO = PagingResultDTO.valueOf(auditingPage);
        queryResultDTO.setData(auditContentDTOList);
        List<AuditingDO> auditingList = auditingPage.getContent();
        for (AuditingDO auditingItem : auditingList) {
            // skip if there is any broken format and get rest data with correct format
            try {
                Optional<AuditContentDTO> auditContentDTOOpt = convertToAuditContentDTO(
                  auditingItem);
                auditContentDTOOpt.ifPresent(auditContentDTOList::add);
            } catch (JsonProcessingException e) {
                log.error(
                  "[convertAuditingToPageResult] Failed in convert concrete data into json format. auditId={}, issuerBankId={}"
                  , auditingItem.getId()
                  , auditingItem.getIssuerBankId(),
                  e);
            } catch (IOException e) {
                log.error(
                  "[convertAuditingToPageResult] Failed in convert draft into DTO container. draftContent={}"
                  , StringUtils.normalizeSpace(auditingItem.getDraftContent()),
                  e);
            }
        }

        return queryResultDTO;
    }

    private Optional<AuditContentDTO> convertToAuditContentDTO(AuditingDO auditingItem)
      throws IOException {
        AuditServiceWrapper serviceWrapper = getAuditService(auditingItem.getFunctionType());
        AuditContentDTO auditContentDTO = new AuditContentDTO(auditingItem);

        // get draft data object
        Optional<AuditableDTO> draftDataOpt =
          serviceWrapper.convertJsonToConcreteDTO(auditingItem.getDraftContent());
        if (!draftDataOpt.isPresent()) {
            log.error(
              "[convertToAuditContentDTO] Failed in convert draft into DTO container. draftContent={}"
              , StringUtils.normalizeSpace(auditingItem.getDraftContent()));
            return Optional.of(auditContentDTO);
        }
        AuditableDTO targetAuditableDTO = draftDataOpt.get();

        // Attach bank info. Extract target issuer bank id and query info of bank
        Optional<IssuerBankDO> issuerBankOpt = issuerBankDAO
          .findById(targetAuditableDTO.getIssuerBankId());
        issuerBankOpt
          .ifPresent(issuerBank -> auditContentDTO.setIssuerBankName(issuerBank.getName()));

        // Return result if audit content is neither update nor delete. Both Update and Delete have to retrieve original data.
        if (AuditActionType.getBySymbol(auditingItem.getActionType()) != AuditActionType.UPDATE
          && AuditActionType.getBySymbol(auditingItem.getActionType()) != AuditActionType.DELETE) {
            return Optional.of(auditContentDTO);
        }

        // get original data object
        try {
            Optional<AuditableDTO> originalDataOpt = serviceWrapper
              .getOriginalDataDTO(targetAuditableDTO);
            if (!originalDataOpt.isPresent()) {
                log.error(
                  "[convertToAuditContentDTO] Failed in convert original into DTO container. draftContent={}"
                  , StringUtils.normalizeSpace(auditingItem.getDraftContent()));
                return Optional.of(auditContentDTO);
            }

            AuditableDTO originalAuditableDTO = originalDataOpt.get();
            serviceWrapper.filterForAuditUsed(originalAuditableDTO);
            auditContentDTO
              .setOriginalContent(jsonObjMapper.writeValueAsString(originalAuditableDTO));

            Optional<AuditFileDTO> originalFileOpt =
              serviceWrapper.getOriginalFileDTO(originalAuditableDTO);
            originalFileOpt.ifPresent(auditContentDTO::setOriginalFile);

        } catch (NoSuchDataException | DatabaseException e) {
            // 在覆核狀態查詢時，因為Raw Data 已刪除所以撈不到Data，若直接throw NoSuchDataException會造成整個API回傳錯誤
            log.error("[convertToAuditContentDTO] raw data has been deleted", e);
        }

    return Optional.of(auditContentDTO);
  }

  /** 取得使用者的審核權限清單 */
  public Optional<List<AuditFunctionType>> getUserAuditFunctionList() {
    List<AuditFunctionType> auditFunctionList = new ArrayList<>();

    for (AuditFunctionType auditFuncType : AuditFunctionType.values()) {
      if (permissionManager.hasPermission(auditFuncType.getPermissionType())) {
        auditFunctionList.add(auditFuncType);
      }
    }

    return Optional.of(auditFunctionList);
  }

  public PagingResultDTO<AuditContentDTO> getPersonalLog(
      AuditingLogQueryDTO queryDTO, Pageable paging) {
      String user = getUserAccount();

      AuditStatus auditStatus = AuditStatus.getStatusBySymbol(queryDTO.getAuditStatus());
      Optional<Page<AuditingDO>> auditingLogListOpt = auditingDAO.getLogByUser(
        user, auditStatus, queryDTO.getStartTime(), queryDTO.getEndTime(), paging);

      if (auditingLogListOpt == null || !auditingLogListOpt.isPresent()) {
          return PagingResultDTO.empty();
      }

      return convertAuditingToPageResult(auditingLogListOpt.get());
  }
}
