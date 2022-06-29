package ocean.acs.commons.enumerator;

import java.util.Locale;

public enum MessageType {

    AReq, 
    ARes, 
    CReq, 
    CRes, 
    PReq, 
    PRes, 
    RReq, 
    RRes, 
    Erro,
    /** Not an EMVco Spec.* */
    General,
    ThreeDSMethod, 
    Portal;

    public static MessageType getByName(String messageTypeStr) throws IllegalArgumentException {
        if (messageTypeStr == null) {
            return null;
        }
        switch (messageTypeStr.toLowerCase(Locale.getDefault())) {
            case "areq":
                return AReq;
            case "ares":
                return ARes;
            case "creq":
                return CReq;
            case "cres":
                return CRes;
            case "rreq":
                return RReq;
            case "rres":
                return RRes;
            case "erro":
                return Erro;
            case "threedsmethod":
                return ThreeDSMethod;
            case "portal":
                return Portal;
            default:
                throw new IllegalArgumentException(
                        String.format("MessageType:%s not supported.", messageTypeStr));
        }
    }

    public static boolean isAReq(String messageType) {
        return MessageType.AReq.name().equals(messageType);
    }

    public static boolean isErro(String messageType) {
        return MessageType.Erro.name().equals(messageType);
    }

}
