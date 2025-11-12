package com.example.networkscannerv1;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PortScanner {
    private ExecutorService executorService;
    private static final int[] COMMON_PORTS = {22, 23, 80, 443, 8080, 3389, 21, 25, 53, 110, 143, 993, 995};

    public interface PortScanCallback {
        void onPortScanComplete(DeviceInfo device);
    }

    public PortScanner() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void scanPorts(DeviceInfo device, PortScanCallback callback) {
        List<Future<?>> futures = new ArrayList<>();

        for (int port : COMMON_PORTS) {
            final int portToScan = port;
            Future<?> future = executorService.submit(() -> {
                if (isPortOpen(device.getIpAddress(), portToScan, 500)) {
                    device.addOpenPort(portToScan);
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
                callback.onPortScanComplete(device);
            }
        });
    }

    private boolean isPortOpen(String ip, int port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new java.net.InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}

