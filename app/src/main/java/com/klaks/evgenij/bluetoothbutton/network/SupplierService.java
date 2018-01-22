package com.klaks.evgenij.bluetoothbutton.network;

import com.klaks.evgenij.bluetoothbutton.model.Auth;
import com.klaks.evgenij.bluetoothbutton.model.ResponseBody;
import com.klaks.evgenij.bluetoothbutton.model.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SupplierService {

    @FormUrlEncoded
    @POST("/authorise")
    Observable<Auth> signIn(@Field("login") String login, @Field("password") String password);

    @GET("getbutton/InfoButton/{button}")
    Observable<ResponseBody> infoButton(@Path("button") String button);

    @FormUrlEncoded
    @POST("getzakaz")
    Observable<Result> sendOrder(@Field("json") String json);
}
