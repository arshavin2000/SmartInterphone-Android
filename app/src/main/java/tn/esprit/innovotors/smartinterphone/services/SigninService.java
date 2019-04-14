package tn.esprit.innovotors.smartinterphone.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Credentials;
import tn.esprit.innovotors.smartinterphone.HomeActivity;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.models.User;

public class SigninService {


    private static final String TAG = "SIGNIN_SERVICE";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private Context context;
    private Activity activity;
    public static final String MY_PREFS_NAME = "MyPrefsFile";



    public SigninService(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;

    }


    public void signinWithEmailAndPassword(final String username, final String password) {


        AndroidNetworking.post(BASE_URL.concat("signin"))
                .addHeaders("content-type", "application/json")
                .addHeaders("Authorization", Credentials.basic(username, password))
                .setTag("Signin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Toast.makeText(context, response.getJSONObject("message").getString("email"), Toast.LENGTH_LONG).show();

                            User user = new User();
                            user.setEmail(response.getJSONObject("message").getString("email"));
                            user.setUsername(response.getJSONObject("message").getString("username"));
                            user.setName(response.getJSONObject("message").getString("name"));
                            user.setToken(response.getString("token"));
                            UserManager userManager = new UserManager(context);
                            userManager.addUser(user);
                            SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
                            editor.putString("login", "login");
                            editor.apply();

                            activity.startActivity(new Intent(context, HomeActivity.class));
                            activity.finish();



                            Log.d(TAG, "onResponse: " + response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() == 401) {
                            Toast.makeText(context, context.getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 500) {
                            Toast.makeText(context, context.getString(R.string.user_already_registered), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 400) {
                            Toast.makeText(context, context.getString(R.string.invalid_request), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 404)
                            Toast.makeText(context, context.getString(R.string.user_not_found), Toast.LENGTH_LONG).show();

                        Log.e(TAG, "onError: " + error.getErrorBody());
                    }
                });

    }

    public void signinWithFacebookorGmail(final String username, final String password, final String email , final String name,final String url) {


        AndroidNetworking.post(BASE_URL.concat("signin"))
                .addHeaders("content-type", "application/json")
                .addHeaders("Authorization", Credentials.basic(username, password))
                .setTag("Signin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Toast.makeText(context, response.getJSONObject("message").getString("email"), Toast.LENGTH_LONG).show();

                            User user = new User();
                            user.setEmail(response.getJSONObject("message").getString("email"));
                            user.setUsername(response.getJSONObject("message").getString("username"));
                            user.setName(response.getJSONObject("message").getString("name"));
                            user.setToken(response.getString("token"));
                            user.setUrlImage(url);
                            UserManager userManager = new UserManager(context);
                            userManager.addUser(user);
                            SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
                            editor.putString("login", "login");
                            editor.apply();
                            activity.startActivity(new Intent(context, HomeActivity.class));
                            activity.finish();



                            Log.d(TAG, "onResponse: " + response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if (error.getErrorCode() == 401) {
                            Toast.makeText(context, context.getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 500) {
                            Toast.makeText(context, context.getString(R.string.user_already_registered), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 400) {
                            Toast.makeText(context, context.getString(R.string.invalid_request), Toast.LENGTH_LONG).show();
                        } else if (error.getErrorCode() == 404) {
                           // Toast.makeText(context, context.getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                            SignupService signupService = new SignupService(context,activity);
                            signupService.signupWithFacebook(email , name ,username,password, url);
                        }

                        Log.e(TAG, "onError: " + error.getErrorBody());
                    }
                });

    }
}
