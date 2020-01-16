package com.e.rpi_controller.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.home.apis.RGBAPI;
import com.e.rpi_controller.ui.home.dataSets.rgb.RGBDataSet;
import com.e.rpi_controller.ui.home.dataSets.rgbRelay.RGBRelayDataSet;
import com.e.rpi_controller.ui.home.dialogs.RGBDialog;
import com.e.rpi_controller.ui.home.services.RGBIntentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RGBFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String RGB_SHAREDPREFERENCES = "RGBNames";

    private static final String ON = "on";
    private static final String OFF = "off";

    private static Switch one;
    private static Switch two;
    private static Switch tree;

    private SeekBar red;
    private TextView textRedValue;
    private int redValue;
    private SeekBar green;
    private TextView textGreenValue;
    private int greenValue;
    private SeekBar blue;
    private TextView textBlueValue;
    private int blueValue;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Intent service;

    private final RGBDataSet rgbDataSet = new RGBDataSet();
    private final RGBRelayDataSet rgbRelayDataSet = new RGBRelayDataSet();
    private RGBResponseReceiver receiver = new RGBResponseReceiver();

    private Retrofit retrofit;

    private RGBAPI rgbApi;

    private Call<ResponseDataSet> callRgb;
    private Callback<ResponseDataSet> callbackRgb = new Callback<ResponseDataSet>() {
        @Override
        public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
            if (response.isSuccessful()) {
                if (response.body().getValid()) {

                }
            } else if (response.code() == 400) {
                Toast.makeText(getContext(), "400 RGB", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 RGB", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail RGB", Toast.LENGTH_SHORT).show();
        }
    };

    private Call<ResponseDataSet> callRgbRelay;
    private Callback<ResponseDataSet> callbackRgbRelay = new Callback<ResponseDataSet>() {
        @Override
        public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
            if (response.isSuccessful()) {
                if (response.body().getValid()) {

                }
            } else if (response.code() == 400) {
                Toast.makeText(getContext(), "400 RGB Relay", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 RGB Relay", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail RGB Relay", Toast.LENGTH_SHORT).show();
        }
    };

    private class RGBResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RGBIntentService.ACTION)) {
                rgbDataSet.setRed(intent.getIntExtra("red", 0));
                rgbDataSet.setGreen(intent.getIntExtra("green", 0));
                rgbDataSet.setBlue(intent.getIntExtra("blue", 0));

                rgbRelayDataSet.setIn1(intent.getBooleanExtra("in1", false));
                rgbRelayDataSet.setIn2(intent.getBooleanExtra("in2", false));
                rgbRelayDataSet.setIn3(intent.getBooleanExtra("in3", false));

                refreshProgressBar(rgbDataSet);
                refreshSwitch(rgbRelayDataSet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        rgbApi = retrofit.create(RGBAPI.class);

        View view = inflater.inflate(R.layout.fragment_rgb, container, false);

        one = view.findViewById(R.id.rgb_switch_1);
        two = view.findViewById(R.id.rgb_switch_2);
        tree = view.findViewById(R.id.rgb_switch_3);

        one.setOnCheckedChangeListener(this);
        two.setOnCheckedChangeListener(this);
        tree.setOnCheckedChangeListener(this);

        textRedValue = view.findViewById(R.id.rgb_red_value);
        red = view.findViewById(R.id.rgb_seekBar_red);
        red.setOnSeekBarChangeListener(this);

        textGreenValue = view.findViewById(R.id.rgb_green_value);
        green = view.findViewById(R.id.rgb_seekBar_green);
        green.setOnSeekBarChangeListener(this);

        textBlueValue = view.findViewById(R.id.rgb_blue_value);
        blue = view.findViewById(R.id.rgb_seekBar_blue);
        blue.setOnSeekBarChangeListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_rgb_tab);
        swipeRefreshLayout.setOnRefreshListener(this);

        onRefresh();

        loadNames(getContext());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // TODO: Trovare alternativa
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.menu_rgb, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rgb_change_name:
                RGBDialog rgbDialog = new RGBDialog();
                FragmentManager rgbFm = getActivity().getSupportFragmentManager();
                rgbDialog.show(rgbFm, "RGBDialogTag");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver, new IntentFilter(RGBIntentService.ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rgb_switch_1:
                if (isChecked) {
                    switchAction(ON, 1);
                } else {
                    switchAction(OFF, 1);
                }

                break;

            case R.id.rgb_switch_2:
                if (isChecked) {
                    switchAction(ON, 2);
                } else {
                    switchAction(OFF, 2);
                }

                break;

            case R.id.rgb_switch_3:

            default:
                if (isChecked) {
                    switchAction(ON, 3);
                } else {
                    switchAction(OFF, 3);
                }
        }
    }

    /**
     * Call API to change the status of relays
     *
     * @param action On | Off
     * @param num    Number of relay
     */
    private void switchAction(String action, int num) {
        callRgbRelay = rgbApi.SendData(SessionManager.getApiKey(), action, num);

        callRgbRelay.enqueue(callbackRgbRelay);
    }

    /**
     * Update the status of the switches
     *
     * @param rgbRelayDataSet RGBRelayDataSet
     */
    private void refreshSwitch(RGBRelayDataSet rgbRelayDataSet) {
        one.setChecked(rgbRelayDataSet.getIn1());
        two.setChecked(rgbRelayDataSet.getIn2());
        tree.setChecked(rgbRelayDataSet.getIn3());
    }

    /**
     * Load the name of the switches
     *
     * @param context Context
     */
    public static void loadNames(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RGB_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            one.setText(sharedPreferences.getString("nameOne", ""));
            two.setText(sharedPreferences.getString("nameTwo", ""));
            tree.setText(sharedPreferences.getString("nameTree", ""));
        } else {
            Toast.makeText(context, "Use default settings", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.rgb_seekBar_red:
                textRedValue.setText(String.valueOf(progress));
                redValue = progress;
                break;

            case R.id.rgb_seekBar_green:
                textGreenValue.setText(String.valueOf(progress));
                greenValue = progress;
                break;

            case R.id.rgb_seekBar_blue:

            default:
                textBlueValue.setText(String.valueOf(progress));
                blueValue = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        swipeRefreshLayout.setEnabled(true);

        callRgb = rgbApi.SetColors(SessionManager.getApiKey(), redValue, greenValue, blueValue);

        callRgb.enqueue(callbackRgb);
    }


    /**
     * Update the value of the ProgressBar
     *
     * @param rgbDataSet RGBDataSet
     */
    private void refreshProgressBar(RGBDataSet rgbDataSet) {
        red.setProgress(rgbDataSet.getRed());
        green.setProgress(rgbDataSet.getGreen());
        blue.setProgress(rgbDataSet.getBlue());
    }

    @Override
    public void onRefresh() {
        service = new Intent(getContext(), RGBIntentService.class);
        getContext().startService(service);

        loadNames(getContext());

        swipeRefreshLayout.setRefreshing(false);
    }
}
