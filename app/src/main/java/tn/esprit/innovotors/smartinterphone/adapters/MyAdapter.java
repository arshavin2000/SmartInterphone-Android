package tn.esprit.innovotors.smartinterphone.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.blushine.android.ui.showcase.MaterialShowcaseSequence;
import io.blushine.android.ui.showcase.MaterialShowcaseView;
import io.blushine.android.ui.showcase.ShowcaseConfig;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.DataHolder;
import tn.esprit.innovotors.smartinterphone.fragments.AddMessageFragment;
import tn.esprit.innovotors.smartinterphone.fragments.MessageDetailsFragment;
import tn.esprit.innovotors.smartinterphone.models.Device;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Device> mDataset;
    private Context context;
    private Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name1;
        private ImageButton info, send , calendar;

        private MyViewHolder(View v, Context context , Activity activity) {
            super(v);
            name1 = v.findViewById(R.id.device_name);
            info = v.findViewById(R.id.info);
            send = v.findViewById(R.id.send);
            calendar = v.findViewById(R.id.calendar);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Device> myDataset, Context context,Activity activity) {
        mDataset = myDataset;
        this.context = context;
        this.activity =activity;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.device_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v,context ,activity);
       // View v1 = v.findViewById(R.id.device_name);
       // View v2 = v.findViewById(R.id.info);
       // View v3 = v.findViewById(R.id.send);


        ShowcaseConfig config = new ShowcaseConfig(context);
        config.setDelay(0);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(activity, "6");
        sequence.setConfig(config);

        // 1
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(activity)
                        .setTarget(vh.calendar)
                        .setTitleText("Calendar")
                        .setContentText("Here you can see all your messages of one device")
                        .setDismissText("Got it")
                        .setBackgroundColor(R.color.colorAccent)
                        .build()
        );


        // We update the config so that there is half second delay between each showcase view
        config.setDelay(500);

        // 2

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(activity)
                        .setTarget(vh.info)
                        .setTitleText("Info")
                        .setContentText("Here you can see your device info")
                        .setDismissText("Got it")
                        .setBackgroundColor(R.color.colorAccent)
                        .build()
        );


        // 3
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(activity)
                        .setTarget(vh.send)
                        .setTitleText("Send")
                        .setContentText("Here you can send a message to your device")
                        .setDismissText("Got it")
                        .setBackgroundColor(R.color.colorAccent)
                        .build()
        );



        sequence.show();


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name1.setText(mDataset.get(position).getName());

        holder.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DataHolder.getInstance().setDevice(mDataset.get(position));
                MessageDetailsFragment fragment = new MessageDetailsFragment();
                FragmentManager fragmentManager =((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);



                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CENTER);
                layout.setPadding(20, 2, 20, 2);

                final TextView id = new TextView(context);
                id.setText("ID DEVICE : "+ mDataset.get(position).getId());
                id.setPadding(40, 40, 40, 40);
                id.setGravity(Gravity.CENTER);
                id.setTextSize(20);

                final TextView name = new TextView(context);
                final TextView code = new TextView(context);
                name.setText("Name : "+mDataset.get(position).getName());
                code.setText("Code : "+mDataset.get(position).getCode());
                name.setGravity(Gravity.CENTER);
                code.setGravity(Gravity.CENTER);
                name.setTextSize(20);
                code.setTextSize(20);


                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(id);
                layout.addView(name);
                layout.addView(code, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setTitle("Device Info");

                // alertDialogBuilder.setMessage("Input Student ID");


                // alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(false);

                // Setting Negative "Cancel" Button
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);



                try {
                    alertDialog.show();
                } catch (Exception e) {
                    // WindowManager$BadTokenException will be caught and the app would
                    // not display the 'Force Close' message
                    e.printStackTrace();
                }

            }

        });




        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DataHolder.getInstance().setDevice(mDataset.get(position));
                AddMessageFragment fragment = new AddMessageFragment();
                FragmentManager fragmentManager =((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


             /*   final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy-mm-dd hh:mm:ss"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        updateLabel(displayAt, myCalendar);
                        updateLabel(hiddenAt, myCalendar);

                    }

                };*/



            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void updateLabel(EditText edittext, Calendar myCalendar) {
        String myFormat = "yyyy-mm-dd hh:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
