package com.example.networkscannerv1;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkScanner {
    private Context context;
    private ExecutorService executorService;
    private List<DeviceInfo> discoveredDevices;
    private ScanCallback callback;

    public interface ScanCallback {
        void onDeviceFound(DeviceInfo device);
        void onScanComplete();
        void onScanProgress(int current, int total);
    }

    public NetworkScanner(Context context) {
        this.context = context;
        this.executorService = Executors.newFixedThreadPool(20);
        this.discoveredDevices = new ArrayList<>();
    }

    public void setCallback(ScanCallback callback) {
        this.callback = callback;
    }

    public void startScan() {
        discoveredDevices.clear();
        
        String localIp = getLocalIpAddress();
        if (localIp == null || localIp.isEmpty()) {
            if (callback != null) {
                callback.onScanComplete();
            }
            return;
        }

        String[] ipParts = localIp.split("\\.");
        if (ipParts.length != 4) {
            if (callback != null) {
                callback.onScanComplete();
            }
            return;
        }

        String baseIp = ipParts[0] + "." + ipParts[1] + "." + ipParts[2] + ".";
        List<Future<?>> futures = new ArrayList<>();
        final int[] scannedCount = {0};
        final int total = 254;

        for (int i = 1; i <= 254; i++) {
            final int host = i;
            final String ipToScan = baseIp + host;

            Future<?> future = executorService.submit(() -> {
                try {
                    InetAddress address = InetAddress.getByName(ipToScan);
                    if (address.isReachable(500)) {
                        DeviceInfo device = new DeviceInfo(ipToScan);
                        device.setOnline(true);
                        
                        try {
                            device.setHostname(address.getHostName());
                        } catch (Exception e) {
                            // Hostname lookup failed, keep empty
                        }

                        synchronized (discoveredDevices) {
                            discoveredDevices.add(device);
                        }

                        if (callback != null) {
                            callback.onDeviceFound(device);
                        }
                    }
                } catch (Exception e) {
                    // Device not reachable or error occurred
                } finally {
                    synchronized (scannedCount) {
                        scannedCount[0]++;
                        if (callback != null) {
                            callback.onScanProgress(scannedCount[0], total);
                        }
                    }
                }
            });

            futures.add(future);
        }

        executorService.submit(() -> {
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    // Ignore
                }
            }
            if (callback != null) {
                callback.onScanComplete();
            }
        });
    }

    public void stopScan() {
        executorService.shutdownNow();
        executorService = Executors.newFixedThreadPool(20);
    }

    public List<DeviceInfo> getDiscoveredDevices() {
        return discoveredDevices;
    }

    private String getLocalIpAddress() {
        try {
            // Try WiFi first
            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                if (ipAddress != 0) {
                    return String.format("%d.%d.%d.%d",
                            (ipAddress & 0xff),
                            (ipAddress >> 8 & 0xff),
                            (ipAddress >> 16 & 0xff),
                            (ipAddress >> 24 & 0xff));
                }
            }

            // Fallback to NetworkInterface
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(':') < 0) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}

