package tn.esprit.innovotors.smartinterphone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


import io.realm.Realm;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;

public class MessageFragment extends Fragment implements CalendarPickerController {

    List<CalendarEvent> eventList = new ArrayList<>();
    Calendar minDate;
    Calendar maxDate;
    AgendaCalendarView mAgendaCalendarView;
    List<String> devices = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    List<String> startTime = new ArrayList<>();
    List<String> endTime = new ArrayList<>();
    List<String> startDate = new ArrayList<>();
    List<String> endDate = new ArrayList<>();
    List<String> content = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_message, container, false);
         mAgendaCalendarView = root.findViewById(R.id.agenda_calendar_view);

         minDate = Calendar.getInstance();
         maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, 0);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MessageManager messageManager = new MessageManager(getContext());
                messageManager.getMessage(new MessageCallback() {
                    int i =-1;

                    @Override
                    public void setMessages(List<Message> messages) {
                        Log.e("size", "setMessages: " + messages.size() );
                        for (Message message:messages
                        ) {

                            Log.e("test", "setMessages: "+ message.getDisplayedAt() );

                            Calendar startTime1 = toCalendar(message.getDisplayedAt());

                            Log.e("start", "setMessages: " + startTime1 );
                            i++;

                            Calendar endTime1 = toCalendar(message.getHiddenAt());
                           // endTime1.add(Calendar.MONTH, 1);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(message.getContent()).append("\n").append(" Start Display  : ").append(message.getDisplayedAt().toString().split(" ")[3].split(":")[0]+":"+message.getDisplayedAt().toString().split(" ")[3].split(":")[1]);
                            BaseCalendarEvent event1 = new BaseCalendarEvent(stringBuilder.toString(), message.getId() + message.getDevice(), "Esprit",
                                    ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary), startTime1, endTime1, true);
                            event1.setId(i);
                            eventList.add(event1);
                            devices.add(message.getDevice());
                            ids.add(message.getId());
                            startTime.add(message.getDisplayedAt().toString().split(" ")[3].split(":")[0]+":"+message.getDisplayedAt().toString().split(" ")[3].split(":")[1]);
                            endTime.add(message.getHiddenAt().toString().split(" ")[3].split(":")[0]+":"+message.getHiddenAt().toString().split(" ")[3].split(":")[1]);
                            content.add(message.getContent());
                            startDate.add(message.getDisplayedAt().toString().split(" ")[5]+"-"+convertMonth(message.getDisplayedAt().toString().split(" ")[1])+"-"+message.getDisplayedAt().toString().split(" ")[2]);
                            endDate.add(message.getDisplayedAt().toString().split(" ")[5]+"-"+convertMonth(message.getHiddenAt().toString().split(" ")[1])+"-"+message.getHiddenAt().toString().split(" ")[2]);

                            Log.e("size1", "setMessages: "+ eventList.size() );


                        }
                    }

                    @Override
                    public void setError(String msg) {

                    }
                });
            }

        });
        thread.start();


        //mockList(eventList,getContext());


        return  root;
    }

    @Override
    public void onStart() {
        try{
//block
            mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);

        }catch(ConcurrentModificationException ex ){
//thus handling my code over here
            Toast.makeText(getContext(),"Waiting for doaloading new messages ...",Toast.LENGTH_LONG).show();
        }

        super.onStart();
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("item", String.format("Selected day: %s", dayItem));

    }

    @Override
    public void onEventSelected( CalendarEvent event) {

        Log.d("event", String.format("Selected event: %s", event.getId()));


        DataHolder.getInstance().setId_device(devices.get((int) event.getId()));
        DataHolder.getInstance().setId_message(ids.get((int) event.getId()));
        DataHolder.getInstance().setStartTime(startTime.get((int) event.getId()));
        DataHolder.getInstance().setEndTime(endTime.get((int) event.getId()));
        DataHolder.getInstance().setStartDate(startDate.get((int) event.getId()));
        DataHolder.getInstance().setEndDate(endDate.get((int) event.getId()));
        DataHolder.getInstance().setContent(content.get((int) event.getId()));


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new UpdateMessageFragment());
        fragmentTransaction.commit();



    }

    @Override
    public void onScrollToDate(Calendar calendar) {


    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    String convertMonth(String input2)
    {
        switch(input2) {
            case "january":
            case "Jan":
                input2 = "01";
                break;

            case "febuary":
            case "Feb":
                input2 = "02";
                break;

            case "march":
            case "Mar":
                input2 = "03";
                break;

            case "april":
            case "Apr":
                input2 = "04";
                break;

            case "May":
                input2 = "05";
                break;

            case "june":
            case "Jun":
                input2 = "06";
                break;

            case "july":
            case "Jul":
                input2 = "07";
                break;

            case "august":
            case "Aug":
                input2 = "08";
                break;

            case "september":
            case "Sep":
            case "Sept":
                input2 = "09";
                break;

            case "october":
            case "Oct":
                input2 = "10";
                break;

            case "november":
            case "Nov":
                input2 = "11";
                break;

            case "december":
            case "Dec":
                input2 = "12";
                break;
        }

        return  input2;
    }
}
