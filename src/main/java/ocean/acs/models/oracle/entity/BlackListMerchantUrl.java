package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import ocean.acs.models.data_object.portal.IssuerBankRelativeData;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ocean.acs.commons.enumerator.TransStatus;
import ocean.acs.models.data_object.entity.BlackListMerchantUrlDO;
import ocean.acs.models.data_object.portal.PortalBlackListMerchantUrlDO;
import ocean.acs.models.entity.DBKey;
import ocean.acs.models.entity.OperatorInfo;

@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = DBKey.TABLE_BLACK_LIST_MERCHANT_URL)
public class BlackListMerchantUrl extends OperatorInfo implements IssuerBankRelativeData {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "black_list_merchant_url_seq_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name",
                            value = "BLACK_LIST_MERCHANT_URL_ID_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "black_list_merchant_url_seq_generator")
    @Column(name = DBKey.COL_BLACK_LIST_MERCHANT_URL_ID)
    private Long id;

    @Column(name = DBKey.COL_BLACK_LIST_MERCHANT_URL_ISSUER_BANK_ID)
    private Long issuerBankId;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_MERCHANT_URL_URL)
    private String url;

    @NonNull
    @Column(name = DBKey.COL_BLACK_LIST_MERCHANT_URL_TRANS_STATUS)
    private String transStatus;

    @Column(name = DBKey.COL_AUDIT_STATUS)
    private String auditStatus;

    public BlackListMerchantUrl(Long id, Long issuerBankId, String url, String transStatus,
            String auditStatus, String creator, Long createMillis, String updater,
            Long updateMillis, Boolean deleteFlag, String deleter, Long deleteMillis) {
        super(creator, createMillis, updater, updateMillis, deleteFlag, deleter, deleteMillis);
        this.id = id;
        this.issuerBankId = issuerBankId;
        this.url = url;
        this.transStatus = transStatus;
        this.auditStatus = auditStatus;
    }

    public static BlackListMerchantUrl valueOf(BlackListMerchantUrlDO d) {
        return new BlackListMerchantUrl(d.getId(), d.getIssuerBankId(), d.getUrl(),
                d.getTransStatus(), d.getAuditStatus(), d.getCreator(), d.getCreateMillis(),
                d.getUpdater(), d.getUpdateMillis(), d.getDeleteFlag(), d.getDeleter(),
                d.getDeleteMillis());
    }

    public static BlackListMerchantUrl valueOf(PortalBlackListMerchantUrlDO d, TransStatus transStatus) {
        return new BlackListMerchantUrl(null, d.getIssuerBankId(), d.getMerchantUrl(),
                transStatus.getCode(), d.getAuditStatus().getSymbol(), d.getCreator(),
                System.currentTimeMillis(), null, null, Boolean.FALSE, null, null);
    }

}
