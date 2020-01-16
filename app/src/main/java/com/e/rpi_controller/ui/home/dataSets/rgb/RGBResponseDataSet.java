package com.e.rpi_controller.ui.home.dataSets.rgb;

public class RGBResponseDataSet {

    private String valid;
    private RGBDataSet info;

    /**
     * Map the response from server
     *
     * @param valid Indicate whether the API request was successful
     * @param info  RGBDataSet
     */
    public RGBResponseDataSet(String valid, RGBDataSet info) {
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
     * @return RGBDataSet
     */
    public RGBDataSet getInfo() {
        return info;
    }
}
