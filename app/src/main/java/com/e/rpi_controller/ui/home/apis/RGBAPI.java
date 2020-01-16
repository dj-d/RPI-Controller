package com.e.rpi_controller.ui.home.apis;

import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.ui.home.dataSets.rgb.RGBResponseDataSet;
import com.e.rpi_controller.ui.home.dataSets.rgbRelay.RGBRelayResponseDataSet;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RGBAPI {
    @POST("/rgb")
    Call<ResponseDataSet> SetColors(
            @Header("api_key") String apiKey,

            @Query("red") int red,
            @Query("green") int green,
            @Query("blue") int blue
    );

    @POST("/rgb/status")
    Call<RGBResponseDataSet> GetColorsStatus(
            @Header("api_key") String apiKey
    );

    @POST("/rgb/relay")
    Call<ResponseDataSet> SendData(
            @Header("api_key") String apiKey,

            @Query("action") String action,
            @Query("in") Integer in
    );

    @POST("/rgb/relay/status")
    Call<RGBRelayResponseDataSet> GetRelaysStatus(
            @Header("api_key") String apiKey
    );
}
