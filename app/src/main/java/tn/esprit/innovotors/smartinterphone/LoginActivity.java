package tn.esprit.innovotors.smartinterphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.FacebookService;
import tn.esprit.innovotors.smartinterphone.services.GoogleService;
import tn.esprit.innovotors.smartinterphone.services.MessageService;
import tn.esprit.innovotors.smartinterphone.services.SigninService;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView username, password;
    private Button signin;
    private SharedPreferences mPrefs;
    private TextView signup;
    private Activity activity;
    private Button facebook, google;
    public GoogleSignInClient mGoogleSignInClient;
    private static final int RC_GOOGLE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        activity = this;

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        facebook = findViewById(R.id.facebook);
        google = findViewById(R.id.google);


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("user_photos", "email",
                                "user_birthday", "public_profile", "user_gender"));


                // Callback registration
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        // final DonorService donorService=new DonorService();
                        FacebookService facebookService = new FacebookService(getApplicationContext(), activity);
                        facebookService.getFbInfo();
                        final MessageService messageService = new MessageService(getApplicationContext(),activity);
                        UserManager userManager = new UserManager(getApplicationContext());
                        MessageManager messageManager = new MessageManager(getApplicationContext());
                        messageManager.deleteMessages();

                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {


                                messageService.getMessages(user.getUsername(), new MessageCallback() {
                                    MessageManager messageManager = new MessageManager(getApplicationContext());

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


                    }

                    @Override
                    public void onCancel() {
                        // App code

                        Toast.makeText(getApplicationContext(), "Facebook Cancel", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(getApplicationContext(), "Facebook" + exception.toString(), Toast.LENGTH_LONG).show();


                    }
                });

            }


        });


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();
                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                signIn();
                final MessageService messageService = new MessageService(getApplicationContext(),activity);
                UserManager userManager = new UserManager(getApplicationContext());
                MessageManager messageManager = new MessageManager(getApplicationContext());
                messageManager.deleteMessages();

                userManager.getUser(new UserCallback() {
                    @Override
                    public void setUser(User user) {


                            messageService.getMessages(user.getUsername(), new MessageCallback() {
                            MessageManager messageManager = new MessageManager(getApplicationContext());

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


            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ok", "onClick: ");
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

                    SigninService signinService = new SigninService(getApplicationContext(), activity);
                    signinService.signinWithEmailAndPassword(username.getText().toString(), password.getText().toString());
                    MessageService messageService = new MessageService(getApplicationContext(),activity);
                    MessageManager messageManager = new MessageManager(getApplicationContext());
                    messageManager.deleteMessages();
                    messageService.getMessages(username.getText().toString(), new MessageCallback() {
                        MessageManager messageManager = new MessageManager(getApplicationContext());


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
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putString("login", "logged");
                    mEditor.apply();
                }
            }
        });


        // loginButton = (LoginButton) findViewById(R.id.login_button);
        //  loginButton.setReadPermissions("email");
        // login with facebook
        //  loginFB();


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleService googleService = new GoogleService(getApplicationContext(),activity);
            googleService.handleSignInResult(task);
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE);
    }
}
