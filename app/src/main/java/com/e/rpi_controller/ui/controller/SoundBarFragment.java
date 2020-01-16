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

public class SoundBarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, NestedScrollView.OnScrollChangeListener {

    private static final String ON = "ON";
    private static final String AUX = "AUX";
    private static final String HDMI = "HDMI";
    private static final String USB = "USB";
    private static final String OPTICAL = "OPTICAL";
    private static final String BLUETOOTH = "BLUETOOTH";
    private static final String DISPLAY = "DISPLAY";
    private static final String VOL_UP = "VOL_UP";
    private static final String VOL_DOWN = "VOL_DOWN";
    private static final String VOL_MUTE = "VOL_MUTE";
    private static final String NEXT_TRACK = "NEXT_TRACK";
    private static final String PREVIOUS_TRACK = "PREVIOUS_TRACK";
    private static final String PLAY_PAUSE = "PLAY_PAUSE";
    private static final String STOP = "STOP";
    private static final String LOOP = "LOOP";
    private static final String TREBLE_UP = "TREBLE_UP";
    private static final String TREBLE_DOWN = "TREBLE_DOWN";
    private static final String BASS_UP = "BASS_UP";
    private static final String BASS_DOWN = "BASS_DOWN";
    private static final String MUSIC = "MUSIC";
    private static final String MOVIE = "MOVIE";
    private static final String DIALOG = "DIALOG";
    private static final String TRE_D = "3D";

    private static final String EQ = "eq_";

    private ImageButton on;
    private ImageButton display;
    private ImageButton volUp;
    private ImageButton volDown;
    private ImageButton volMute;
    private ImageButton nextTrack;
    private ImageButton previousTrack;
    private ImageButton playPause;
    private ImageButton stop;
    private ImageButton loop;
    private ImageButton trebleUp;
    private ImageButton trebleDown;
    private ImageButton bassUp;
    private ImageButton bassDown;

    private Spinner input;
    ArrayAdapter<CharSequence> inputAdapter;

    private Spinner eq;
    ArrayAdapter<CharSequence> eqAdapter;

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
                Toast.makeText(getContext(), "400 Soundbar", Toast.LENGTH_SHORT).show();

            } else if (response.code() == 500) {
                Toast.makeText(getContext(), "500 Soundbar", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseDataSet> call, Throwable t) {
            Toast.makeText(getContext(), "Fail Soundbar", Toast.LENGTH_SHORT).show();
        }
    };

    private Intent serviceIntent;
    private SoundBarDataSet soundBarDataSet = new SoundBarDataSet();
    private ResponseReceiver receiver = new ResponseReceiver();

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SoundBarIntentService.ACTION)) {
                    soundBarDataSet.setEq(intent.getStringExtra("eq"));
                    soundBarDataSet.setInput(intent.getStringExtra("input"));

                    refreshSpinner(soundBarDataSet);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        controllerAPI = retrofit.create(ControllerAPI.class);

        View view = inflater.inflate(R.layout.fragment_sound_bar, container, false);

        on = view.findViewById(R.id.sound_on);
        display = view.findViewById(R.id.sound_display);
        volUp = view.findViewById(R.id.sound_vol_up);
        volDown = view.findViewById(R.id.sound_vol_down);
        volMute = view.findViewById(R.id.sound_mute);
        nextTrack = view.findViewById(R.id.sound_track_next);
        previousTrack = view.findViewById(R.id.sound_track_previous);
        playPause = view.findViewById(R.id.sound_play_pause);
        stop = view.findViewById(R.id.sound_stop);
        loop = view.findViewById(R.id.sound_loop);
        trebleUp = view.findViewById(R.id.sound_treble_up);
        trebleDown = view.findViewById(R.id.sound_treble_down);
        bassUp = view.findViewById(R.id.sound_bass_up);
        bassDown = view.findViewById(R.id.sound_bass_down);

        on.setOnClickListener(this);
        display.setOnClickListener(this);
        volUp.setOnClickListener(this);
        volDown.setOnClickListener(this);
        volMute.setOnClickListener(this);
        nextTrack.setOnClickListener(this);
        previousTrack.setOnClickListener(this);
        playPause.setOnClickListener(this);
        stop.setOnClickListener(this);
        loop.setOnClickListener(this);
        trebleUp.setOnClickListener(this);
        trebleDown.setOnClickListener(this);
        bassUp.setOnClickListener(this);
        bassDown.setOnClickListener(this);

        input = view.findViewById(R.id.sound_input_spinner);

        inputAdapter = ArrayAdapter.createFromResource(getContext(), R.array.input_array, android.R.layout.simple_spinner_item);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        input.setAdapter(inputAdapter);
        input.setOnItemSelectedListener(this);

        eq = view.findViewById(R.id.sound_eq_spinner);

        eqAdapter = ArrayAdapter.createFromResource(getContext(), R.array.eq_array, android.R.layout.simple_spinner_item);
        eqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eq.setAdapter(eqAdapter);
        eq.setOnItemSelectedListener(this);

        swipeRefreshLayout = view.findViewById(R.id.swipe_soundbar);
        swipeRefreshLayout.setOnRefreshListener(this);

        nestedScrollView = view.findViewById(R.id.soundbar_nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(this);

        onRefresh();

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
            case R.id.sound_on:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), ON.toLowerCase());
                break;

            case R.id.sound_display:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), DISPLAY.toLowerCase());
                break;

            case R.id.sound_vol_up:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), VOL_UP.toLowerCase());
                break;

            case R.id.sound_vol_down:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), VOL_DOWN.toLowerCase());
                break;

            case R.id.sound_mute:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), VOL_MUTE.toLowerCase());
                break;

            case R.id.sound_track_next:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), NEXT_TRACK.toLowerCase());
                break;

            case R.id.sound_track_previous:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), PREVIOUS_TRACK.toLowerCase());
                break;

            case R.id.sound_play_pause:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), PLAY_PAUSE.toLowerCase());
                break;

            case R.id.sound_stop:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), STOP.toLowerCase());
                break;

            case R.id.sound_loop:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), LOOP.toLowerCase());
                break;

            case R.id.sound_treble_up:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), TREBLE_UP.toLowerCase());
                break;

            case R.id.sound_treble_down:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), TREBLE_DOWN.toLowerCase());
                break;

            case R.id.sound_bass_up:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), BASS_UP.toLowerCase());
                break;

            case R.id.sound_bass_down:

            default:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), BASS_DOWN.toLowerCase());
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
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), BLUETOOTH.toLowerCase());
                break;

            case MUSIC:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), EQ + MUSIC.toLowerCase());
                break;

            case MOVIE:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), EQ + MOVIE.toLowerCase());
                break;

            case DIALOG:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), EQ + DIALOG.toLowerCase());
                break;

            case TRE_D:

            default:
                call = controllerAPI.SoundBarCommand(SessionManager.getApiKey(), EQ + TRE_D.toLowerCase());

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
     *
     * @param soundBarDataSet SoundBarDataSet
     */
    private void refreshSpinner(SoundBarDataSet soundBarDataSet) {
        int eqPos = eqAdapter.getPosition(soundBarDataSet.getEq().toUpperCase());
        eq.setSelection(eqPos);

        int inputPos = inputAdapter.getPosition(soundBarDataSet.getInput().toUpperCase());
        input.setSelection(inputPos);
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
