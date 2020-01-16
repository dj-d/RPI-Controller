package com.e.rpi_controller.ui.home.apis;

import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.ui.home.dataSets.PowerStripsResponseDataSet;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PowerStripsAPI {

    @POST("/smart")
    Call<ResponseDataSet> SendData(
            @Header("api_key") String apiKey,

            @Query("num") String num,
            @Query("action") String action,
            @Query("in") Integer in
    );

    @POST("/smart/status")
    Call<PowerStripsResponseDataSet> GetStatus(
            @Header("api_key") String apiKey,

            @Query("num") String num
    );
}
