package tn.esprit.innovotors.smartinterphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class SplashScreenActivity extends Activity {

    private  SharedPreferences mPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("smartinterphone.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

       // Use the config
         Realm realm = Realm.getInstance(config);





        if(mPrefs.contains("login")){
            String login = mPrefs.getString("login", "");
            assert login != null;
            if(!login.equals("")){
                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
            }
            else{
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        }else{
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

        }






    }
}
