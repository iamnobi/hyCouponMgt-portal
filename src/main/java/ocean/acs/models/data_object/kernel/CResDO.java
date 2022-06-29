package ocean.acs.models.data_object.kernel;

import java.awt.Image;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ocean.acs.commons.enumerator.MessageType;
import ocean.acs.commons.enumerator.TransStatus;

@Setter
@Getter
@ToString
public class CResDO {

    private String threeDSServerTransID;
    private String acsCounterAtoS;
    private String acsTransID;
    private String acsHTML;
    private String acsUiType;
    private String challengeAddInfo;
    private String challengeCompletionInd;
    private String challengeInfoHeader;
    private String challengeInfoLabel;
    private String challengeInfoText;
    private String challengeInfoTextIndicator;
    private List<Map<String, String>> challengeSelectInfo;
    private String expandInfoLabel;
    private String expandInfoText;
    private Image issuerImage;
    private List<MessageExtensionDO> messageExtension;
    private String messageType;
    private String messageVersion;
    private String oobAppURL;
    private String oobAppLabel;
    private String oobContinueLabel;
    private Image psImage;
    private String resendInformationLabel;
    private String sdkTransID;
    private String submitAuthenticationLabel;
    private String transStatus;
    private String whyInfoLabel;
    private String whyInfoText;

    public CResDO() {
        this.messageType = MessageType.CRes.name();
    }

    public static CResDO newInstanceForBrowser(String threeDSServerTransID, String acsTransID,
            String messageVersion, TransStatus transStatus) {
        CResDO cRes = new CResDO();
        cRes.setThreeDSServerTransID(threeDSServerTransID);
        cRes.setAcsTransID(acsTransID);
        cRes.setMessageVersion(messageVersion);
        cRes.setTransStatus(transStatus.getCode());
        return cRes;
    }
}
