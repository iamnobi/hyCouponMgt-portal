package ocean.acs.models.data_object.kernel;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CReqDO {

	@JsonProperty("threeDSServerTransID")
	private String threeDSServerTransID;

	@JsonProperty("acsTransID")
	private String acsTransID;

	@JsonProperty("challengeCancel")
	private String challengeCancel;

	@JsonProperty("challengeDataEntry")
	private String challengeDataEntry;

	@JsonProperty("challengeHTMLDataEntry")
	private String challengeHTMLDataEntry;

	@JsonProperty("challengeWindowSize")
	private String challengeWindowSize;

	@JsonProperty("messageExtension")
	private List<MessageExtensionDO> messageExtension;

	@JsonProperty("messageType")
	private String messageType;

	@JsonProperty("messageVersion")
	private String messageVersion;

	@JsonProperty("oobContinue")
	private Boolean oobContinue;

	@JsonProperty("resendChallenge")
	private String resendChallenge;

	@JsonProperty("sdkTransID")
	private String sdkTransID;

	@JsonProperty("sdkCounterStoA")
	private String sdkCounterStoA;

}
