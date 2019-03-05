package tn.esprit.innovotors.smartinterphone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.fragments.DeviceFragment;
import tn.esprit.innovotors.smartinterphone.fragments.MessageFragment;
import tn.esprit.innovotors.smartinterphone.fragments.ProfileFragment;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;

public class HomeActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragmentTransaction.replace(R.id.container, new DeviceFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:

                    fragmentTransaction.replace(R.id.container, new ProfileFragment());
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_notifications:

                    fragmentTransaction.replace(R.id.container, new MessageFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new DeviceFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();


            final UserManager userManager = new UserManager(getApplicationContext());
            final MessageManager messageManager = new MessageManager(getApplicationContext());
            messageManager.deleteMessages();
            userManager.getUser(new UserCallback() {
                @Override
                public void setUser(User user) {


                    MessageService messageService = new MessageService(getApplicationContext());
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

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
