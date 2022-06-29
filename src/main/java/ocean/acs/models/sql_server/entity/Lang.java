package ocean.acs.models.sql_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.LangDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_LANG)
public class Lang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_LANG_ID)
    private Long id;

    @Column(name = DBKey.COL_LANG_LANGUAGE_CODE)
    private String languageCode;

    @Column(name = DBKey.COL_LANG_DISPLAY_TEXT)
    private String displayText;

    @Column(name = DBKey.COL_LANG_IS_DEFAULT)
    private boolean isDefault;

    public static Lang valueOf(LangDO d) {
        return new Lang(d.getId(), d.getLanguageCode(), d.getDisplayText(), d.isDefault());
    }
}
