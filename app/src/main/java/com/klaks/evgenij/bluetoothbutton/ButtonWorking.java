package com.klaks.evgenij.bluetoothbutton;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import static com.klaks.evgenij.bluetoothbutton.Common.TAG;

public class ButtonWorking extends BluetoothGattCallback {

    private static final String BUTTON_MESSAGE_GET = "get";
    private static final String BUTTON_MESSAGE_OK = "ok";

    private static final String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_UUID =  "0000ffe1-0000-1000-8000-00805f9b34fb";

    private ButtonWorkingListener listener;

    public ButtonWorking(ButtonWorkingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        switch (newState) {
            case 0:
                listener.onButtonDisconnected(gatt);
                Log.d(TAG, "Device disconnected");
                break;
            case 2:
                Log.d(TAG, "Device connected");
                gatt.discoverServices();
                break;
            case 3:
                Log.d(TAG, "Device don`t know what do");
                break;
        }
    }



    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (gatt.getServices() != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    if (service.getUuid().toString().equals(SERVICE_UUID) && characteristic.getUuid().toString().equals(CHARACTERISTIC_UUID)) {
                        characteristic.setValue(BUTTON_MESSAGE_GET.getBytes());
                        gatt.writeCharacteristic(characteristic);
                        gatt.setCharacteristicNotification(characteristic, true);
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        String value = new String(characteristic.getValue());
        characteristic.setValue(BUTTON_MESSAGE_OK.getBytes());
        gatt.writeCharacteristic(characteristic);
        listener.onResponseIsReceived(gatt, value);
    }

    public interface ButtonWorkingListener {
        void onResponseIsReceived(BluetoothGatt gatt, String response);
        void onButtonDisconnected(BluetoothGatt gatt);
    }
}
