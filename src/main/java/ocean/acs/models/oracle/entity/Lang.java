package ocean.acs.models.oracle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.LangDO;
import ocean.acs.models.entity.DBKey;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(
            name = "lang_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "C_V_E_M_ID_SEQ"),
            })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lang_seq_generator")
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
