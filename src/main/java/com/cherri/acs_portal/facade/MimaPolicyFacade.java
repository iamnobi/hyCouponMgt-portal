package com.cherri.acs_portal.facade;

import com.cherri.acs_portal.controller.request.MimaPolicyCreateRequest;
import com.cherri.acs_portal.controller.request.MimaPolicyUpdateRequest;
import com.cherri.acs_portal.controller.response.MimaPolicyQueryResponse;
import com.cherri.acs_portal.controller.validator.impl.MimaPolicyCreateValidator;
import com.cherri.acs_portal.controller.validator.impl.MimaPolicyUpdateValidator;
import com.cherri.acs_portal.dto.ApiResponse;
import com.cherri.acs_portal.dto.mima.MimaPolicyDto;
import com.cherri.acs_portal.service.MimaPolicyService;
import com.google.gson.JsonObject;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.commons.enumerator.ResultStatus;
import ocean.acs.commons.exception.OceanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mima Policy Facade
 *
 * @author Alan Chen
 */
@Slf4j
@Service
public class MimaPolicyFacade {

    private final MimaPolicyService mimaPolicyService;
    private final MimaPolicyUpdateValidator mimaPolicyUpdateValidator;
    private final MimaPolicyCreateValidator mimaPolicyCreateValidator;

    @Autowired
    public MimaPolicyFacade(MimaPolicyService mimaPolicyService,
      MimaPolicyUpdateValidator mimaPolicyUpdateValidator,
      MimaPolicyCreateValidator mimaPolicyCreateValidator) {
        this.mimaPolicyService = mimaPolicyService;
        this.mimaPolicyUpdateValidator = mimaPolicyUpdateValidator;
        this.mimaPolicyCreateValidator = mimaPolicyCreateValidator;
    }

    /**
     * 取得UI欄位基本檢核條件
     *
     * @return 檢核條件Json {@link ApiResponse<JsonObject>}
     */
    public ApiResponse<Map<String, Object>> getColumnBaseRule() {
        return new ApiResponse<>(mimaPolicyService.getColumnBaseRule());
    }

    /**
     * 查詢密碼管理原則
     *
     * @param issuerBankId 銀行代碼
     * @return 已存在密碼管理原則 {@link ApiResponse} {@link MimaPolicyQueryResponse}
     */
    public ApiResponse<MimaPolicyQueryResponse> query(Long issuerBankId) {
        try {
            return issuerBankIdIsNull(issuerBankId)
              ? new ApiResponse<>(ResultStatus.ILLEGAL_ARGUMENT)
              : new ApiResponse<>(
                MimaPolicyQueryResponse.valueOf(mimaPolicyService.query(issuerBankId)));
        } catch (OceanException oe) {
            return new ApiResponse<>(oe.getResultStatus());
        } catch (Exception e) {
            return new ApiResponse<>(ResultStatus.SERVER_ERROR);
        }
    }

    /**
     * 更新密碼管理原則
     *
     * @param request {@link MimaPolicyUpdateRequest} 密碼原則管理更新請求物件
     * @return 是否成功 {@link ApiResponse<Boolean>}
     */
    public ApiResponse<Boolean> update(MimaPolicyUpdateRequest request, Long issuerBankId) {
        try {
            if (requestIsNotValid(request))
                return new ApiResponse<>(ResultStatus.ILLEGAL_ARGUMENT);

            return policyIsNotExist(request.getId())
              ? new ApiResponse<>(ResultStatus.NO_SUCH_DATA)
              : new ApiResponse<>(
                mimaPolicyService.update(MimaPolicyDto.valueOf(request, issuerBankId)));
        } catch (OceanException oe) {
            return new ApiResponse<>(oe.getResultStatus());
        } catch (Exception e) {
            return new ApiResponse<>(ResultStatus.SERVER_ERROR);
        }
    }

    private boolean requestIsNotValid(MimaPolicyUpdateRequest request) {
        return !mimaPolicyUpdateValidator.isValid(request);
    }

    private boolean createRequestIsNotValid(MimaPolicyCreateRequest request) {
        return !mimaPolicyCreateValidator.isValid(request);
    }

    private boolean policyIsNotExist(long id) {
        return !mimaPolicyService.isPolicyExistById(id);
    }

    private boolean policyIsExist(long issuerBankId) {
        return mimaPolicyService.isPolicyExistByIssuerBankId(issuerBankId);
    }

    private boolean issuerBankIdIsNull(Long issuerBankId) {
        return issuerBankId == null;
    }
}
