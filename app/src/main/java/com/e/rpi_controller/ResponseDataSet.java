package com.e.rpi_controller;

public class ResponseDataSet {

    private String valid;
    private String info;

    /**
     * Map the response from server
     *
     * @param valid Indicates whether the API request was successful
     * @param info  Extra information
     */
    public ResponseDataSet(String valid, String info) {
        this.valid = valid;
        this.info = info;
    }

    /**
     * Get "valid" value
     *
     * @return Boolean
     */
    public Boolean getValid() {
        return Boolean.parseBoolean(valid);
    }

    /**
     * Get "info" value
     *
     * @return String
     */
    public String getInfo() {
        return info;
    }
}
