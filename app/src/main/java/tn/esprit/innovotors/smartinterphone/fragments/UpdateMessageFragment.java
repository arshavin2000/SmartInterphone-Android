package tn.esprit.innovotors.smartinterphone.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.adapters.CustomDateTimePicker;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;

public class UpdateMessageFragment extends Fragment {

    private TextView messageText, time_start, time_end, date_start, date_end;
    private Button update , delete;
    private ImageButton c1 , c2;
    private CustomDateTimePicker custom;



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
        delete = root.findViewById(R.id.delete);
        delete.setVisibility(View.VISIBLE);

        messageText.setHeight(100);

        update.setText(R.string.update);
        c1 = root.findViewById(R.id.imageButton);
        c2 = root.findViewById(R.id.imageButton1);


        date_end.setText(DataHolder.getInstance().getStartTime());
        time_end.setText(DataHolder.getInstance().getEndTime());
        messageText.setText(DataHolder.getInstance().getContent());
        date_start.setText(DataHolder.getInstance().getStartDate());
        time_start.setText(DataHolder.getInstance().getEndDate());




        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MessageService messageService = new MessageService(getContext(), getActivity());
                messageService.deleteMessage(DataHolder.getInstance().getId_message());


            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MessageService messageService = new MessageService(getContext(), getActivity());


                String displayAt = date_start.getText().toString() + "T"+ date_end.getText().toString()+":00+01:00";
                String hiddenAt = time_start.getText().toString() + "T"+ time_end.getText().toString()+":00+01:00";
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


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseDate(date_start,date_end);
                custom.showDialog();

            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseDate(time_start,time_end);
                custom.showDialog();

            }
        });


        return root;
    }





    void  chooseDate(final TextView textView, final TextView time)
    {
        custom = new CustomDateTimePicker(getActivity(),
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {

                        String month;
                        if(monthNumber+1 <10)
                        {
                            month = "0"+String.valueOf(monthNumber+1);
                        }else{
                            month =  String.valueOf(monthNumber+1);
                        }

                        String day ;
                        if(calendarSelected
                                .get(Calendar.DAY_OF_MONTH )<10)
                        {
                            day = "0" + String.valueOf(calendarSelected
                                    .get(Calendar.DAY_OF_MONTH ));
                        }
                        else{
                            day =  String.valueOf(calendarSelected
                                    .get(Calendar.DAY_OF_MONTH ));
                        }

                        String hour ;
                        if(hour24<10)
                        {
                            hour ="0"+ String.valueOf(hour24);
                        }else{
                            hour = String.valueOf(hour24);
                        }
                        String minute ;
                        if(min<10)
                        {
                            minute ="0"+ String.valueOf(min);

                        }else
                        {
                            minute = String.valueOf(min);
                        }


                        textView
                                .setText(year
                                        + "-" + month + "-" + day
                                );


                        time.setText(hour + ":" + minute
                        );
                    }


                    @Override
                    public void onCancel() {

                    }
                });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
        custom.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom.setDate(Calendar.getInstance());
    }


}
