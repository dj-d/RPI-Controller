package com.e.rpi_controller.ui.home.dataSets;

public class PowerStripsResponseDataSet {

    private String valid;
    private PowerStripsDataSet info;

    /**
     * Map the response from server
     *
     * @param valid Indicates whether the API request was successful
     * @param info  PowerStripsDataSet
     */
    public PowerStripsResponseDataSet(String valid, PowerStripsDataSet info) {
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
     * @return PowerStripsDataSet
     */
    public PowerStripsDataSet getInfo() {
        return info;
    }
}
