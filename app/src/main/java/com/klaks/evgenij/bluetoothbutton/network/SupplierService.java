package com.klaks.evgenij.bluetoothbutton.network;

import com.klaks.evgenij.bluetoothbutton.model.ResponseBody;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SupplierService {

    @POST("authorise")
    Observable<String> signIn(@Query("login") String login, @Query("password") String password);

    @GET("getbutton/InfoButton/{button}")
    Observable<ResponseBody> infoButton(@Path("button") String button);

    @POST("getzakaz")
    Observable<String> sendOrder(@Query("json") String json);
}
