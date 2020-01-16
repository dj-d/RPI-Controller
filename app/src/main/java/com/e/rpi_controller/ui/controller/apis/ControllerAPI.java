package com.e.rpi_controller.ui.controller.apis;

import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.ui.controller.dataset.SoundBarResponseDataSet;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ControllerAPI {

    @POST("/tv")
    Call<ResponseDataSet> TvCommand(
            @Header("api_key") String apiKey,

            @Query("action") String action);
/rpi/api/soundbar")
    Call<ResponseDataSet> SoundBarCommand(
            @Header("api_key") String apiKey,

            @Query("action") String action
    );

    @POST("/soundbar/last_selections")
    Call<SoundBarResponseDataSet> SoundBarLastSelection(
            @Header("api_key") String apiKey
    );
}
