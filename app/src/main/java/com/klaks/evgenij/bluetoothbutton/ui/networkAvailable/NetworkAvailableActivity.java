package com.klaks.evgenij.bluetoothbutton.ui.networkAvailable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.klaks.evgenij.bluetoothbutton.R;
import com.klaks.evgenij.bluetoothbutton.ui.BaseActivity;

public class NetworkAvailableActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_available);

        findViewById(R.id.check).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (isNetworkAvailableAndConnected()) {
            onBackPressed();
        }
    }
}
