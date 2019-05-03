package tn.esprit.innovotors.smartinterphone.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.interfaces.DeviceCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;

public class DeviceService {

    private Context context;
    private static final String TAG = "DEVICE_SERVICE";
    private static final String BASE_URL = "http://smart-interphone.herokuapp.com/api/";


    public DeviceService(Context context) {
        this.context = context;
    }


    public void addDevice(final Device device) {

        AndroidNetworking.post(BASE_URL.concat("devices"))
                .addBodyParameter("_id", device.getId())
                .addBodyParameter("name", device.getName())
                .addBodyParameter("code", device.getCode())
                .setTag("Add_device")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, context.getString(R.string.device_added), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context, context.getString(R.string.device_not_added), Toast.LENGTH_LONG).show();


                    }
                });
    }


    public void getDevices(final DeviceCallback deviceCallback, String username) {


        final List<Device> devices = new ArrayList<>();


        AndroidNetworking.get(BASE_URL.concat(username).concat("/devices"))
                .setTag("Add_device")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                Device device = new Device();
                                device.setId(response.getJSONObject(i).getString("_id"));
                                device.setCode(response.getJSONObject(i).getString("code"));
                                device.setName(response.getJSONObject(i).getString("name"));
                                devices.add(device);
                                Log.e(TAG, "onResponse: "+device );

                            }
                            deviceCallback.setDevices(devices);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        deviceCallback.setError(anError.toString());

                    }
                });

    }
}
