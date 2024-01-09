package org.server.utils;

import java.util.Arrays;
import java.util.List;

public final class SecurityIpHelper {
    private static final String SEPARATOR = ",";
    private static final String WILDCARD = "*";
    private static final String RANGE_SEPARATOR = "-";

    private static final String IP_REGEX = "((([0-9]+)-([0-9]+))|([0-9*]+)).((([0-9]+)-([0-9]+))|([0-9*]+)).((([0-9]+)-([0-9]+))|([0-9*]+)).((([0-9]+)-([0-9]+))|([0-9*]+))";

    private static boolean validateIp(String ip) {
        return ip.matches(IP_REGEX);
    }

    private static boolean validateIpList(List<String> ipList) {
        for (String ip : ipList) {
            if (!validateIp(ip)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateIpList(String ips) {
        return validateIpList(Arrays.asList(ips.split(SEPARATOR)));
    }

    public static List<String> toList(String ips) {
        return Arrays.asList(ips.split(SEPARATOR));
    }

    private final List<String> ipList;

    public SecurityIpHelper(String ips) {
        ipList = toList(ips);
    }

    public boolean match(String ip) {
        for (String ipPattern : ipList) {
            if (match(ipPattern, ip)) {
                return true;
            }
        }
        return false;
    }

    private boolean match(String ipPattern, String ip) {
        String[] ipPatterns = ipPattern.split("\\.");
        String[] ips = ip.split("\\.");
        for (int i = 0; i < ipPatterns.length; i++) {
            if (!matchPart(ipPatterns[i], ips[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean matchPart(String patternPart, String part) {
        if (patternPart.equals(WILDCARD)) {
            return true;
        } else if (patternPart.equals(part)) {
            return true;
        } else if (patternPart.contains(RANGE_SEPARATOR)) {
            String[] rangeStr = patternPart.split(RANGE_SEPARATOR);
            Integer[] range = {Integer.valueOf(rangeStr[0]), Integer.valueOf(rangeStr[1])};
            Integer partNum = Integer.valueOf(part);
            return partNum >= range[0] && partNum <= range[1];
        } else {
            return false;
        }
    }
}
