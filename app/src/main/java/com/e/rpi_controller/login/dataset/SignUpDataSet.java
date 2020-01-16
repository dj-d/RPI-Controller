package com.e.rpi_controller.login.dataset;

import java.util.HashMap;

public class SignUpDataSet {

    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private String name;
    private String surname;
    private String email;
    private String password;

    /**
     * Map the data to be sent to the server for SignUp operation
     *
     * @param name     Name of the user
     * @param surname  Surname of the user
     * @param email    Email of the user
     * @param password Password of the user
     */
    public SignUpDataSet(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
     * Create a HashMap of user data for SignUp operation
     * @return HashMap
     */
    public HashMap<String, String> createBody() {
        HashMap<String, String> body = new HashMap<>();

        body.put(NAME, this.name);
        body.put(SURNAME, this.surname);
        body.put(EMAIL, this.email);
        body.put(PASSWORD, this.password);

        return body;
    }
}