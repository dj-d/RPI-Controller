package com.e.rpi_controller.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.rpi_controller.R;
import com.e.rpi_controller.ResponseDataSet;
import com.e.rpi_controller.RetrofitClient;
import com.e.rpi_controller.login.api.LoginAPI;
import com.e.rpi_controller.login.dataset.SignUpDataSet;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SIGN_UP_PREFERENCE = "SIGN_UP_PREFERENCE";
    public static final String USERNAME = "username";

    private EditText name;
    private EditText surname;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private ProgressBar loading;
    private Button btn;

    private SignUpDataSet signUpDataset;

    private Retrofit retrofit;
    private LoginAPI loginAPI;
    private Call<ResponseDataSet> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        retrofit = RetrofitClient.getRetrofit();
        loginAPI = retrofit.create(LoginAPI.class);

        name = findViewById(R.id.signup_name);
        surname = findViewById(R.id.signup_surname);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);
        loading = findViewById(R.id.signup_loading);
        btn = findViewById(R.id.signup_button);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SignUp();
    }

    /**
     * Check if all box are not empty
     */
    private void SignUp() {
        loading.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);

        String _name = this.name.getText().toString().trim();
        String _surname = this.surname.getText().toString().trim();
        String _username = this.username.getText().toString().trim();
        String _email = this.email.getText().toString().trim();
        String _password = this.password.getText().toString().trim();
        String _confirmPassword = this.confirmPassword.getText().toString().trim();

        if (!_name.isEmpty() && !_surname.isEmpty() && !_username.isEmpty() && !_email.isEmpty() && !_password.isEmpty() && !_confirmPassword.isEmpty() && _password.equals(_confirmPassword)) {
            sendData(signUpDataset);

        } else if (_name.isEmpty()) {
            this.name.setError("Insert name");

        } else if (_surname.isEmpty()) {
            this.surname.setError("Insert surname");

        } else if (_username.isEmpty()) {
            this.username.setError("Insert username");

        } else if (_email.isEmpty()) {
            this.email.setError("Insert email");

        } else if (_password.isEmpty()) {
            this.password.setError("Insert password");

        } else if (_confirmPassword.isEmpty()) {
            this.confirmPassword.setError("Insert confirmPassword");

        } else if (!_password.equals(_confirmPassword)) {
            this.confirmPassword.setError("Password do not match");

        } else {
            this.name.setError("Insert name");
            this.surname.setError("Insert surname");
            this.username.setError("Insert username");
            this.email.setError("Insert email");
            this.password.setError("Insert password");
            this.confirmPassword.setError("Insert confirmPassword");
        }
    }

    /**
     * Call SignUp API
     *
     * @param _signUpDataSet SingUp data set
     */
    private void sendData(SignUpDataSet _signUpDataSet) {
        HashMap<String, String> body = _signUpDataSet.createBody();

        call = loginAPI.SignUp(body);

        call.enqueue(new Callback<ResponseDataSet>() {
            @Override
            public void onResponse(Call<ResponseDataSet> call, Response<ResponseDataSet> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValid()) {
                        Toast.makeText(SignUpActivity.this, "SignUp successful", Toast.LENGTH_SHORT).show();

                        saveUsername(username);

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (response.code() == 400) {
                    btn.setVisibility(View.VISIBLE);

                    Toast.makeText(SignUpActivity.this, "400 SignUp", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    btn.setVisibility(View.VISIBLE);

                    Toast.makeText(SignUpActivity.this, "500 SignUp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataSet> call, Throwable t) {
                btn.setVisibility(View.VISIBLE);

                Toast.makeText(SignUpActivity.this, "Fail SignUp", Toast.LENGTH_SHORT).show();
            }
        });

        loading.setVisibility(View.GONE);
    }

    /**
     * Save username of the user in the sharedPreferences of the SingUp
     *
     * @param username EditText of the username
     */
    private void saveUsername(EditText username) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SIGN_UP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, username.getText().toString());

        editor.apply();
    }
}