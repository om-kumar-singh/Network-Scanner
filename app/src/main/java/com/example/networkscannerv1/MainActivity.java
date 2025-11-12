package com.example.networkscannerv1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MaterialButton btnScan;
    private MaterialButton btnShare;
    private CircularProgressIndicator progressBar;
    private TextView tvProgress;
    private MaterialCardView cardProgress;
    private TextView tvResultsTitle;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    private NetworkScanner networkScanner;
    private PortScanner portScanner;
    private List<DeviceInfo> allDevices;
    private Handler mainHandler;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainHandler = new Handler(Looper.getMainLooper());
        allDevices = new ArrayList<>();
        
        initializeViews();
        setupRecyclerView();
        setupNetworkScanner();
        setupPortScanner();
        setupButtons();
    }

    private void initializeViews() {
        btnScan = findViewById(R.id.btnScan);
        btnShare = findViewById(R.id.btnShare);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);
        cardProgress = findViewById(R.id.cardProgress);
        tvResultsTitle = findViewById(R.id.tvResultsTitle);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        adapter = new DeviceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupNetworkScanner() {
        networkScanner = new NetworkScanner(this);
        networkScanner.setCallback(new NetworkScanner.ScanCallback() {
            @Override
            public void onDeviceFound(DeviceInfo device) {
                mainHandler.post(() -> {
                    allDevices.add(device);
                    adapter.addDevice(device);
                    
                    // Start port scanning for this device
                    portScanner.scanPorts(device, scannedDevice -> {
                        mainHandler.post(() -> {
                            adapter.notifyDataSetChanged();
                        });
                    });
                });
            }

            @Override
            public void onScanComplete() {
                mainHandler.post(() -> {
                    isScanning = false;
                    btnScan.setEnabled(true);
                    btnScan.setText(getString(R.string.scan_network));
                    cardProgress.setVisibility(View.GONE);
                    
                    if (allDevices.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No devices found", Toast.LENGTH_SHORT).show();
                        tvResultsTitle.setVisibility(View.GONE);
                        btnShare.setVisibility(View.GONE);
                    } else {
                        tvResultsTitle.setVisibility(View.VISIBLE);
                        btnShare.setVisibility(View.VISIBLE);
                        btnShare.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Scan complete. Found " + allDevices.size() + " device(s)", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onScanProgress(int current, int total) {
                mainHandler.post(() -> {
                    tvProgress.setText("Scanning network: " + current + "/" + total + " IPs");
                });
            }
        });
    }

    private void setupPortScanner() {
        portScanner = new PortScanner();
    }

    private void setupButtons() {
        btnScan.setOnClickListener(v -> {
            if (isScanning) {
                networkScanner.stopScan();
                isScanning = false;
                btnScan.setEnabled(true);
                btnScan.setText(getString(R.string.scan_network));
                cardProgress.setVisibility(View.GONE);
            } else {
                startScan();
            }
        });

        btnShare.setOnClickListener(v -> shareResults());
    }

    private void startScan() {
        isScanning = true;
        allDevices.clear();
        adapter.setDevices(new ArrayList<>());
        tvResultsTitle.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
        btnScan.setEnabled(true);
        btnScan.setText("Stop Scan");
        btnShare.setEnabled(false);
        cardProgress.setVisibility(View.VISIBLE);
        tvProgress.setText("Initializing scan...");
        
        networkScanner.startScan();
    }

    private void shareResults() {
        if (allDevices.isEmpty()) {
            Toast.makeText(this, "No results to share", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder shareText = new StringBuilder();
        shareText.append("Network Scan Results\n");
        shareText.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())).append("\n");
        shareText.append("Total Devices Found: ").append(allDevices.size()).append("\n\n");
        
        for (DeviceInfo device : allDevices) {
            shareText.append("IP Address: ").append(device.getIpAddress()).append("\n");
            if (device.getHostname() != null && !device.getHostname().isEmpty()) {
                shareText.append("Hostname: ").append(device.getHostname()).append("\n");
            }
            List<Integer> openPorts = device.getOpenPorts();
            if (openPorts != null && !openPorts.isEmpty()) {
                shareText.append("Open Ports: ");
                for (int i = 0; i < openPorts.size(); i++) {
                    if (i > 0) shareText.append(", ");
                    shareText.append(openPorts.get(i));
                }
                shareText.append("\n");
            } else {
                shareText.append("Open Ports: None\n");
            }
            shareText.append("\n");
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Network Scan Results");
        
        startActivity(Intent.createChooser(shareIntent, "Share scan results via"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkScanner != null) {
            networkScanner.stopScan();
        }
        if (portScanner != null) {
            portScanner.shutdown();
        }
    }
}
