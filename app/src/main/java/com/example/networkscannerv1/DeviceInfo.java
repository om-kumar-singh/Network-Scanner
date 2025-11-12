package com.example.networkscannerv1;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo {
    private String ipAddress;
    private String hostname;
    private List<Integer> openPorts;
    private boolean isOnline;

    public DeviceInfo(String ipAddress) {
        this.ipAddress = ipAddress;
        this.hostname = "";
        this.openPorts = new ArrayList<>();
        this.isOnline = false;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<Integer> getOpenPorts() {
        return openPorts;
    }

    public void setOpenPorts(List<Integer> openPorts) {
        this.openPorts = openPorts;
    }

    public void addOpenPort(int port) {
        if (!openPorts.contains(port)) {
            openPorts.add(port);
        }
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}

