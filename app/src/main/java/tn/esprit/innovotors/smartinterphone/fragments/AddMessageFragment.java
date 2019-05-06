package tn.esprit.innovotors.smartinterphone.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.adapters.CustomDateTimePicker;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;


public class AddMessageFragment extends Fragment {


    private TextView messageText , time_start , time_end , date_start , date_end;
    private Button add ;
    private ImageButton  c1, c2;
    private CustomDateTimePicker custom;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN

        );



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
        c1 = root.findViewById(R.id.imageButton);
        c2 = root.findViewById(R.id.imageButton1);






        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (date_start.getText().toString().isEmpty() || date_end.getText().toString().isEmpty() || time_start.getText().toString().isEmpty()
                        || time_start.getText().toString().isEmpty() || messageText.getText().toString().isEmpty()) {

                    Toast.makeText(getContext(),"Please all fileds are required ! ",Toast.LENGTH_LONG).show();

                } else {
                    Device device = DataHolder.getInstance().getDevice();
                    Log.e("device", "onClick: " + device.getId());
                    final MessageService messageService = new MessageService(getContext(), getActivity());
                    final Message message = new Message();
                    message.setContent(messageText.getText().toString());
                    String displayAt = date_start.getText().toString() + "T" + date_end.getText().toString() + ":00+01:00";
                    String hiddenAt = time_start.getText().toString() + "T" + time_end.getText().toString() + ":00+01:00";
                    Log.e("time1", "onClick: " + displayAt);
                    Log.e("time2", "onClick: " + hiddenAt);


                    Log.e("date", "onClick: " + displayAt);

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

                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                        }
                    });
                    message.setDevice(device.getId());
                    messageService.addMessage(message);


                }
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

      /* date_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


            }
        });

        date_end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                chooseDate(date_end, time_end);
                custom.showDialog();

            }
        });*/
      /*  time_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              //  chooseTime(time_start);

            }
        });*/
      /*  time_end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                chooseTime(time_end);

            }
        });*/







        return root;
    }

   /* private void chooseDate(final EditText date) {
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

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                time.setText( selectedHour + ":" + selectedMinute);
            }
        }, 24, 60, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }*/

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
