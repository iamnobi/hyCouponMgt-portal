package com.cherri.acs_portal.dto.certificate;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

@Log4j2
@Builder
public class RsaKeyDto {

	@Getter
	private String privateExponentHex;
	@Getter
	private String modulusHex;
	@Getter
	private String publicExponentHex;
	
	public PublicKey findRsaPublicKey(){
		final RSAPublicKeySpec rsaPublicKeySpec =
				new RSAPublicKeySpec(new BigInteger(modulusHex, 16), new BigInteger(publicExponentHex, 16));
		try {
			return KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			log.error("error = " + e.getMessage() , e);
		}
		return null;
	}
	
}
