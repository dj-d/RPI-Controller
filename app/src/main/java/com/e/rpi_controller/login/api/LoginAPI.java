package com.e.rpi_controller.login.api;

import com.e.rpi_controller.ResponseDataSet;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginAPI {

    @Headers("Content-Type: application/json")
    @POST("/signup")
    Call<ResponseDataSet> SignUp(
            @Body HashMap<String, String> body
    );

    @Headers("Content-Type: application/json")
    @POST("/request-otp")
    Call<ResponseDataSet> RequestOtp(
            @Body HashMap<String, String> body
    );

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<ResponseDataSet> Login(
            @Body HashMap<String, String> body
    );

    @Headers("Content-Type: application/json")
    @POST("/reset-password")
    Call<JSONObject> ResetPassword(
            @Field("email") String email,
            // TODO: rimuovere l'api_key
            @Field("api_key") String api_key
    );

    @Headers("Content-Type: application/json")
    @POST("/change-password")
    Call<JSONObject> ChangePassword(
            @Field("email") String email,
            @Field("api_key") String api_key
    );
}
