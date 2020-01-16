package com.e.rpi_controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.api.LoginAPI;
import com.e.rpi_controller.login.dataset.LoginDataSet;
import com.e.rpi_controller.login.dialog.OTPDialog;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    public static TextView or;
    private ProgressBar loading;
    public static Button btnLogin;
    public static Button btnSignUp;

    public static LoginDataSet loginDataSet;

    private Retrofit retrofit;
    private LoginAPI loginAPI;
    private Call<ResponseDataSet> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofit = RetrofitClient.getRetrofit();
        loginAPI = retrofit.create(LoginAPI.class);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        or = findViewById(R.id.login_or_text);
        loading = findViewById(R.id.login_loading);
        btnLogin = findViewById(R.id.login_button);
        btnSignUp = findViewById(R.id.login_signup_button);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                fieldControlForLogin();
                break;

            case R.id.login_signup_button:
                redirectToSignUp();
                break;
        }
    }

    /**
     * Check if field are not empty
     */
    private void fieldControlForLogin() {
        final String mEmail = email.getText().toString().trim();
        String mPassword = password.getText().toString().trim();

        if (!mEmail.isEmpty() && !mPassword.isEmpty()) {
            this.loginDataSet = new LoginDataSet(mEmail, mPassword);
            requestOtp(this.loginDataSet);

        } else if (mEmail.isEmpty()) {
            email.setError("Insert email");

        } else if (mPassword.isEmpty()) {
            password.setError("Insert password");

        } else {
            email.setError("Insert email");
            password.setError("Insert password");
        }
    }

    /**
     * Open window dialog for insert the OTP Code
     */
    private void showDialog() {
        OTPDialog otpDialog = new OTPDialog();
        FragmentManager otpFm = this.getSupportFragmentManager();
        otpDialog.show(otpFm, "OTPDialogTag");
    }

    /**
     * Call API for request OTP Code from server
     *
     * @param loginDataSet LoginDataSet
     */
    public void requestOtp(LoginDataSet loginDataSet) {
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        or.setVisibility(View.GONE);
        btnSignUp.setVisibility(View.GONE);

        HashMap<String, String> body = loginDataSet.createRequestOtpBody();

        call = loginAPI.RequestOtp(body);

        call.enqueue(new Callback<ResponseDataSet>() {
            @Override
            public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        showDialog();
                        loading.setVisibility(View.GONE);
                    }

                } else if (response.code() == 400) {
                    loading.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);

                    Toast.makeText(LoginActivity.this, "400 Login", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    loading.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    or.setVisibility(View.VISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);

                    Toast.makeText(LoginActivity.this, "500 Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataSet> call, Throwable t) {
                loading.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                or.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, "Fail Login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Redirect to SignUpActivity
     */
    private void redirectToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
