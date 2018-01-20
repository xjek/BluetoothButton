package com.klaks.evgenij.bluetoothbutton.ui.main;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klaks.evgenij.bluetoothbutton.R;

import java.util.ArrayList;
import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.Holder> {

    private List<BluetoothDevice> devices = new ArrayList<>();
    private DevicesAdapterListener listener;

    public DevicesAdapter(DevicesAdapterListener listener) {
        this.listener = listener;
    }

    static class Holder extends RecyclerView.ViewHolder {

        View itemView;
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            this.itemView = itemView;
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final BluetoothDevice device = devices.get(position);
        holder.name.setText(device.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeviceClick(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<BluetoothDevice> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    public interface DevicesAdapterListener {
        void onDeviceClick(BluetoothDevice bluetoothDevice);
    }
}
