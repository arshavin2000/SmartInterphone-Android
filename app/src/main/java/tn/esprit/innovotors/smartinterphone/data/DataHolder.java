package tn.esprit.innovotors.smartinterphone.data;


import java.util.ArrayList;

import tn.esprit.innovotors.smartinterphone.models.Device;

public class DataHolder {
    //design pattern to share arguments between fragments and activities
    private static DataHolder dataHolder = null;

    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (dataHolder == null)
            dataHolder = new DataHolder();
        return dataHolder;
    }
    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
