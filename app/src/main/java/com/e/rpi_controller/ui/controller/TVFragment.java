package com.e.rpi_controller.ui.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.ui.controller.apis.ControllerAPI;
import com.e.rpi_controller.ui.controller.dataset.SoundBarDataSet;
import com.e.rpi_controller.ui.controller.services.SoundBarIntentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TVFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, NestedScrollView.OnScrollChangeListener {

    //TV
    private static final String ON = "on";
    private static final String INPUT = "input";
    private static final String VOL_UP = "vol_up";
    private static final String VOL_DOWN = "vol_down";
    private static final String VOL_MUTE = "vol_mute";
    private static final String PROG_UP = "prog_up";
    private static final String PROG_DOWN = "prog_down";
    private static final String ARROW_UP = "arrow_up";
    private static final String ARROW_DOWN = "arrow_down";
    private static final String ARROW_LEFT = "arrow_left";
    private static final String ARROW_RIGHT = "arrow_right";
    private static final String OK = "ok";
    private static final String SETTINGS = "settings";
    private static final String BACK = "back";

    // SOUNDBAR
    private static final String ON_SOUNDBAR = "ON";
    private static final String AUX = "AUX";
    private static final String HDMI = "HDMI";
    private static final String USB = "USB";
    private static final String OPTICAL = "OPTICAL";
    private static final String BLUETOOTH = "BLUETOOTH";

    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView nestedScrollView;

    private Retrofit retrofit;
    private ControllerAPI controllerAPI;
    private Call<ResponseDataSet> call;
    private Callback<ResponseDataSet> callback = new Callback<ResponseDataSet>() {
        @Override
        public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
            if (response.isSuccessful()) {
                if (response.body().getValid()) {

                }
            } else if (response.code() == 400) {
                Toast.makeText(getContext(), "400 TV", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 TV", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail TV", Toast.LENGTH_SHORT).show();
        }
    };

    private Intent serviceIntent;
    private SoundBarDataSet soundBarDataSet = new SoundBarDataSet();
    private ResponseReceiver receiver = new ResponseReceiver();

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SoundBarIntentService.ACTION)) {
                soundBarDataSet.setInput(intent.getStringExtra("input"));

                refreshSpinner(soundBarDataSet);
            }
        }
    }

    // TV
    private ImageButton on;
    private ImageButton input;
    private ImageButton settings;
    private ImageButton volUp;
    private ImageButton volDown;
    private ImageButton volMute;
    private ImageButton prgUp;
    private ImageButton prgDown;
    private ImageButton arrowLeft;
    private ImageButton arrowRight;
    private ImageButton arrowUp;
    private ImageButton arrowDown;
    private Button ok;
    private Button back;

    //SOUNDBAR
    private ImageButton soundBarOn;
    private Spinner soundBarInput;
    ArrayAdapter<CharSequence> soundBarInputAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        controllerAPI = retrofit.create(ControllerAPI.class);

        View view = inflater.inflate(R.layout.fragment_tv, container, false);

        on = view.findViewById(R.id.tv_on);
        input = view.findViewById(R.id.tv_input);
        settings = view.findViewById(R.id.tv_settings);
        volUp = view.findViewById(R.id.tv_vol_up);
        volDown = view.findViewById(R.id.tv_vol_down);
        volMute = view.findViewById(R.id.tv_vol_mute);
        prgUp = view.findViewById(R.id.tv_program_up);
        prgDown = view.findViewById(R.id.tv_program_down);
        arrowLeft = view.findViewById(R.id.tv_arrow_left);
        arrowRight = view.findViewById(R.id.tv_arrow_right);
        arrowUp = view.findViewById(R.id.tv_arrow_up);
        arrowDown = view.findViewById(R.id.tv_arrow_down);
        ok = view.findViewById(R.id.tv_ok);
        back = view.findViewById(R.id.tv_back);

        on.setOnClickListener(this);
        input.setOnClickListener(this);
        settings.setOnClickListener(this);
        volUp.setOnClickListener(this);
        volDown.setOnClickListener(this);
        volMute.setOnClickListener(this);
        prgUp.setOnClickListener(this);
        prgDown.setOnClickListener(this);
        arrowLeft.setOnClickListener(this);
        arrowRight.setOnClickListener(this);
        arrowUp.setOnClickListener(this);
        arrowDown.setOnClickListener(this);
        ok.setOnClickListener(this);
        back.setOnClickListener(this);

        soundBarOn = view.findViewById(R.id.tv_sound_on);
        soundBarOn.setOnClickListener(this);

        soundBarInput = view.findViewById(R.id.tv_sound_input_spinner);
        soundBarInputAdapter = ArrayAdapter.createFromResource(getContext(), R.array.input_array, android.R.layout.simple_spinner_item);
        soundBarInputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        soundBarInput.setAdapter(soundBarInputAdapter);
        soundBarInput.setOnItemSelectedListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_tv);
        swipeRefreshLayout.setOnRefreshListener(this);

        nestedScrollView = view.findViewById(R.id.tv_nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(this);

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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(receiver, new IntentFilter(SoundBarIntentService.ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_on:
                BaseAction(ON);
                break;

            case R.id.tv_input:
                BaseAction(INPUT);
                break;

            case R.id.tv_settings:
                BaseAction(SETTINGS);
                break;

            case R.id.tv_vol_up:
                VolAction(VOL_UP);
                break;

            case R.id.tv_vol_down:
                VolAction(VOL_DOWN);
                break;

            case R.id.tv_vol_mute:
                VolAction(VOL_MUTE);
                break;

            case R.id.tv_program_up:
                PrgAction(PROG_UP);
                break;

            case R.id.tv_program_down:
                PrgAction(PROG_DOWN);
                break;

            case R.id.tv_arrow_up:
                ArrowAction(ARROW_UP);
                break;

            case R.id.tv_arrow_down:
                ArrowAction(ARROW_DOWN);
                break;

            case R.id.tv_arrow_left:
                ArrowAction(ARROW_LEFT);
                break;

            case R.id.tv_arrow_right:
                ArrowAction(ARROW_RIGHT);
                break;

            case R.id.tv_ok:
                ArrowAction(OK);
                break;

            case R.id.tv_back:
                BaseAction(BACK);
                break;

            case R.id.tv_sound_on:
                BaseAction(ON_SOUNDBAR);
                break;
        }
    }

    /**
     * Call API for base action
     *
     * @param i Action (String)
     */
    private void BaseAction(String i) {
        switch (i) {
            case ON_SOUNDBAR:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), ON_SOUNDBAR.toLowerCase());
                break;

            case ON:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), ON);
                break;

            case INPUT:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), INPUT);
                break;

            case BACK:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), BACK);
                break;

            case SETTINGS:

            default:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), SETTINGS);
        }

        call.enqueue(callback);
    }

    /**
     * Call API for volume action
     * @param i Action (String)
     */
    private void VolAction(String i) {
        switch (i) {
            case VOL_MUTE:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), VOL_MUTE);
                break;

            case VOL_UP:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), VOL_UP);
                break;

            case VOL_DOWN:

            default:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), VOL_DOWN);
        }

        call.enqueue(callback);
    }

    /**
     * Call API for program action
     * @param i Action (String)
     */
    private void PrgAction(String i) {
        switch (i) {
            case PROG_UP:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), PROG_UP);
                break;

            case PROG_DOWN:

            default:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), PROG_DOWN);
        }

        call.enqueue(callback);
    }

    /**
     * Call API for arrow action
     * @param i Action (String)
     */
    private void ArrowAction(String i) {
        switch (i) {
            case ARROW_UP:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), ARROW_UP);
                break;

            case ARROW_DOWN:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), ARROW_DOWN);
                break;

            case ARROW_LEFT:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), ARROW_LEFT);
                break;

            case ARROW_RIGHT:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), ARROW_RIGHT);
                break;

            case OK:

            default:
                call = controllerAPI.TvCommand(SessionManager.getApiKey(), OK);
        }

        call.enqueue(callback);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = (String) parent.getItemAtPosition(position);

        switch (selection) {
            case AUX:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), AUX.toLowerCase());
                break;

            case HDMI:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), HDMI.toLowerCase());
                break;

            case USB:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), USB.toLowerCase());
                break;

            case OPTICAL:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), OPTICAL.toLowerCase());
                break;

            case BLUETOOTH:

            default:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), BLUETOOTH.toLowerCase());
                break;
        }

        call.enqueue(callback);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRefresh() {
        serviceIntent = new Intent(getContext(), SoundBarIntentService.class);
        getContext().startService(serviceIntent);

        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Update the Spinner status
     * @param soundBarDataSet SoundBarDataSet
     */
    private void refreshSpinner(SoundBarDataSet soundBarDataSet) {
        int inputPos = soundBarInputAdapter.getPosition(soundBarDataSet.getInput().toUpperCase());
        soundBarInput.setSelection(inputPos);
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
