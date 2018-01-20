package com.klaks.evgenij.bluetoothbutton.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.klaks.evgenij.bluetoothbutton.ui.networkAvailable.NetworkAvailableActivity;


abstract public class BaseActivity extends AppCompatActivity {

    protected boolean isNetworkAvailableAndConnected() {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    protected boolean checkNetworkState() {
        if (!isNetworkAvailableAndConnected()) {
            Intent intent = new Intent(this, NetworkAvailableActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }
}
