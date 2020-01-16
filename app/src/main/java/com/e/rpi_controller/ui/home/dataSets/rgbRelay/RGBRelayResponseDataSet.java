package com.e.rpi_controller.ui.home.dataSets.rgbRelay;

public class RGBRelayResponseDataSet {

    private String valid;
    private RGBRelayDataSet info;

    /**
     * Map the response from server
     *
     * @param valid Indicates whether the API request was successful
     * @param info  RGBRelayDataSet
     */
    public RGBRelayResponseDataSet(String valid, RGBRelayDataSet info) {
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
     * @return RGBRelayDataSet
     */
    public RGBRelayDataSet getInfo() {
        return info;
    }
}
