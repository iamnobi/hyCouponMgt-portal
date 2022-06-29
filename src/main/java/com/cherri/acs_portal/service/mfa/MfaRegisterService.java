package com.cherri.acs_portal.service.mfa;

import com.cherri.acs_portal.dto.mfa.BaseMfaInfo;
import com.cherri.acs_portal.dto.mfa.MfaRegisterRequestDto;
import com.cherri.acs_portal.util.QrCodeUtils;
import com.google.common.io.BaseEncoding;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.extern.slf4j.Slf4j;
import ocean.acs.models.dao.MfaDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alan Chen
 */
@Slf4j
@Service
public class MfaRegisterService {

    private final MfaDAO mfaDao;

    @Autowired
    public MfaRegisterService(MfaDAO mfaDao) {
        this.mfaDao = mfaDao;
    }

    public boolean isRegistered(BaseMfaInfo requestDto) {
        return mfaDao.isRegistered(requestDto.getIssuerBankId(), requestDto.getAccount());
    }

    public Byte[] register(MfaRegisterRequestDto requestDto) throws Exception {
        String secretKey = generateSecretKey();
        mfaDao.save(requestDto.getIssuerBankId(), requestDto.getAccount(), secretKey);
        return generateQrCodeByteArray(requestDto, secretKey);
    }

    public Byte[] getExistQrCode(MfaRegisterRequestDto requestDto) throws Exception {
        log.info("[MfaRegisterService][getExistQrCode] Get exist QR code!!");
        String secretKey = mfaDao
          .findSecretKey(requestDto.getIssuerBankId(), requestDto.getAccount());
        if (StringUtils.isBlank(secretKey)) {
            log.info(
              "[MfaRegisterService][getExistQrCode] Secret key is blank. then generate new secret key!!");
            secretKey = generateSecretKey();
            mfaDao.update(requestDto.getIssuerBankId(), requestDto.getAccount(), secretKey);
        }
        return generateQrCodeByteArray(requestDto, secretKey);
    }

    private String generateSecretKey() {
        log.info("[MfaRegisterService][generateSecretKey] Generate secret key");
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String secretKey = BaseEncoding.base32().encode(bytes);
        return secretKey.toLowerCase();
    }

    private Byte[] generateQrCodeByteArray(MfaRegisterRequestDto requestDto, String secretKey)
      throws Exception {
        log.info("[MfaRegisterService][generateQrCodeByteArray] Generate QrCode String.");
        String qrCodeData = createGoogleAuthQRCodeData(
          secretKey,
          requestDto.getAccount(),
          requestDto.getIssuerBankId().toString()
        );

        return QrCodeUtils.createQRCode(qrCodeData, 300, 300, 0);
    }

    private String createGoogleAuthQRCodeData(String secret, String account, String issuer) {
        try {
            String encodePath = encode(issuer + ":" + account);
            String encodeSecret = encode(secret);
            String encodeIssuer = encode(issuer);

            String qrCodeData = "otpauth://totp/%s?secret=%s&issuer=%s";
            return String.format(qrCodeData, encodePath, encodeSecret, encodeIssuer);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    private String encode(String plainText) throws UnsupportedEncodingException {
        return URLEncoder.encode(plainText, StandardCharsets.UTF_8.name()).replace("+", "%20");
    }
}

