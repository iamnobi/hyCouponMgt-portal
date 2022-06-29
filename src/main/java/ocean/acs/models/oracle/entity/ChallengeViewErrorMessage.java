package ocean.acs.models.oracle.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.ChallengeViewErrorMessageDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_CHALLENGE_VIEW_ERROR_MESSAGE)
public class ChallengeViewErrorMessage extends OperatorInfo {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "challenge_view_error_message_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "C_V_E_M_ID_SEQ")
            })
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "challenge_view_error_message_seq_generator")
    @Column(name = DBKey.COL_CHALLENGE_VIEW_ERROR_MESSAGE_ID)
    private Long id;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_ERROR_MESSAGE_LANGUAGE_CODE)
    private String languageCode;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_CATEGORY)
    private String errorCategory;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_TITLE)
    private String errorTitle;

    @Column(name = DBKey.COL_CHALLENGE_VIEW_ERROR_MESSAGE_ERROR_MESSAGE)
    private String errorMessage;

    public ChallengeViewErrorMessage(
            Long id,
            String languageCode,
            String errorCategory,
            String errorTitle,
            String errorMessage,
            String creator,
            Long createMillis,
            String updater,
            Long updateMillis,
            Boolean deleteFlag,
            String deleter,
            Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.languageCode = languageCode;
        this.errorCategory = errorCategory;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public static ChallengeViewErrorMessage valueOf(ChallengeViewErrorMessageDO d) {
        return new ChallengeViewErrorMessage(
                d.getId(),
                d.getLanguageCode(),
                d.getErrorCategory().name(),
                d.getErrorTitle(),
                d.getErrorMessage(),
                d.getCreator(),
                d.getCreateMillis(),
                d.getUpdater(),
                d.getUpdateMillis(),
                d.getDeleteFlag(),
                d.getDeleter(),
                d.getDeleteMillis());
    }
}
