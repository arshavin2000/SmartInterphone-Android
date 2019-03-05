package tn.esprit.innovotors.smartinterphone.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.adapters.MyAdapter;
import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.interfaces.DeviceCallback;
import tn.esprit.innovotors.smartinterphone.models.Device;
import tn.esprit.innovotors.smartinterphone.services.DeviceService;

public class DeviceFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_device, container, false);

        recyclerView = root.findViewById(R.id.my_recycler_view);
        add = root.findViewById(R.id.add);
        final DeviceService deviceService = new DeviceService(getContext());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

                LinearLayout layout = new LinearLayout(getContext());
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(parms);

                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(2, 2, 2, 2);

                final EditText id = new EditText(getContext());
                id.setHint("ID Device");
                id.setPadding(40, 40, 40, 40);
                id.setGravity(Gravity.CENTER);
                id.setTextSize(20);

                final EditText name = new EditText(getContext());
                final EditText code = new EditText(getContext());
                name.setHint("Device Name");
                code.setHint("Device Code");

                LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv1Params.bottomMargin = 5;
                layout.addView(id);
                layout.addView(name);
                layout.addView(code, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setTitle("Add Device");
                // alertDialogBuilder.setMessage("Input Student ID");


                // alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(false);

                // Setting Negative "Cancel" Button
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                // Setting Positive "OK" Button
                alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Device device = new Device();
                        device.setId(id.getText().toString());
                        device.setName(name.getText().toString());
                        device.setCode(code.getText().toString());
                        deviceService.addDevice(device);
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
         deviceService.getDevices(new DeviceCallback() {
            @Override
            public void setDevices(List<Device> d) {

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                mAdapter = new MyAdapter(d,getContext());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void setError(String msg) {

                Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();

            }
        });


        return  root;
    }
}
