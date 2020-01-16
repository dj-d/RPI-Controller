package com.e.rpi_controller.ui.controller.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.controller.apis.ControllerAPI;
import com.e.rpi_controller.ui.controller.dataset.SoundBarResponseDataSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SoundBarIntentService extends IntentService {
    public static final String ACTION = "LAST SELECTION";

    private Retrofit retrofit;
    private ControllerAPI controllerAPI;
    private Call<SoundBarResponseDataSet> call;

    /**
     * Service for request data of sound bar from server
     */
    public SoundBarIntentService() {
        super("SoundBarIntentService");

        retrofit = RetrofitClient.getRetrofit();
        controllerAPI = retrofit.create(ControllerAPI.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);

        call = controllerAPI.SoundBarLastSelection(SessionManager.getApiKey());

        call.enqueue(new Callback<SoundBarResponseDataSet>() {
            @Override
            public void onResponse(Call<SoundBarResponseDataSet> call, Response<SoundBarResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        broadcastIntent.putExtra("eq", response.body().getInfo().getEq());
                        broadcastIntent.putExtra("input", response.body().getInfo().getInput());

                        sendBroadcast(broadcastIntent);
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "400 SoundBar Intent", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "500 SoundBar Intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SoundBarResponseDataSet> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail SoundBar Intent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
