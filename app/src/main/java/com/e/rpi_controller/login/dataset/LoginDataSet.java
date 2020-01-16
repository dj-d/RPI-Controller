package com.e.rpi_controller.login.dataset;

import com.e.rpi_controller.APIKeyCreator;

import java.util.HashMap;

public class LoginDataSet {

    private static final String EMAIL = "email";
    private static final String API_KEY = "api_key";
    private static final String OTP = "otp";

    private String email;
    private String password;
    private String otp;

    private APIKeyCreator apiKeyCreator;

    /**
     * Map the data to be sent to the server for Login operation
     *
     * @param email    Email of the user
     * @param password Password of the user
     */
    public LoginDataSet(String email, String password) {
        this.email = email;
        this.password = password;
        this.apiKeyCreator = new APIKeyCreator(email, password);
    }

    /**
     * Map the data to be sent to the server for Login operation
     *
     * @param email    Email of the user
     * @param password Password of the user
     * @param otp      OTP Code of the user
     */
    public LoginDataSet(String email, String password, String otp) {
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.apiKeyCreator = new APIKeyCreator(email, password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Create a HashMap for OTP Code request
     * @return HashMap
     */
    public HashMap<String, String> createRequestOtpBody() {
        HashMap<String, String> body = new HashMap<>();

        body.put(EMAIL, this.email);
        body.put(API_KEY, this.apiKeyCreator.sha256Creator());

        return body;
    }

    /**
     * Create a HashMap of user data for Login operation
     * @return HashMap
     */
    public HashMap<String, String> createLoginBody() {
        HashMap<String, String> body = new HashMap<>();

        body.put(EMAIL, this.email);
        body.put(API_KEY, this.apiKeyCreator.sha256Creator());
        body.put(OTP, this.otp);

        return body;
    }
}
