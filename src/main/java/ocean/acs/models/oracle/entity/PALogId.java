package ocean.acs.models.oracle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * for VELog PAReq_received
 */
@Entity
@DynamicUpdate
@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "PA_LOG")
public class PALogId {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "UUID")
  private String id;

  @OneToOne(optional = false)
  @JoinColumn(name = "VE_LOG_ID")
  private VELog veLog;
}
