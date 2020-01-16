package com.e.rpi_controller.ui.home.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ui.home.RGBFragment;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class RGBDialog extends DialogFragment implements View.OnClickListener {

    private EditText switch_1;
    private EditText switch_2;
    private EditText switch_3;

    private Button sendButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_rgb, container, false);

        switch_1 = view.findViewById(R.id.dialog_rgb_switch_1);
        switch_2 = view.findViewById(R.id.dialog_rgb_switch_2);
        switch_3 = view.findViewById(R.id.dialog_rgb_switch_3);

        sendButton = view.findViewById(R.id.dialog_rgb_btn_send);
        sendButton.setOnClickListener(this);

        cancelButton = view.findViewById(R.id.dialog_rgb_btn_cancel);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getShowsDialog()) {
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

            int dialogWidth = (int) Math.min(metrics.widthPixels * 0.7, metrics.heightPixels);

            getDialog().getWindow().setLayout(dialogWidth, WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_rgb_btn_send:
                sendButtonAction();
                break;

            case R.id.dialog_rgb_btn_cancel:
                if (getShowsDialog()) {
                    getDialog().cancel();
                } else {
                    dismiss();
                }
                break;
        }
    }

    /**
     * Send the name of the switch to RGBFragment
     */
    private void sendButtonAction() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(RGBFragment.RGB_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!switch_1.getText().toString().isEmpty() && !switch_1.getText().toString().equals("TODO")) {
            editor.putString("nameOne", switch_1.getText().toString());
        } else if (switch_1.getText().toString().equals("TODO")) {
            editor.putString("nameOne", "");
        }

        if (!switch_2.getText().toString().isEmpty() && !switch_2.getText().toString().equals("TODO")) {
            editor.putString("nameTwo", switch_2.getText().toString());
        } else if (switch_2.getText().toString().equals("TODO")) {
            editor.putString("nameTwo", "");
        }

        if (!switch_3.getText().toString().isEmpty() && !switch_3.getText().toString().equals("TODO")) {
            editor.putString("nameTree", switch_3.getText().toString());
        } else if (switch_3.getText().toString().equals("TODO")) {
            editor.putString("nameTree", "");
        }

        editor.apply();

        RGBFragment.loadNames(getContext());

        dismiss();

        if (switch_1.getText().toString().isEmpty() && switch_2.getText().toString().isEmpty() && switch_3.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "No name changed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Names changed", Toast.LENGTH_SHORT).show();
        }
    }
}
