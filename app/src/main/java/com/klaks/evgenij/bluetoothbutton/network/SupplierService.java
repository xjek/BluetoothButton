package com.klaks.evgenij.bluetoothbutton.network;

import com.klaks.evgenij.bluetoothbutton.model.ResponseBody;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SupplierService {

    @GET("getbutton/InfoButton/{button}")
    Observable<ResponseBody> infoButton(@Path("button") String button);
}
