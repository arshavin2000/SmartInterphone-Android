package tn.esprit.innovotors.smartinterphone.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;

public class MessageManager {

    private Context context;
    private Realm realm = null;


    public MessageManager(Context context) {
        this.context = context;
    }

    public void addMessage(final Message message) {

        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@SuppressWarnings("NullableProblems") Realm realm) {

                    try {
                        Log.e("messgae", "execute: " + message);

                        realm.insert(message);

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

    public void getMessage(final MessageCallback messageCallback) {


        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@SuppressWarnings("NullableProblems") Realm realm) {


                List<Message> messages = realm.where(Message.class).findAll();
                if (messages != null)
                    messageCallback.setMessages(messages);
                else
                    messageCallback.setError(context.getString(R.string.user_not_found));
                Log.e("User", "execute: " + messages);


            }
        });


    }


    public void deleteMessages(){
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Message.class);
            }
        });
    }
}
