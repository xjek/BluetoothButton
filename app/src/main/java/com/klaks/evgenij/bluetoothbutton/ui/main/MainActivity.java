package com.klaks.evgenij.bluetoothbutton.ui.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.klaks.evgenij.bluetoothbutton.ButtonWorking;
import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.Scanner;
import com.klaks.evgenij.bluetoothbutton.ui.tovar.TovarActivity;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements DevicesAdapter.DevicesAdapterListener,
        ButtonWorking.ButtonWorkingListener,
        Scanner.ScannerListener{

    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    private Scanner scanner = new Scanner(this);

    private RecyclerView recyclerView;
    private DevicesAdapter devicesAdapter;
    private TextView process;
    private TextView pressButtonText;

    private Map<BluetoothDevice, ButtonWorking> connectedButtons = Collections.synchronizedMap(new HashMap<BluetoothDevice, ButtonWorking>());

    private boolean scannerIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        process = findViewById(R.id.process);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        devicesAdapter = new DevicesAdapter(this);
        recyclerView.setAdapter(devicesAdapter);

        pressButtonText = findViewById(R.id.pressButtonText);


        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<BluetoothDevice> devices = scanner.getDevicesList();
                        devicesAdapter.setDevices(devices);
                        if (devices.isEmpty()) {
                            pressButtonText.setVisibility(View.VISIBLE);
                        } else {
                            pressButtonText.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 0, 2, TimeUnit.SECONDS);

       // onResponseIsReceived("1-ffgsshsh");
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOnBluetooth();
    }

    private void checkOnBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            checkPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.location_access);
                builder.setMessage(R.string.grant_location_access);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= 23)
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
                return;
            }
        }
        startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScan();
                } else {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle(R.string.functionality_limited);
                    builder.setMessage(R.string.no_grant_location_access);
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    private void startScan() {
        if (!scannerIsOn) {
            if (bluetoothLeScanner == null) {
                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            }
            process.setText(R.string.process_scanning);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner.startScan(scanner);
                    scannerIsOn = true;
                }
            });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scannerIsOn) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner.stopScan(scanner);
                    scannerIsOn = false;
                }
            });

        }
    }

    @Override
    public void onDeviceClick(BluetoothDevice bluetoothDevice) {
        /*process.setText(R.string.process_connecting);
        ButtonWorking buttonWorking = new ButtonWorking(this);
        bluetoothDevice.connectGatt(this, false, buttonWorking);
        connectedButtons.put(bluetoothDevice, buttonWorking);*/
    }

    @Override
    public void onButtonDisconnected(BluetoothGatt gatt) {
        BluetoothDevice device = gatt.getDevice();
        connectedButtons.remove(device);
    }

    @Override
    public void onResponseIsReceived(final BluetoothGatt gatt, final String response) {
        Intent intent = new Intent(MainActivity.this, TovarActivity.class);
        intent.putExtra("button", response);
        startActivity(intent);
    }

    @Override
    public Set<BluetoothDevice> getConnectedDevices() {
        return connectedButtons.keySet();
    }
}
