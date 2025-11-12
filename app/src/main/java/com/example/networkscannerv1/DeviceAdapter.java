package com.example.networkscannerv1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<DeviceInfo> devices;

    public DeviceAdapter() {
        this.devices = new ArrayList<>();
    }

    public void setDevices(List<DeviceInfo> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    public void addDevice(DeviceInfo device) {
        devices.add(device);
        notifyItemInserted(devices.size() - 1);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        DeviceInfo device = devices.get(position);
        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIpAddress;
        private TextView tvHostname;
        private TextView tvOpenPorts;
        private TextView tvNoPorts;
        private LinearLayout portsContainer;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIpAddress = itemView.findViewById(R.id.tvIpAddress);
            tvHostname = itemView.findViewById(R.id.tvHostname);
            tvOpenPorts = itemView.findViewById(R.id.tvOpenPorts);
            tvNoPorts = itemView.findViewById(R.id.tvNoPorts);
            portsContainer = itemView.findViewById(R.id.portsContainer);
        }

        public void bind(DeviceInfo device) {
            tvIpAddress.setText(device.getIpAddress());

            if (device.getHostname() != null && !device.getHostname().isEmpty() && 
                !device.getHostname().equals(device.getIpAddress())) {
                tvHostname.setText(device.getHostname());
                tvHostname.setVisibility(View.VISIBLE);
            } else {
                tvHostname.setVisibility(View.GONE);
            }

            List<Integer> openPorts = device.getOpenPorts();
            if (openPorts != null && !openPorts.isEmpty()) {
                StringBuilder portsText = new StringBuilder();
                for (int i = 0; i < openPorts.size(); i++) {
                    if (i > 0) portsText.append(", ");
                    portsText.append(openPorts.get(i));
                }
                tvOpenPorts.setText(portsText.toString());
                portsContainer.setVisibility(View.VISIBLE);
                tvNoPorts.setVisibility(View.GONE);
            } else {
                portsContainer.setVisibility(View.GONE);
                tvNoPorts.setVisibility(View.VISIBLE);
            }
        }
    }
}
