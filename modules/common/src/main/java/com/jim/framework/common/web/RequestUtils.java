package com.jim.framework.common.web;

import com.jim.framework.common.util.RegExps;
import com.jim.framework.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtils {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    private static final String reg = "(10|172|192|127)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";

    private static final Pattern INTRANET_PATTERN = Pattern.compile(reg);

    private static final Map<String, String> DEVICES = new TreeMap<>();

    private static Set<String> deviceTypes;

    private static final String DEFAULT_DEVICE = "pc";

    static {
        DEVICES.put("ipad", "ipad");
        DEVICES.put("iphone os", "ios");
        DEVICES.put("android", "android");
        DEVICES.put("windows mobile", "wp");    //win phone
        deviceTypes = DEVICES.keySet();
    }

    private RequestUtils() {
    }

    public static String getDomainFromRequest(HttpServletRequest request) {
        return request.getServerName();
    }

    public static String getTopDomainFromRequest(HttpServletRequest request) {
        String domain = getDomainFromRequest(request);
        Matcher matcher = RegExps.TOP_DOMAIN_PATTERN.matcher(domain);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return domain;
    }

    public static boolean isRequestFromIntranet(HttpServletRequest request) {
        String ip = getRealIpAddr(request);
        return INTRANET_PATTERN.matcher(ip).find();
    }

    public static String getRealIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getUserAgentSummary(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    public static String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getId();
        } else {
            return null;
        }
    }

    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(name);
        } else {
            return null;
        }
    }

    public static String detectDevice(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeadersConstant.USER_AGENT);
        if (StringUtils.hasText(userAgent)) {
            userAgent = userAgent.toLowerCase();
            for (String type : deviceTypes) {
                if (userAgent.indexOf(type) >= 0) {
                    return DEVICES.get(type);
                }
            }
            return DEFAULT_DEVICE;
        }
        return null;
    }
}
