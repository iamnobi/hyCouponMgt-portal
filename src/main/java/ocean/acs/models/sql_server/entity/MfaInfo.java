package ocean.acs.models.sql_server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
