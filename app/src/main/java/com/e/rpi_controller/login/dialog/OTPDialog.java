package com.e.rpi_controller.login.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.e.rpi_controller.MainActivity;
import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.LoginActivity;
import com.e.rpi_controller.login.SessionManager;
import com.e.rpi_controller.login.SignUpActivity;
import com.e.rpi_controller.login.api.LoginAPI;
import com.e.rpi_controller.login.dataset.LoginDataSet;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OTPDialog extends DialogFragment implements View.OnClickListener {

    private EditText otp;
    private Button btn;

    private LoginDataSet loginDataSet;

    private Retrofit retrofit;
    private LoginAPI loginAPI;
    private Call<ResponseDataSet> call;

    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        retrofit = RetrofitClient.getRetrofit();
        loginAPI = retrofit.create(LoginAPI.class);

        View view = inflater.inflate(R.layout.dialog_otp, container, false);

        otp = view.findViewById(R.id.dialog_login_otp);
        btn = view.findViewById(R.id.dialog_login_btn);
        btn.setOnClickListener(this);

        sessionManager = new SessionManager(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_login_btn:
                sendButtonAction();
                break;
        }
    }

    /**
     * Remind to login function
     */
    private void sendButtonAction() {
        if (!otp.getText().toString().isEmpty()) {
            this.loginDataSet = new LoginDataSet(LoginActivity.loginDataSet.getEmail(), LoginActivity.loginDataSet.getPassword(), otp.getText().toString());
            login(this.loginDataSet);

        } else {
            otp.setError("Insert OTP");
        }
    }

    /**
     * Call API for login
     *
     * @param loginDataSet LoginDataSet
     */
    private void login(LoginDataSet loginDataSet) {
        HashMap<String, String> body = loginDataSet.createLoginBody();

        call = loginAPI.Login(body);

        call.enqueue(new Callback<ResponseDataSet>() {
            @Override
            public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
                if (response.isSuccessful()) {

                    if (response.body().getValid()) {
                        sessionManager.createSession(getUsername(), LoginActivity.loginDataSet.getEmail(), LoginActivity.loginDataSet.getPassword());

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);

                        Toast.makeText(getActivity(), "Valid OTP", Toast.LENGTH_SHORT).show();
                        dismiss();

                    } else {
                        Toast.makeText(getActivity(), "Not valid OTP", Toast.LENGTH_SHORT).show(); // NON SCRIVE L'OTP NEL DB
                    }

                } else if (response.code() == 400) {
                    resetButtons();
                    dismiss();

                    Toast.makeText(getActivity(), "400 OTP", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    resetButtons();
                    dismiss();

                    Toast.makeText(getActivity(), "500 OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataSet> call, Throwable t) {
                Toast.makeText(getActivity(), "Fail OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Make visible the buttons on LoginActivity after loading
     */
    private void resetButtons() {
        LoginActivity.btnLogin.setVisibility(View.VISIBLE);
        LoginActivity.or.setVisibility(View.VISIBLE);
        LoginActivity.btnSignUp.setVisibility(View.VISIBLE);
    }

    /**
     * Get username of user from sharedPreferences of SignUp
     * @return username (String)
     */
    private String getUsername() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SignUpActivity.SIGN_UP_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SignUpActivity.USERNAME, "");
    }
}