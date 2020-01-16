package com.e.rpi_controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class APIKeyCreator {
    private String email;
    private String password;

    /**
     * Make the API KEY like the server
     *
     * @param email    Email of the user
     * @param password Password of the user
     */
    public APIKeyCreator(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Split the email by "@"
     * @return The first element of split (String)
     */
    private String emailSplit() {
        String[] str = this.email.split("@");

        return str[0];
    }

    /**
     * Make a SHA256 string by email + password + emailSplit()
     * @return The API_KEY (String)
     */
    public String sha256Creator() {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String str = this.email + this.password + this.emailSplit();

        byte[] rawString = messageDigest.digest(str.getBytes());

        StringBuffer apiKey = new StringBuffer();

        for (int i = 0; i < rawString.length; i++) {
            apiKey.append(Integer.toString((rawString[i] & 0xff) + 0x100, 16).substring(1));
        }

        return apiKey.toString();
    }
}