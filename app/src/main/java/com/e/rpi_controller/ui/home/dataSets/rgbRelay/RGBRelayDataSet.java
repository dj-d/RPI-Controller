package com.e.rpi_controller.ui.home.dataSets.rgbRelay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RGBRelayDataSet {
    @SerializedName("in1")
    @Expose
    private boolean in1;
    @SerializedName("in2")
    @Expose
    private boolean in2;
    @SerializedName("in3")
    @Expose
    private boolean in3;

    public boolean getIn1() {
        return in1;
    }

    public void setIn1(boolean in1) {
        this.in1 = in1;
    }

    public boolean getIn2() {
        return in2;
    }

    public void setIn2(boolean in2) {
        this.in2 = in2;
    }

    public boolean getIn3() {
        return in3;
    }

    public void setIn3(boolean in3) {
        this.in3 = in3;
    }
}