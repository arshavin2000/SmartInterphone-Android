package tn.esprit.innovotors.smartinterphone.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.User;

public class UserManager {

    private Context context;
    private Realm realm = null;


    public UserManager(Context context) {
        this.context = context;
    }

    public void addUser(final User user) {

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@SuppressWarnings("NullableProblems") Realm realm) {

                    try {
                        Log.e("user", "execute: " + user);

                        realm.insert(user);

                    } catch (RealmPrimaryKeyConstraintException e) {
                        Toast.makeText(context, "Primary Key exists, Press Update instead", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public void getUser(final UserCallback userCallback) {


        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@SuppressWarnings("NullableProblems") Realm realm) {


                User user = realm.where(User.class).findFirst();
                if (user != null)
                    userCallback.setUser(user);
                else
                    userCallback.setError(context.getString(R.string.user_not_found));
                Log.e("User", "execute: " + user);


            }
        });


    }
}
