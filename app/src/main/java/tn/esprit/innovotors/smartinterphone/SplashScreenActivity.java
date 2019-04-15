package tn.esprit.innovotors.smartinterphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;


public class SplashScreenActivity extends Activity {

    private SharedPreferences mPrefs;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Activity activity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        activity = this;
      //  mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("smartinterphone.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        // Use the config
        Realm realm = Realm.getInstance(config);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if (mPrefs.contains("login")) {
                    String login = mPrefs.getString("login", "");
                    assert login != null;
                    if (!login.equals("")) {
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


                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();

                }


            }
        }, 3000);


    }
}
