package com.qg.utils;


import java.net.NetworkInterface;

import java.util.Enumeration;

public class NetWorkCode {

    private NetWorkCode() {
    }

    private static final String DEFAULT_NETWORK_INTERFACE = "ethernet_3";

    public static String getNetWorkCode() throws Exception {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (ni.getName().equals(DEFAULT_NETWORK_INTERFACE)) {
                byte[] mac = ni.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    return sb.toString();
                }
            }
        }
        throw new Exception("Network interface not found or does not have a MAC address.");}
}
