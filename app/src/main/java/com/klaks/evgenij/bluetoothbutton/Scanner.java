package com.klaks.evgenij.bluetoothbutton;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.klaks.evgenij.bluetoothbutton.Common.BUTTON_PREFIX;

public class Scanner extends android.bluetooth.le.ScanCallback {

    private static final Pattern pattern = Pattern.compile("^" + BUTTON_PREFIX + ".*");

    private Map<String, BluetoothDevice> devices = Collections.synchronizedMap(new HashMap<String, BluetoothDevice>());

    private ScannerListener listener;

    public Scanner(ScannerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        BluetoothDevice device = result.getDevice();
        if (!listener.getConnectedDevices().contains(device)) {
            if (device.getName() != null) {
                Matcher matcher = pattern.matcher(device.getName().toLowerCase());
                if (matcher.matches()) {
                    devices.put(result.getDevice().getAddress(), result.getDevice());
                }
            }
        }

    }

    public List<BluetoothDevice> getDevicesList() {
        List<BluetoothDevice> devicesList = new ArrayList<>(devices.values());
        devices.clear();
        return devicesList;
    }

    public interface ScannerListener {
        Set<BluetoothDevice> getConnectedDevices();
    }


}
