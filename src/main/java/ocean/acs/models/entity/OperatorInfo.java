package ocean.acs.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import ocean.acs.models.data_object.entity.OperatorInfoDO;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class OperatorInfo implements Serializable  {

	private static final long serialVersionUID = 1L;

	@NonNull
	@Column(name = DBKey.COL_CREATOR)
	private String creator;

	@NonNull
	@Column(name = DBKey.COL_CREATE_MILLIS)
	private Long createMillis;

	@Column(name = DBKey.COL_UPDATER)
	private String updater;

	@Column(name = DBKey.COL_UPDATE_MILLIS)
	private Long updateMillis;

	@Column(name = DBKey.COL_DELETE_FLAG)
	private Boolean deleteFlag = false;

	@Column(name = DBKey.COL_DELETER)
	private String deleter;

	@Column(name = DBKey.COL_DELETE_MILLIS)
	private Long deleteMillis;

	public void setOperatorInfo(OperatorInfoDO o) {
		creator = o.getCreator();
		createMillis = o.getCreateMillis();
		updater = o.getUpdater();
		updateMillis = o.getUpdateMillis();
		deleteFlag = o.getDeleteFlag();
		deleter = o.getDeleter();
		deleteMillis = o.getDeleteMillis();
	}

}
