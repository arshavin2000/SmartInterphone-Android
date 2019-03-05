package tn.esprit.innovotors.smartinterphone.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;


public class AddMessageFragment extends Fragment {


    private EditText messageText , time_start , time_end , date_start , date_end;
    private Button add;



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
        add = root.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device =DataHolder.getInstance().getDevice();
                Log.e("device", "onClick: "+ device.getId() );
                MessageService messageService = new MessageService(getContext());
                final Message message = new Message();
                message.setContent(messageText.getText().toString());
                String displayAt = date_start.getText().toString() + "T"+ time_start.getText().toString()+":00.000Z";
                String hiddenAt = date_end.getText().toString() + "T"+ time_end.getText().toString()+":00.000Z";

                Log.e("date", "onClick: " + displayAt );

                //  message.setDisplayedAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(displayAt));
                //  message.setHiddenAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(hiddenAt));
                message.setStartDate(displayAt);
                message.setEndDate(hiddenAt);

                UserManager userManager = new UserManager(getContext());
                userManager.getUser(new UserCallback() {
                    @Override
                    public void setUser(User user) {
                        message.setUser(user);
                    }

                    @Override
                    public void setError(String msg) {

                        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();

                    }
                });
                message.setDevice(device);
                messageService.addMessage(message);
            }
        });


        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(date_start);
            }
        });


        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(date_end);
            }
        });

        time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(time_start);

            }
        });

        time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTime(time_end);

            }
        });



        return root;
    }

    private void chooseDate(final EditText date) {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());

                        date.setText(dateString); // set the date
                    }
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }


    public void chooseTime(final EditText time){

        TimePicker timePicker  = new TimePicker(getContext());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //Display the new time to app interface
                time.setText(hourOfDay + ":" + minute);
            }
        });
    }


}
