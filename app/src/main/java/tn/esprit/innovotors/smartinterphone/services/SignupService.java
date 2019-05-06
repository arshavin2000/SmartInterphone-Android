package tn.esprit.innovotors.smartinterphone.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import tn.esprit.innovotors.smartinterphone.HomeActivity;
import tn.esprit.innovotors.smartinterphone.LoginActivity;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.models.User;

public class SignupService {


    private static final String TAG ="SIGNUP_SERVICE";
    private static final String BASE_URL = "http://smart-interphone.herokuapp.com/api/";
    private Context context;
    private Activity activity;


    public SignupService(Context context, Activity activity){

        this.context = context;
        this.activity = activity;

    }



    public void signupWithEmailAndPassword(String email , String name , String username , String password){
        AndroidNetworking.post(BASE_URL.concat("signup"))
                .addBodyParameter("email", email)
                .addBodyParameter("name", name)
                .addBodyParameter("username",username)
                .addBodyParameter("password",password)
                .setTag("Signup")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: " + response.getString("message"));
                            activity.startActivity(new Intent(context, LoginActivity.class));
                            activity.finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(error.getErrorCode() == 409){
                            Toast.makeText(context,context.getString(R.string.internal_server_error),Toast.LENGTH_LONG).show();
                        }else if( error.getErrorCode() ==500){
                            Toast.makeText(context,context.getString(R.string.user_already_registered),Toast.LENGTH_LONG).show();
                        }else if(error.getErrorCode() == 400){
                            Toast.makeText(context,context.getString(R.string.invalid_request),Toast.LENGTH_LONG).show();
                        }
                        Log.e(TAG, "onError: " + error.getErrorBody() );
                    }
                });
    }

    public void signupWithFacebook(final String email , final String name , final String username , String password,final String url){
        AndroidNetworking.post(BASE_URL.concat("signup"))
                .addBodyParameter("email", email)
                .addBodyParameter("name", name)
                .addBodyParameter("username",username)
                .addBodyParameter("password",password)
                .setTag("Signup")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: " + response.getString("message"));
                            UserManager userManager = new UserManager(context);
                            User user = new User();
                            user.setName(name);
                            user.setUsername(username);
                            user.setEmail(email);
                            user.setUrlImage(url);
                            userManager.addUser(user);
                            activity.startActivity(new Intent(context, HomeActivity.class));
                            activity.finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        if(error.getErrorCode() == 409){
                            Toast.makeText(context,context.getString(R.string.internal_server_error),Toast.LENGTH_LONG).show();
                        }else if( error.getErrorCode() ==500){
                            Toast.makeText(context,context.getString(R.string.user_already_registered),Toast.LENGTH_LONG).show();
                        }else if(error.getErrorCode() == 400){
                            Toast.makeText(context,context.getString(R.string.invalid_request),Toast.LENGTH_LONG).show();
                        }
                        Log.e(TAG, "onError: " + error.getErrorBody() );
                    }
                });
    }
}
