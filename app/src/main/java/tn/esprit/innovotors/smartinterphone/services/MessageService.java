package tn.esprit.innovotors.smartinterphone.services;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.fragments.MessageFragment;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;

public class MessageService {

    private static final String TAG = "MESSAGE_SERVICE";
    private static final String BASE_URL = "http://smart-interphone.herokuapp.com/api/";
    private Context context;
    private Activity activity;


    public MessageService(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    public void addMessage(final Message message) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


        Log.e(TAG, "addMessage: " + message.getUser().getId());

        AndroidNetworking.post(BASE_URL.concat("messages"))
                .addBodyParameter("content", message.getContent())
                .addBodyParameter("displayAt", message.getStartDate())
                .addBodyParameter("hiddenAt", message.getEndDate())
                .addBodyParameter("username", message.getUser().getUsername())
                .addBodyParameter("user", String.valueOf(message.getUser().getId()))
                .addBodyParameter("device_id", message.getDevice())
                .setTag("Add_device")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, context.getString(R.string.message_created), Toast.LENGTH_LONG).show();
                        UserManager userManager = new UserManager(context);
                        MessageManager messageManager = new MessageManager(context);
                        messageManager.deleteMessages();

                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {


                                getMessages(user.getUsername(), new MessageCallback() {
                                    MessageManager messageManager = new MessageManager(context);

                                    @Override
                                    public void setMessages(List<Message> messages) {
                                        for (Message message : messages
                                        ) {
                                            messageManager.addMessage(message);
                                        }
                                    }

                                    @Override
                                    public void setError(String msg) {

                                    }
                                });

                            }

