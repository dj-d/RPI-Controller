package com.e.rpi_controller.ui.home.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.home.apis.RGBAPI;
import com.e.rpi_controller.ui.home.dataSets.rgb.RGBResponseDataSet;
import com.e.rpi_controller.ui.home.dataSets.rgbRelay.RGBRelayResponseDataSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RGBIntentService extends IntentService {

    public static final String ACTION = "RGB";

    private Retrofit retrofit;
    private RGBAPI rgbApi;
    private Call<RGBResponseDataSet> callRGB;
    private Call<RGBRelayResponseDataSet> callRelay;

    /**
     * Service for request rgb data and rgbRelay data from server
     */
    public RGBIntentService() {
        super("RGBIntentService");

        retrofit = RetrofitClient.getRetrofit();
        rgbApi = retrofit.create(RGBAPI.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);

        callRGB = rgbApi.GetColorsStatus(SessionManager.getApiKey());

        callRGB.enqueue(new Callback<RGBResponseDataSet>() {
            @Override
            public void onResponse(Call<RGBResponseDataSet> call, Response<RGBResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        broadcastIntent.putExtra("red", response.body().getInfo().getRed());
                        broadcastIntent.putExtra("green", response.body().getInfo().getGreen());
                        broadcastIntent.putExtra("blue", response.body().getInfo().getBlue());

                        sendBroadcast(broadcastIntent);
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "400 RGB Intent", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "500 RGB Intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RGBResponseDataSet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail RGB Intent", Toast.LENGTH_SHORT).show();
            }
        });

        callRelay = rgbApi.GetRelaysStatus(SessionManager.getApiKey());

        callRelay.enqueue(new Callback<RGBRelayResponseDataSet>() {
            @Override
            public void onResponse(Call<RGBRelayResponseDataSet> call, Response<RGBRelayResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        broadcastIntent.putExtra("in1", response.body().getInfo().getIn1());
                        broadcastIntent.putExtra("in2", response.body().getInfo().getIn2());
                        broadcastIntent.putExtra("in3", response.body().getInfo().getIn3());

                        sendBroadcast(broadcastIntent);
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "400 RGB Relay Intent", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "500 RGB Relay Intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RGBRelayResponseDataSet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail RGB Relay Intent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
