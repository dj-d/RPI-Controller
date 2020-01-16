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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.home.apis.PowerStripsAPI;
import com.e.rpi_controller.ui.home.dataSets.PowerStripsDataSet;
import com.e.rpi_controller.ui.home.dialogs.PowerStripOneDialog;
import com.e.rpi_controller.ui.home.services.PowerStripOneIntentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PowerStripOneFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, NestedScrollView.OnScrollChangeListener {

    public static final String POWER_STRIP_ONE_SHAREDPREFERENCES = "PowerStripOneNames";

    private static final String NUM_POWER_STRIP = "one";
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
                Toast.makeText(getContext(), "400 PS 1", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 PS 1", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail PS 1", Toast.LENGTH_SHORT).show();
        }
    };

    private static Switch one;
    private static Switch two;
    private static Switch tree;
    private static Switch four;
    private static Switch five;
    private static Switch six;
    private static Switch seven;
    private static Switch eight;

    private SwipeRefreshLayout swipeRefreshLayout;

    private NestedScrollView nestedScrollView;

    private Intent serviceIntent;
    private PowerStripsDataSet powerStripsDataSet = new PowerStripsDataSet();
    private ResponseReceiver receiver = new ResponseReceiver();

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PowerStripOneIntentService.ACTION)) {
                powerStripsDataSet.setIn1(intent.getBooleanExtra("in1", false));
                powerStripsDataSet.setIn2(intent.getBooleanExtra("in2", false));
                powerStripsDataSet.setIn3(intent.getBooleanExtra("in3", false));
                powerStripsDataSet.setIn4(intent.getBooleanExtra("in4", false));
                powerStripsDataSet.setIn5(intent.getBooleanExtra("in5", false));
                powerStripsDataSet.setIn6(intent.getBooleanExtra("in6", false));
                powerStripsDataSet.setIn7(intent.getBooleanExtra("in7", false));
                powerStripsDataSet.setIn8(intent.getBooleanExtra("in8", false));

                refreshSwitch(powerStripsDataSet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        powerStripsAPI = retrofit.create(PowerStripsAPI.class);

        View view = inflater.inflate(R.layout.fragment_power_strip_one_tab, container, false);

        one = view.findViewById(R.id.s1_switch_1);
        two = view.findViewById(R.id.s1_switch_2);
        tree = view.findViewById(R.id.s1_switch_3);
        four = view.findViewById(R.id.s1_switch_4);
        five = view.findViewById(R.id.s1_switch_5);
        six = view.findViewById(R.id.s1_switch_6);
        seven = view.findViewById(R.id.s1_switch_7);
        eight = view.findViewById(R.id.s1_switch_8);

        one.setOnCheckedChangeListener(this);
        two.setOnCheckedChangeListener(this);
        tree.setOnCheckedChangeListener(this);
        four.setOnCheckedChangeListener(this);
        five.setOnCheckedChangeListener(this);
        six.setOnCheckedChangeListener(this);
        seven.setOnCheckedChangeListener(this);
        eight.setOnCheckedChangeListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_power_strip_one);
        swipeRefreshLayout.setOnRefreshListener(this);

        nestedScrollView = view.findViewById(R.id.s1_nasted_scroll_view);
        nestedScrollView.setOnScrollChangeListener(this);

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
        inflater.inflate(R.menu.menu_power_strip_one, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pw1_change_name:
                PowerStripOneDialog powerStripOneDialog = new PowerStripOneDialog();
                FragmentManager powerStripOneFm = getActivity().getSupportFragmentManager();
                powerStripOneDialog.show(powerStripOneFm, "PowerStripOneDialogTag");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver, new IntentFilter(PowerStripOneIntentService.ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.s1_switch_1:
                if (isChecked) {
                    switchAction(ON, 1);
                } else {
                    switchAction(OFF, 1);
                }

                break;

            case R.id.s1_switch_2:
                if (isChecked) {
                    switchAction(ON, 2);
                } else {
                    switchAction(OFF, 2);
                }

                break;

            case R.id.s1_switch_3:
                if (isChecked) {
                    switchAction(ON, 3);
                } else {
                    switchAction(OFF, 3);
                }

                break;

            case R.id.s1_switch_4:
                if (isChecked) {
                    switchAction(ON, 4);
                } else {
                    switchAction(OFF, 4);
                }

                break;

            case R.id.s1_switch_5:
                if (isChecked) {
                    switchAction(ON, 5);
                } else {
                    switchAction(OFF, 5);
                }

                break;

            case R.id.s1_switch_6:
                if (isChecked) {
                    switchAction(ON, 6);
                } else {
                    switchAction(OFF, 6);
                }

                break;

            case R.id.s1_switch_7:
                if (isChecked) {
                    switchAction(ON, 7);
                } else {
                    switchAction(OFF, 7);
                }

                break;

            case R.id.s1_switch_8:
                if (isChecked) {
                    switchAction(ON, 8);
                } else {
                    switchAction(OFF, 8);
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
        serviceIntent = new Intent(getContext(), PowerStripOneIntentService.class);
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
        five.setChecked(powerStripsDataSet.getIn5());
        six.setChecked(powerStripsDataSet.getIn6());
        seven.setChecked(powerStripsDataSet.getIn7());
        eight.setChecked(powerStripsDataSet.getIn8());
    }

    /**
     * Load the name of the switches
     * @param context Context
     */
    public static void loadNames(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(POWER_STRIP_ONE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences != null) {
            one.setText(sharedPreferences.getString("nameOne", ""));
            two.setText(sharedPreferences.getString("nameTwo", ""));
            tree.setText(sharedPreferences.getString("nameTree", ""));
            four.setText(sharedPreferences.getString("nameFour", ""));
            five.setText(sharedPreferences.getString("nameFive", ""));
            six.setText(sharedPreferences.getString("nameSix", ""));
            seven.setText(sharedPreferences.getString("nameSeven", ""));
            eight.setText(sharedPreferences.getString("nameEight", ""));
        } else {
            Toast.makeText(context, "Use default settings", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        scrollY = v.getScrollY();

        if (scrollY == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}
