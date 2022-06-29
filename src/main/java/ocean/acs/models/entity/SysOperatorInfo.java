package ocean.acs.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class SysOperatorInfo implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@NonNull
	@Column(name = DBKey.COL_SYS_CREATOR)
	private String sysCreator;

	@NonNull
	@Column(name = DBKey.COL_CREATE_MILLIS)
	private Long createMillis;

	@Column(name = DBKey.COL_SYS_UPDATER)
	private String sysUpdater;

	@Column(name = DBKey.COL_UPDATE_MILLIS)
	private Long updateMillis;
}
