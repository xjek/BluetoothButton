package com.klaks.evgenij.bluetoothbutton.network;

import com.klaks.evgenij.bluetoothbutton.Common;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
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
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        System.out.println("CHAIN " + chain.request().url());
                        return chain.proceed(chain.request());
                    }
                })
                .build();
    }
}
