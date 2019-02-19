package tn.esprit.innovotors.smartinterphone.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;
import tn.esprit.innovotors.smartinterphone.models.Message;
import tn.esprit.innovotors.smartinterphone.models.User;
import tn.esprit.innovotors.smartinterphone.services.MessageService;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Device> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView name1;
        private ImageButton info, send;

        private MyViewHolder(View v) {
            super(v);
            name1 = v.findViewById(R.id.device_name);
            info = v.findViewById(R.id.info);
            send = v.findViewById(R.id.send);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Device> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.device_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name1.setText(mDataset.get(position).getName());
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                final TextView id = new TextView(context);
                id.setText(mDataset.get(position).getId());
                id.setPadding(40, 40, 40, 40);
                id.setGravity(Gravity.CENTER);
                id.setTextSize(20);

                final TextView name = new TextView(context);
                final TextView code = new TextView(context);
                name.setText(mDataset.get(position).getName());
                code.setText(mDataset.get(position).getCode());

                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(id);
                layout.addView(name);
                layout.addView(code, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setTitle("ok");
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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                final EditText content = new EditText(context);
                content.setHint("");
                content.setPadding(40, 40, 40, 40);
                content.setGravity(Gravity.CENTER);
                content.setTextSize(20);
                final Calendar myCalendar = Calendar.getInstance();


                final EditText displayAt = new EditText(context);
                final EditText hiddenAt = new EditText(context);

                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

                };

                displayAt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(context, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                hiddenAt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(context, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });


                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(content);
                layout.addView(displayAt);
                layout.addView(hiddenAt, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setTitle("ok");
                // alertDialogBuilder.setMessage("Input Student ID");


                // alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(false);

                // Setting Negative "Cancel" Button
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MessageService messageService = new MessageService(context);
                        final Message message = new Message();
                        message.setContent(content.getText().toString());
                        try {
                            message.setDisplayedAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(displayAt.getText().toString()));
                            message.setHiddenAt(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(hiddenAt.getText().toString()));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        UserManager userManager = new UserManager(context);
                        userManager.getUser(new UserCallback() {
                            @Override
                            public void setUser(User user) {
                                message.setUser(user);
                            }

                            @Override
                            public void setError(String msg) {

                                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();

                            }
                        });
                        message.setDevice(mDataset.get(position));
                        messageService.addMessage(message);


                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();

                try {
                    alertDialog.show();
                } catch (Exception e) {
                    // WindowManager$BadTokenException will be caught and the app would
                    // not display the 'Force Close' message
                    e.printStackTrace();
                }

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
