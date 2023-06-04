package org.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class IpUtil {
    /**
     * 獲取本地IP
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
                while (ipAddresses.hasMoreElements()) {
                    InetAddress inetAddress = ipAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 獲取公共IP
     */
    public static String getPublicIpAddress() {
        try {
            URL url = new URL("https://api.ipify.org");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress = reader.readLine();
            reader.close();
            return ipAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
