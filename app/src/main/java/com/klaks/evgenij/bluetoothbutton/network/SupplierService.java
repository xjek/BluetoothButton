package com.klaks.evgenij.bluetoothbutton.network;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SupplierService {

    @GET("getbutton/InfoButton/{button}")
    String infoButton(@Path("button") String button);
}
