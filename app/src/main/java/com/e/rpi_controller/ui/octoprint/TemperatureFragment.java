package com.e.rpi_controller.ui.octoprint;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.rpi_controller.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemperatureFragment extends Fragment {


    public TemperatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_octoprint_temperature, container, false);
    }

}
