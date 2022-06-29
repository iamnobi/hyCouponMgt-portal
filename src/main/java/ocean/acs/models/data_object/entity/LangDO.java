package ocean.acs.models.data_object.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LangDO {

    private Long id;

    private String languageCode;

    private String displayText;

    private boolean isDefault;

    public static LangDO valueOf(ocean.acs.models.oracle.entity.Lang e) {
        return new LangDO(e.getId(), e.getLanguageCode(), e.getDisplayText(), e.isDefault());
    }

    public static LangDO valueOf(ocean.acs.models.sql_server.entity.Lang e) {
        return new LangDO(e.getId(), e.getLanguageCode(), e.getDisplayText(), e.isDefault());
    }
}
