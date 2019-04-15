package tn.esprit.innovotors.smartinterphone.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;

public class UpdateMessageFragment extends Fragment {

    private EditText messageText, time_start, time_end, date_start, date_end;
    private Button update;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_message, container, false);

        messageText = root.findViewById(R.id.message);
        time_end = root.findViewById(R.id.time_end);
        time_start = root.findViewById(R.id.time_start);
        date_end = root.findViewById(R.id.date_end);
        date_start = root.findViewById(R.id.date_start);
        update = root.findViewById(R.id.add);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MessageService messageService = new MessageService(getContext(), getActivity());


                String displayAt = date_start.getText().toString() + "T" + time_start.getText().toString() + ":00.000Z";
                String hiddenAt = date_end.getText().toString() + "T" + time_end.getText().toString() + ":00.000Z";
                messageService.updateMessage(DataHolder.getInstance().getId_message(), DataHolder.getInstance().getId_device(), messageText.getText().toString(), displayAt, hiddenAt);
                UserManager userManager = new UserManager(getContext());
                MessageManager messageManager = new MessageManager(getContext());
                messageManager.deleteMessages();
                userManager.getUser(new UserCallback() {
                    @Override
                    public void setUser(User user) {

                        messageService.getMessages(user.getUsername(), new MessageCallback() {
                            MessageManager messageManager = new MessageManager(getContext());


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


        return root;
    }

}
