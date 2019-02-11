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

import tn.esprit.innovotors.smartinterphone.R;

public class SignupService {


    private static final String TAG ="SIGNUP_SERVICE";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private Context context;


    public SignupService(Context context){

        this.context = context;

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
