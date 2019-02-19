package tn.esprit.innovotors.smartinterphone.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.MessageManager;
import tn.esprit.innovotors.smartinterphone.interfaces.MessageCallback;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.services.MessageService;

public class MessageFragment extends Fragment implements CalendarPickerController {

    List<CalendarEvent> eventList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_message, container, false);
        AgendaCalendarView mAgendaCalendarView = root.findViewById(R.id.agenda_calendar_view);
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MessageManager messageManager = new MessageManager(getContext());
                messageManager.getMessage(new MessageCallback() {

                    @Override
                    public void setMessages(List<Message> messages) {
                        for (Message message:messages
                        ) {

                            Log.e("test", "setMessages: "+ message );

                            Calendar startTime1 = toCalendar(message.getDisplayedAt());

                            Calendar endTime1 = toCalendar(message.getHiddenAt());
                           // endTime1.add(Calendar.MONTH, 1);
                            BaseCalendarEvent event1 = new BaseCalendarEvent(message.getContent(), message.getContent(), "Tunis",
                                    ContextCompat.getColor(getContext(), R.color.colorPrimary), startTime1, endTime1, true);
                            eventList.add(event1);
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


        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), this);
        //mockList(eventList,getContext());


        return  root;
    }



    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("item", String.format("Selected day: %s", dayItem));

    }

    @Override
    public void onEventSelected(CalendarEvent event) {

        Log.d("event", String.format("Selected event: %s", event));


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
}
