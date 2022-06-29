package com.cherri.acs_kernel.plugin.dto;

import java.util.Map;
import lombok.Data;

/**
 * Authentication Channel
 *
 * @author William, Alan Chen
 */
@Data
public class AuthenticationChannel {

    /**
     * customized id of auth channel,
     * <p>
     * should be unique id in all auth channel which will be took as query condition
     */
    private String id;

    /**
     * Name of authentication channel
     * <p>
     * which will be displayed in the challenge page
     */
    private String name;

    /**
     * If the channel is enable
     */
    private boolean isEnable;

    /**
     * Description of authentication channel which will be displayed in the challenge page
     * <p>
     * Value will be either builtin pattern or customized message
     * builtin template pattern: {{mail}}, {{phone}}
     * pattern will be replaced to masked user info if user data is available
     */
    private String description;

    /**
     * customized channel connection attribute. This Map will be taken as parameter when trigger send authentication
     * <p>
     * e.g.
     * - phone number, then key would be phone and value will be user's cell phone
     * - email, then key would be mail and value will be user's email
     * <p>
     * p.s. Value of these attribute should not be mask, system will mask both mail and phone.
     * If value was mask, then authentication plugin would get mask value.
     */
    private Map<String, String> connectionAttr;
}
