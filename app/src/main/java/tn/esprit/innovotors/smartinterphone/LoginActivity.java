package tn.esprit.innovotors.smartinterphone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.services.SigninService;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView username, password;
    private Button signin;
    private SharedPreferences mPrefs;
    private TextView signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.new_account);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextEmpty()) {
                    if (username.getText().toString().isEmpty())
                        username.setError(getString(R.string.invalid_empty));
                    if (password.getText().toString().isEmpty())
                        password.setError(getString(R.string.invalid_empty));
                    clearAll();
                } else {

                    SigninService signinService = new SigninService(getApplicationContext());
                    signinService.signinWithEmailAndPassword(username.getText().toString(), password.getText().toString());
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("login", "logged");
                    mEditor.apply();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
            }
        });


        // loginButton = (LoginButton) findViewById(R.id.login_button);
        //  loginButton.setReadPermissions("email");
        // login with facebook
        //  loginFB();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loginFB() {


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }


    private boolean isTextEmpty() {
        return username.getText().toString().trim().isEmpty()
                || password.getText().toString().trim().isEmpty();
    }


    private boolean isPasswordValid() {
        return password.getText().toString().length() > 8;
    }

    private void clearAll() {
        password.setText("");
        username.setText("");
    }
}
