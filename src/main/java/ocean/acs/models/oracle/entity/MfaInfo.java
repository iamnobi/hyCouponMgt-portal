package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ocean.acs.models.entity.DBKey;

/**
 * Mfa Info
 *
 * @author Alan Chen
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DBKey.TABLE_MFA_INFO)
public class MfaInfo {

    /**
     * Id
     */
    @Id
    @SequenceGenerator(name = "SQ_MFA_INFO", sequenceName = "MFA_INFO_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MFA_INFO")
    @Column(name = DBKey.COL_MFA_INFO_ID)
    private Long id;

    /**
     * BankID 銀行代碼
     */
    @Column(name = DBKey.COL_MFA_INFO_ISSUER_BANK_ID)
    private Long issuerBankId;

    /**
     * Account 使用者帳號
     */
    @Column(name = DBKey.COL_MFA_INFO_ACCOUNT, columnDefinition = "VARCHAR(50)")
    private String account;

    /**
     * Secret Key 密鑰
     */
    @Column(name = DBKey.COL_MFA_INFO_SECRET_KEY, columnDefinition = "VARCHAR(255)")
    private String secretKey;
}
