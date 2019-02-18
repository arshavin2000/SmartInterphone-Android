package tn.esprit.innovotors.smartinterphone.interfaces;

import java.util.List;

import tn.esprit.innovotors.smartinterphone.models.Device;

public interface DeviceCallback {


    void setDevices(List<Device> devices);

    void setError(String msg);
}
