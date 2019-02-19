package tn.esprit.innovotors.smartinterphone.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;

public class MessageService {

    private static final String TAG = "MESSAGE_SERVICE";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private Context context;


    public MessageService(Context context) {
        this.context = context;
    }


    public void addMessage(final Message message) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        AndroidNetworking.post(BASE_URL.concat("messages"))
                .addBodyParameter("content", message.getContent())
                .addBodyParameter("displayAt", dateFormat.format(message.getDisplayedAt()))
                .addBodyParameter("hiddenAt", dateFormat.format(message.getHiddenAt()))
                .addBodyParameter("username", message.getUser().getUsername())
                .addBodyParameter("device_id", message.getDevice().getId())
                .setTag("Add_device")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, context.getString(R.string.message_created), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context, context.getString(R.string.message_not_added), Toast.LENGTH_LONG).show();


                    }
                });
    }


    public void getMessages(final MessageCallback messageCallback) {


        final List<Message> messages = new ArrayList<>();


        AndroidNetworking.get(BASE_URL.concat("messages"))
                .setTag("get_Messages")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            for (int i = 0; i < response.getJSONArray("data").length(); i++) {

                                Message message = new Message();
                                message.setId(response.getJSONArray("data").getJSONObject(i).getString("_id"));
                                String[] createdAt = response.getJSONArray("data").getJSONObject(i).getString("createdAt").split("T");
                                String[] displayedAt = response.getJSONArray("data").getJSONObject(i).getString("displayAt").split("T");
                                String[] hiddenAt = response.getJSONArray("data").getJSONObject(i).getString("hiddenAt").split("T");
                                message.setCreatedAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(createdAt[0]+" "+createdAt[1].substring(0,createdAt[1].length()-2)));
                                message.setDisplayedAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(displayedAt[0]+" "+displayedAt[1].substring(0,displayedAt[1].length()-2)));
                                message.setHiddenAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(hiddenAt[0]+" "+hiddenAt[1].substring(0,hiddenAt[1].length()-2)));
                                message.setContent(response.getJSONArray("data").getJSONObject(i).getString("content"));
                             //   message.setDevice(response.getJSONArray("data").getJSONObject(i).getString("content"));


                                messages.add(message);
                                Log.e(TAG, "onResponse: " + message);

                            }
                            messageCallback.setMessages(messages);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        messageCallback.setError(anError.toString());

                    }
                });

    }
}
