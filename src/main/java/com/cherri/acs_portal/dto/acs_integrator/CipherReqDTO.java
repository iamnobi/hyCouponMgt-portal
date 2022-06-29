package com.cherri.acs_portal.dto.acs_integrator;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CipherReqDTO {

	@Getter
	private Set<CipherData> data = new HashSet<>();

	public CipherReqDTO addData(String key, String value) {
		if (StringUtils.isNotBlank(value)) data.add(new CipherData(key, value));
		return this;
	}

}