                            @Override
                            public void setError(String msg) {

                            }
                        });


                        activity.finish();
                        activity.startActivity(activity.getIntent());

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context, context.getString(R.string.message_not_added), Toast.LENGTH_LONG).show();


                    }
                });
    }


    public void getMessages(String user, final MessageCallback messageCallback) {


        final List<Message> messages = new ArrayList<>();
        Log.e(TAG, "getMessages: " + BASE_URL.concat(user + "/messages"));


        AndroidNetworking.get(BASE_URL.concat(user + "/messages"))
                .setTag("get_Messages")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {


                        Log.e(TAG, "onResponse: " + response.length() );

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                Message message = new Message();
                                message.setId(response.getJSONObject(i).getString("_id"));
                                String[] createdAt = response.getJSONObject(i).getString("createdAt").split("T");
                                String[] displayedAt = response.getJSONObject(i).getString("displayAt").split("T");
                                String[] hiddenAt = response.getJSONObject(i).getString("hiddenAt").split("T");
                                message.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.getDefault()).parse(createdAt[0] + "T" + createdAt[1].substring(0, createdAt[1].length() - 5)+"-0000"));
                                message.setDisplayedAt(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ",Locale.getDefault() ).parse(displayedAt[0] + "T" + displayedAt[1].substring(0, displayedAt[1].length() - 5)+"-0000"));
                                message.setHiddenAt(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ",Locale.getDefault()).parse(hiddenAt[0] + "T" + hiddenAt[1].substring(0, hiddenAt[1].length() - 5)+"-0000"));
                                message.setContent(response.getJSONObject(i).getString("content"));
                                Log.e(TAG, "onResponse: " + displayedAt[0] + " " + displayedAt[1].substring(0, displayedAt[1].length() - 2));
                                Log.e(TAG, "onResponse: " + message.getDisplayedAt());
                                message.setDevice(response.getJSONObject(i).getString("device"));

                                Log.e(TAG, "onResponse: " +i );

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
                        Log.e(TAG, "onError: " + anError.toString() );

                    }
                });

    }

    public void updateMessage(final String id , final String id_device ,final  String content , final String display , final String hidden ) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");



        AndroidNetworking.put(BASE_URL.concat("messages/").concat(id).concat("/").concat(id_device))
                .addBodyParameter("content", content)
                .addBodyParameter("displayAt", display)
                .addBodyParameter("hiddenAt", hidden)
                .addBodyParameter("device", id_device)
                .setTag("Update_device")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, "onResponse: update " +response );

                        Toast.makeText(context, context.getString(R.string.message_updated), Toast.LENGTH_LONG).show();
                        UserManager userManager = new UserManager(context);
                        MessageManager messageManager = new MessageManager(context);
                        messageManager.deleteMessages();

                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {


                                getMessages(user.getUsername(), new MessageCallback() {
                                    MessageManager messageManager = new MessageManager(context);

                                    @Override
                                    public void setMessages(List<Message> messages) {
                                        for (Message message : messages
                                        ) {
                                            messageManager.addMessage(message);
                                        }
                                    }

                                    @Override
                                    public void setError(String msg) {

                                    }
                                });

                            }

                            @Override
                            public void setError(String msg) {

                            }
                        });


                        activity.finish();
                        activity.startActivity(activity.getIntent());


                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context, context.getString(R.string.message_not_added), Toast.LENGTH_LONG).show();


                    }
                });
    }




    public void getMessagesByDevice(String user, final MessageCallback messageCallback, final Device device) {


        final List<Message> messages = new ArrayList<>();
        Log.e(TAG, "getMessages: " + BASE_URL.concat(user + "/messages"));


        AndroidNetworking.get(BASE_URL.concat(user + "/messages"))
                .setTag("get_Messages")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                if (response.getJSONObject(i).getString("device").equals(device.getName())) {
                                    Message message = new Message();
                                    message.setId(response.getJSONObject(i).getString("_id"));
                                    String[] createdAt = response.getJSONObject(i).getString("createdAt").split("T");
                                    String[] displayedAt = response.getJSONObject(i).getString("displayAt").split("T");
                                    String[] hiddenAt = response.getJSONObject(i).getString("hiddenAt").split("T");
                                    message.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdAt[0] + " " + createdAt[1].substring(0, createdAt[1].length() - 2)));
                                    message.setDisplayedAt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(displayedAt[0] + " " + displayedAt[1].substring(0, displayedAt[1].length() - 2)));
                                    message.setHiddenAt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(hiddenAt[0] + " " + hiddenAt[1].substring(0, hiddenAt[1].length() - 2)));
                                    message.setContent(response.getJSONObject(i).getString("content"));
                                    Log.e(TAG, "onResponse: " + displayedAt[0] + " " + displayedAt[1].substring(0, displayedAt[1].length() - 2));
                                    Log.e(TAG, "onResponse: " + message.getDisplayedAt());


                                    //   message.setDevice(response.getJSONArray("data").getJSONObject(i).getString("content"));


                                    messages.add(message);
                                    Log.e(TAG, "onResponse: " + message);
                                }

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

    public void deleteMessage(String message_id) {




        AndroidNetworking.delete(BASE_URL.concat("messages/").concat(message_id))
                .setTag("Delete_Message")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, "Message was deleted sucessfully",Toast.LENGTH_LONG).show();
                        UserManager userManager = new UserManager(context);
                        MessageManager messageManager = new MessageManager(context);
                        messageManager.deleteMessages();

                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {


                                getMessages(user.getUsername(), new MessageCallback() {
                                    MessageManager messageManager = new MessageManager(context);

                                    @Override
                                    public void setMessages(List<Message> messages) {
                                        for (Message message : messages
                                        ) {
                                            messageManager.addMessage(message);
                                        }
                                    }

                                    @Override
                                    public void setError(String msg) {

                                    }
                                });

                            }

                            @Override
                            public void setError(String msg) {

                            }
                        });


                        activity.finish();
                        activity.startActivity(activity.getIntent());

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toast.makeText(context, "Delete action fails !",Toast.LENGTH_LONG).show();
                        UserManager userManager = new UserManager(context);
                        MessageManager messageManager = new MessageManager(context);
                        messageManager.deleteMessages();

                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {


                                getMessages(user.getUsername(), new MessageCallback() {
                                    MessageManager messageManager = new MessageManager(context);

                                    @Override
                                    public void setMessages(List<Message> messages) {
                                        for (Message message : messages
                                        ) {
                                            messageManager.addMessage(message);
                                        }
                                    }

                                    @Override
                                    public void setError(String msg) {

                                    }
                                });

                            }

                            @Override
                            public void setError(String msg) {

                            }
                        });


                        activity.finish();
                        activity.startActivity(activity.getIntent());

                    }
                });

    }
}
