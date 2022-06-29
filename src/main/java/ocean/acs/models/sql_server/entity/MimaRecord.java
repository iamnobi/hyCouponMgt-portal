package ocean.acs.models.sql_server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ocean.acs.models.data_object.entity.MimaRecordDO;
import ocean.acs.models.entity.DBKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MIMA_RECORD)
public class MimaRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DBKey.COL_MIMA_RECORD_ID)
    private Long id;

    /** 銀行代碼 */
    @Column(name = DBKey.COL_MIMA_RECORD_ISSUER_BANK_ID)
    private Long issuerBankId;

    /** 使用者帳號 */
    @Column(name = DBKey.COL_MIMA_RECORD_ACCOUNT)
    private String account;

    /** 加密後密碼 */
    @Column(name = DBKey.COL_MIMA_RECORD_MIMA)
    private String mima;

    /** 建立時間 */
    @Column(name = DBKey.COL_MIMA_RECORD_CREATE_TIME)
    private Long createTime;

    /**
     * Transfer dto to entity
     *
     * @param dto Mima Record Dto
     * @return Mima Record
     */
    public static MimaRecord valueOf(MimaRecordDO dto) {
        MimaRecord mimaRecord = new MimaRecord();
        mimaRecord.setIssuerBankId(dto.getIssuerBankId());
        mimaRecord.setAccount(dto.getAccount());
        mimaRecord.setMima(dto.getEncryptedMima());
        mimaRecord.setCreateTime(System.currentTimeMillis());
        return mimaRecord;
    }

}
