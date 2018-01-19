package com.klaks.evgenij.bluetoothbutton.network;

import com.klaks.evgenij.bluetoothbutton.Common;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private volatile static OkHttpClient client;

    private volatile static SupplierService service;

    public static SupplierService getService() {
        SupplierService service = ApiFactory.service;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = ApiFactory.service;
                if (service == null) {
                    service = ApiFactory.service = ApiFactory.createService();
                }
            }
        }
        return service;
    }

    private static SupplierService createService() {
        return new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SupplierService.class);
    }

    private static OkHttpClient getClient() {
        OkHttpClient client = ApiFactory.client;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = ApiFactory.client;
                if (client == null) {
                    client = ApiFactory.client = createClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
