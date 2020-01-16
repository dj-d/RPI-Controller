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

public class PowerStripTwoIntentService extends IntentService {

    public static final String ACTION = "POWERSTRIPTWO";

    private static final String NUM = "two";

    private Retrofit retrofit;
    private PowerStripsAPI powerStripsAPI;
    private Call<PowerStripsResponseDataSet> call;

    /**
     * Service for request data of power strip two from server
     */
    public PowerStripTwoIntentService() {
        super("PowerStripTwoIntentService");

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

                        sendBroadcast(broadcastIntent);
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "400 PS 2 Intent", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "500 PS 2 Intent", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PowerStripsResponseDataSet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail PS 2 Intent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
