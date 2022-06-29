package ocean.acs.models.data_object.entity;

import lombok.*;
import ocean.acs.commons.enumerator.ChallengeViewErrorMessageCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChallengeViewErrorMessageDO extends OperatorInfoDO {

    private Long id;

    private String languageCode;

    private ChallengeViewErrorMessageCategory errorCategory;

    private String errorTitle;

    private String errorMessage;

    public ChallengeViewErrorMessageDO(
            Long id,
            String languageCode,
            ChallengeViewErrorMessageCategory errorCategory,
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

    public static ChallengeViewErrorMessageDO valueOf(
            ocean.acs.models.oracle.entity.ChallengeViewErrorMessage e) {
        return new ChallengeViewErrorMessageDO(
                e.getId(),
                e.getLanguageCode(),
                ChallengeViewErrorMessageCategory.valueOf(e.getErrorCategory()),
                e.getErrorTitle(),
                e.getErrorMessage(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }

    public static ChallengeViewErrorMessageDO valueOf(
            ocean.acs.models.sql_server.entity.ChallengeViewErrorMessage e) {
        return new ChallengeViewErrorMessageDO(
                e.getId(),
                e.getLanguageCode(),
                ChallengeViewErrorMessageCategory.valueOf(e.getErrorCategory()),
                e.getErrorTitle(),
                e.getErrorMessage(),
                e.getCreator(),
                e.getCreateMillis(),
                e.getUpdater(),
                e.getUpdateMillis(),
                e.getDeleteFlag(),
                e.getDeleter(),
                e.getDeleteMillis());
    }
}
