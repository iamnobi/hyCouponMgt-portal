package com.cherri.acs_portal.dto.usermanagement;

import com.cherri.acs_portal.dto.audit.DeleteDataDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.commons.enumerator.AuditStatus;
import ocean.acs.commons.enumerator.ThreeDsVersion;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class DeleteGroupMemberDto extends DeleteDataDTO {

	private static final long serialVersionUID = 1L;

	private String accountName;
	private String groupName;

	private DeleteGroupMemberDto(DeleteGroupMemberDto.Builder builder) {
		super(builder.id, builder.issuerBankId, builder.auditStatus, builder.user, ThreeDsVersion.TWO.getCode());
		this.accountName = builder.accountName;
		this.groupName = builder.groupName;
	}

	public static DeleteGroupMemberDto.Builder getBuilder() {
		return new DeleteGroupMemberDto.Builder();
	}

	public static class Builder {
		private Long id;
		private Long issuerBankId;
		private AuditStatus auditStatus;
		private String user;
		private String accountName;
		private String groupName;

		public DeleteGroupMemberDto.Builder id(Long id) {
			this.id = id;
			return this;
		}

		public DeleteGroupMemberDto.Builder issuerBankId(Long issuerBankId) {
			this.issuerBankId = issuerBankId;
			return this;
		}

		public DeleteGroupMemberDto.Builder auditStatus(AuditStatus auditStatus) {
			this.auditStatus = auditStatus;
			return this;
		}

		public DeleteGroupMemberDto.Builder user(String user) {
			this.user = user;
			return this;
		}

		public DeleteGroupMemberDto.Builder accountName(String accountName) {
			this.accountName = accountName;
			return this;
		}

		public DeleteGroupMemberDto.Builder groupName(String groupName) {
			this.groupName = groupName;
			return this;
		}

		public DeleteGroupMemberDto build() {
			return new DeleteGroupMemberDto(this);
		}
	}

}
