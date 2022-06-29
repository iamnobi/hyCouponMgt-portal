package com.cherri.acs_portal.util;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;
import com.googlecode.ipv6.IPv6Network;
import inet.ipaddr.IPAddressString;
import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    public static String getIPFromRequest(HttpServletRequest request) {
        final String clientIP;
        if (request.getHeader("api-gateway-source-ip") != null
          && !request.getHeader("api-gateway-source-ip").matches("")
          && !request.getHeader("api-gateway-source-ip").matches("null")) {
            // From API gateway
            clientIP = request.getHeader("api-gateway-source-ip").split(",")[0];
        } else if (request.getHeader("x-forwarded-for") != null
          && !request.getHeader("x-forwarded-for").matches("")
          && !request.getHeader("x-forwarded-for").matches("null")) {
            // From WAF or load balancer
            clientIP = request.getHeader("x-forwarded-for").split(",")[0];
        } else {
            clientIP = request.getRemoteAddr();
        }
        return clientIP;
    }

    public static boolean isIPv4(final String ip) {
        return new IPAddressString(ip).isIPv4();
    }

    public static boolean isIPv6(final String ip) {
        return new IPAddressString(ip).isIPv6();
    }

    /**
     * @param ip [Format: ip(1.2.3.4) or ip+cidr(1.2.3.4/32)]
     * @return IPv6 string
     * @throws IllegalArgumentException
     */
    public static String ip4To6(final String ip) throws IllegalArgumentException {
        final IPAddressString ipAddressString = new IPAddressString(ip);
        if (ipAddressString.isIPv4()) {
            return ipAddressString.getAddress().toIPv4().getIPv4MappedAddress().toString();
        }
        throw new IllegalArgumentException("The IP:" + ip + " is not IPv4.");
    }

    /**
     * @param ip [Format: ip(::ffff:102:304) or ip+cidr(::ffff:102:304/128)]
     * @return IPv4 string
     * @throws IllegalArgumentException (輸入值非IPv6)
     * @throws NullPointerException     (輸入值IPv6大於IPv4能表達的範圍)
     */
    public static String ip6To4(final String ip)
      throws IllegalArgumentException, NullPointerException {
        final IPAddressString ipAddressString = new IPAddressString(ip);
        if (ipAddressString.isIPv6()) {
            return ipAddressString.getAddress().toIPv4().toString();
        }
        throw new IllegalArgumentException("The IP:" + ip + " is not IPv6.");
    }

    /**
     * @param ipCidr1 (待檢查的值)
     * @param ipCidr2 (範圍值)
     * @return
     */
    public static boolean inRange(String ipCidr1, String ipCidr2) {
        IPv6Network ipCidr1Ipv6 = IPv6Network.fromString(ipCidr1);
        IPv6AddressRange checkValue =
          IPv6AddressRange.fromFirstAndLast(
            IPv6Address.fromString(ipCidr1Ipv6.getFirst().toString()),
            IPv6Address.fromString(ipCidr1Ipv6.getLast().toString()));

        IPv6Network ipCidr2Ipv6 = IPv6Network.fromString(ipCidr2);
        IPv6AddressRange range =
          IPv6AddressRange.fromFirstAndLast(
            IPv6Address.fromString(ipCidr2Ipv6.getFirst().toString()),
            IPv6Address.fromString(ipCidr2Ipv6.getLast().toString()));
        return range.contains(checkValue);
    }
}
