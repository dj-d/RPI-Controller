package com.e.rpi_controller.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.e.rpi_controller.APIKeyCreator;
import com.e.rpi_controller.MainActivity;

public class SessionManager {

    public static final String SESSION_PREFERENCES = "SESSION_PREFERENCES";
    private static final String LOGIN = "IS_LOGGED";
    private static final String USERNAME = "username";
    public static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    public static final String API_KEY = "api_key";

    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    /**
     * Manage the session of the current user
     *
     * @param context Context
     */
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Create the session
     * @param username Username of the user
     * @param email Email of the user
     * @param password Password of the user
     */
    public void createSession(String username, String email, String password) {
        APIKeyCreator api = new APIKeyCreator(email, password);

        editor.putBoolean(LOGIN, true);

        editor.putString(USERNAME, username);
        editor.putString(EMAIL, email);
        editor.putString(PASSWORD, password);
        editor.putString(API_KEY, api.sha256Creator());

        editor.apply();
    }

    /**
     * Check if the user is logged
     * @return Boolean
     */
    public boolean isLogged() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    /**
     * If is not logged redirects to LoginActivity
     */
    public void checkLogin() {
        if (!this.isLogged()) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    /**
     * Starts the operation for deleting the current session
     */
    public void logout() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((MainActivity) context).finish();
    }

    /**
     * Get API KEY from sharedPreferences of SessionManager
     * @return api_key (String)
     */
    public static String getApiKey() {
        return sharedPreferences.getString(API_KEY, "");
    }
}
