package com.e.rpi_controller.ui.controller.dataset;

public class SoundBarResponseDataSet {

    private String valid;
    private SoundBarDataSet info;

    /**
     * Map the response from the server when i request sound bar data
     *
     * @param valid Indicates whether the API request was successful
     * @param info  SoundBarDataSet
     */
    public SoundBarResponseDataSet(String valid, SoundBarDataSet info) {
        this.valid = valid;
        this.info = info;
    }

    /**
     * Get "valid" value
     * @return Boolean
     */
    public Boolean getValid() {
        return Boolean.parseBoolean(valid);
    }

    /**
     * Get "info" data
     * @return SoundBarDataSet
     */
    public SoundBarDataSet getInfo() {
        return info;
    }
}
