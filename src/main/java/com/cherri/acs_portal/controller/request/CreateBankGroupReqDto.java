package com.cherri.acs_portal.controller.request;

import com.cherri.acs_portal.aspect.AuditLogAspect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateBankGroupReqDto {

	@NotNull(message = "{column.notempty}") 
	@JsonProperty("issuerBankId")
	private Long issuerBankId;

	@NotNull(message = "{column.notempty}") 
	@JsonProperty("name")
	private String name;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, AuditLogAspect.AUDIT_LOG_STYLE);
	}

}
