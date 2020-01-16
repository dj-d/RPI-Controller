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
import android.widget.Switch;
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
import com.e.rpi_controller.ui.home.apis.PowerStripsAPI;
import com.e.rpi_controller.ui.home.dataSets.PowerStripsDataSet;
import com.e.rpi_controller.ui.home.dialogs.PowerStripTwoDialog;
import com.e.rpi_controller.ui.home.services.PowerStripTwoIntentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PowerStripTwoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String POWER_STRIP_TWO_SHAREDPREFERENCE = "PowerStripTwoNames";

    private static final String NUM_POWER_STRIP = "two";
    private static final String ON = "on";
    private static final String OFF = "off";

    private Retrofit retrofit;
    private PowerStripsAPI powerStripsAPI;
    private Call<ResponseDataSet> call;
    private Callback<ResponseDataSet> callback = new Callback<ResponseDataSet>() {
        @Override
        public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
            if (response.isSuccessful()) {
                if (response.body().getValid()) {

                }
            } else if (response.code() == 400) {
                Toast.makeText(getContext(), "400 PS 2", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 PS 2", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail PS 2", Toast.LENGTH_SHORT).show();
        }
    };

    private static Switch one;
    private static Switch two;
    private static Switch tree;
    private static Switch four;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Intent serviceIntent;
    private PowerStripsDataSet powerStripsDataSet = new PowerStripsDataSet();
    private ResponseReceiver receiver = new ResponseReceiver();

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PowerStripTwoIntentService.ACTION)) {
                powerStripsDataSet.setIn1(intent.getBooleanExtra("in1", false));
                powerStripsDataSet.setIn2(intent.getBooleanExtra("in2", false));
                powerStripsDataSet.setIn3(intent.getBooleanExtra("in3", false));
                powerStripsDataSet.setIn4(intent.getBooleanExtra("in4", false));

                refreshSwitch(powerStripsDataSet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        powerStripsAPI = retrofit.create(PowerStripsAPI.class);

        View view = inflater.inflate(R.layout.fragment_power_strip_two_tab, container, false);

        one = view.findViewById(R.id.s2_switch_1);
        two = view.findViewById(R.id.s2_switch_2);
        tree = view.findViewById(R.id.s2_switch_3);
        four = view.findViewById(R.id.s2_switch_4);

        one.setOnCheckedChangeListener(this);
        two.setOnCheckedChangeListener(this);
        tree.setOnCheckedChangeListener(this);
        four.setOnCheckedChangeListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_power_strip_two);
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
        inflater.inflate(R.menu.menu_power_strip_two, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pw2_change_name:
                PowerStripTwoDialog powerStripTwoDialog = new PowerStripTwoDialog();
                FragmentManager powerStripTwoFm = getActivity().getSupportFragmentManager();
                powerStripTwoDialog.show(powerStripTwoFm, "PowerStripTwoDialogTag");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver, new IntentFilter(PowerStripTwoIntentService.ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.s2_switch_1:
                if (isChecked) {
                    switchAction(ON, 1);
                } else {
                    switchAction(OFF, 1);
                }

                break;

            case R.id.s2_switch_2:
                if (isChecked) {
                    switchAction(ON, 2);
                } else {
                    switchAction(OFF, 2);
                }

                break;

            case R.id.s2_switch_3:
                if (isChecked) {
                    switchAction(ON, 3);
                } else {
                    switchAction(OFF, 3);
                }

                break;

            case R.id.s2_switch_4:
                if (isChecked) {
                    switchAction(ON, 4);
                } else {
                    switchAction(OFF, 4);
                }

                break;
        }
    }

    /**
     * Call API to change the status of relays
     *
     * @param action On | Off
     * @param num    Number of relay
     */
    private void switchAction(String action, int num) {
        call = powerStripsAPI.SendData(SessionManager.getApiKey(), NUM_POWER_STRIP, action, num);

        call.enqueue(callback);
    }

    @Override
    public void onRefresh() {
        serviceIntent = new Intent(getContext(), PowerStripTwoIntentService.class);
        getContext().startService(serviceIntent);

        loadNames(getContext());

        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Update the status of the switches
     * @param powerStripsDataSet PowerStripsDataSet
     */
    private void refreshSwitch(PowerStripsDataSet powerStripsDataSet) {
        one.setChecked(powerStripsDataSet.getIn1());
        two.setChecked(powerStripsDataSet.getIn2());
        tree.setChecked(powerStripsDataSet.getIn3());
        four.setChecked(powerStripsDataSet.getIn4());
    }

    /**
     * Load the name of the switches
     * @param context Context
     */
    public static void loadNames(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(POWER_STRIP_TWO_SHAREDPREFERENCE, Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            one.setText(sharedPreferences.getString("nameOne", ""));
            two.setText(sharedPreferences.getString("nameTwo", ""));
            tree.setText(sharedPreferences.getString("nameTree", ""));
            four.setText(sharedPreferences.getString("nameFour", ""));

        } else {
            Toast.makeText(context, "Use default settings", Toast.LENGTH_SHORT).show();
        }
    }
}
