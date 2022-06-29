package ocean.acs.models.oracle.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/** The persistent class for the PREPARATION_LOG database table. */
@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "PREPARATION_LOG")
public class PreparationLog implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GenericGenerator(
      name = "PREPARATION_LOG_ID_GENERATOR",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @org.hibernate.annotations.Parameter(
            name = "sequence_name",
            value = "PREPARATION_LOG_ID_SEQ"),
        @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
        @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
      })
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PREPARATION_LOG_ID_GENERATOR")
  private Long id;

  @Column(name = "THREE_D_S_SERVER_TRANS_ID")
  private String threeDSServerTransID;

  @Column(name = "DS_END_PROTOCOL_VERSION")
  private String dsEndProtocolVer;

  @Column(name = "DS_START_PROTOCOL_VERSION")
  private String dsStartProtocolVer;

  @Column(name = "MESSAGE_VERSION")
  private String messageVersion;

  @Column(name = "SERIAL_NUM")
  private String serialNum;

  @Column(name = "THREE_D_S_SERVER_OPERATOR_ID")
  private String threeDSServerOperatorID;

  @Column(name = "THREE_D_S_SERVER_REF_NUMBER")
  private String threeDSServerRefNumber;

  @Column(name = "DS_TRANS_ID")
  private String dsTransID;

  @Column(name = "ERROR_MESSAGE_LOG_ID")
  private Long errorMessageLogId;

  @Column(name = "PRES_MESSAGE_VERSION")
  private String presMessageVersion;

  @Column(name = "PRES_SERIAL_NUM")
  private String presSerialNum;

  @NonNull
  @Column(name = "SYS_CREATOR")
  private String sysCreator;

  @NonNull
  @Column(name = "CREATE_MILLIS")
  private Long createMillis;

  @Column(name = "SYS_UPDATER")
  private String sysUpdater;

  @Column(name = "UPDATE_MILLIS")
  private Long updateMillis;

  @OneToOne(cascade = CascadeType.ALL)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name="ERROR_MESSAGE_LOG_ID", insertable = false, updatable = false)
  private ErrorMessageLog errorMessageLog;
}
