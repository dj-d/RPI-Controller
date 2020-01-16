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
import com.e.rpi_controller.ui.home.PowerStripOneFragment;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PowerStripOneDialog extends DialogFragment implements View.OnClickListener {

    private EditText switch_1;
    private EditText switch_2;
    private EditText switch_3;
    private EditText switch_4;
    private EditText switch_5;
    private EditText switch_6;
    private EditText switch_7;
    private EditText switch_8;

    private Button sendButton;
    private Button cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_power_strip_one, container, false);

        switch_1 = view.findViewById(R.id.dialog_ps1_switch_1);
        switch_2 = view.findViewById(R.id.dialog_ps1_switch_2);
        switch_3 = view.findViewById(R.id.dialog_ps1_switch_3);
        switch_4 = view.findViewById(R.id.dialog_ps1_switch_4);
        switch_5 = view.findViewById(R.id.dialog_ps1_switch_5);
        switch_6 = view.findViewById(R.id.dialog_ps1_switch_6);
        switch_7 = view.findViewById(R.id.dialog_ps1_switch_7);
        switch_8 = view.findViewById(R.id.dialog_ps1_switch_8);

        sendButton = view.findViewById(R.id.dialog_ps1_btn_send);
        sendButton.setOnClickListener(this);

        cancelButton = view.findViewById(R.id.dialog_ps1_btn_cancel);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getShowsDialog()) {
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();

            int dialogWidth = (int) Math.min(metrics.widthPixels * 0.7, metrics.heightPixels);

            getDialog().getWindow().setLayout(dialogWidth, WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.dialog_ps1_btn_send:
                sendButtonAction();
                break;

            case R.id.dialog_ps1_btn_cancel:
                if(getShowsDialog()) {
                    getDialog().cancel();
                } else {
                    dismiss();
                }
                break;
        }
    }

    /**
     * Send the name of the switch to PowerStripOneFragment
     */
    private void sendButtonAction() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PowerStripOneFragment.POWER_STRIP_ONE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

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

        if (!switch_4.getText().toString().isEmpty() && !switch_4.getText().toString().equals("TODO")) {
            editor.putString("nameFour", switch_4.getText().toString());
        } else if (switch_4.getText().toString().equals("TODO")) {
            editor.putString("nameFour", "");
        }

        if (!switch_5.getText().toString().isEmpty() && !switch_5.getText().toString().equals("TODO")) {
            editor.putString("nameFive", switch_5.getText().toString());
        } else if (switch_5.getText().toString().equals("TODO")) {
            editor.putString("nameFive", "");
        }

        if (!switch_6.getText().toString().isEmpty() && !switch_6.getText().toString().equals("TODO")) {
            editor.putString("nameSix", switch_6.getText().toString());
        } else if (switch_6.getText().toString().equals("TODO")) {
            editor.putString("nameSix", "");
        }

        if (!switch_7.getText().toString().isEmpty() && !switch_7.getText().toString().equals("TODO")) {
            editor.putString("nameSeven", switch_7.getText().toString());
        } else if (switch_7.getText().toString().equals("TODO")) {
            editor.putString("nameSeven", "");
        }

        if (!switch_8.getText().toString().isEmpty() && !switch_8.getText().toString().equals("TODO")) {
            editor.putString("nameEight", switch_8.getText().toString());
        } else if (switch_8.getText().toString().equals("TODO")) {
            editor.putString("nameEight", "");
        }

        editor.apply();

        PowerStripOneFragment.loadNames(getContext());

        dismiss();

        if (switch_1.getText().toString().isEmpty() && switch_2.getText().toString().isEmpty() && switch_3.getText().toString().isEmpty() && switch_4.getText().toString().isEmpty() &&
                switch_5.getText().toString().isEmpty() && switch_6.getText().toString().isEmpty() && switch_7.getText().toString().isEmpty() && switch_8.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "No name changed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Names changed", Toast.LENGTH_SHORT).show();
        }
    }
}
