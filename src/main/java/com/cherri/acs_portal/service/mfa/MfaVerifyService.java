package com.cherri.acs_portal.service.mfa;

import com.cherri.acs_portal.dto.mfa.MfaVerifyRequestDto;

/**
 * MfaVerifyService
 *
 * @author Alan Chen
 */
public interface MfaVerifyService {

    /**
     * Verify OTP
     *
     * @param requestDto request data transfer object
     * @return is correct
     */
    Boolean verify(MfaVerifyRequestDto requestDto);
}
