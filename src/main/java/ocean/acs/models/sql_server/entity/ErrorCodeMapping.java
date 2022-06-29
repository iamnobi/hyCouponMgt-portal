package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.entity.DBKey;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_ERROR_CODE_MAPPING)
public class ErrorCodeMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_ID)
    private Long id;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_ERROR_GROUP_ID)
    private Long errorGroupId;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_CODE)
    private String code;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_MSG)
    private String message;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_SYS_CREATOR)
    private String sysCreator;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_CREATE_MILLIS)
    private Long createMillis;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_UPDATER)
    private String updater;

    @Column(name = DBKey.COL_ERROR_CODE_MAPPING_UPDATE_MILLIS)
    private Long updateMillis;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DBKey.COL_ERROR_CODE_MAPPING_ERROR_GROUP_ID, insertable = false,
            updatable = false)
    private ErrorIssueGroup issueGroup;

    //
    // public static ErrorCodeMapping valueOf(ErrorCodeMappingDTO mappingDTO)
    // {
    // ErrorCodeMapping codeMapping = new ErrorCodeMapping();
    // codeMapping.setId(mappingDTO.getId());
    // codeMapping.setCode(mappingDTO.getErrorCode());
    // codeMapping.setMessage(mappingDTO.getErrorMsg());
    //
    // return codeMapping;
    // }

}
