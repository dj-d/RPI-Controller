package com.e.rpi_controller.ui.home.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.home.apis.PowerStripsAPI;
import com.e.rpi_controller.ui.home.dataSets.PowerStripsResponseDataSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PowerStripOneIntentService extends IntentService {

    public static final String ACTION = "POWERSTRIPONE";

    private static final String NUM = "one";

    private Retrofit retrofit;
    private PowerStripsAPI powerStripsAPI;
    private Call<PowerStripsResponseDataSet> call;

    /**
     * Service for request data of power strip one from server
     */
    public PowerStripOneIntentService() {
        super("PowerStripOneIntentService");

        retrofit = RetrofitClient.getRetrofit();
        powerStripsAPI = retrofit.create(PowerStripsAPI.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);

        call = powerStripsAPI.GetStatus(SessionManager.getApiKey(), NUM);

        call.enqueue(new Callback<PowerStripsResponseDataSet>() {
            @Override
            public void onResponse(Call<PowerStripsResponseDataSet> call, Response<PowerStripsResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        broadcastIntent.putExtra("in1", response.body().getInfo().getIn1());
                        broadcastIntent.putExtra("in2", response.body().getInfo().getIn2());
                        broadcastIntent.putExtra("in3", response.body().getInfo().getIn3());
                        broadcastIntent.putExtra("in4", response.body().getInfo().getIn4());
                        broadcastIntent.putExtra("in5", response.body().getInfo().getIn5());
                        broadcastIntent.putExtra("in6", response.body().getInfo().getIn6());
                        broadcastIntent.putExtra("in7", response.body().getInfo().getIn7());
                        broadcastIntent.putExtra("in8", response.body().getInfo().getIn8());

                        sendBroadcast(broadcastIntent);
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "400 PS 1 Intent", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "500 PS 1 Intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PowerStripsResponseDataSet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail PS 1 Intent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}