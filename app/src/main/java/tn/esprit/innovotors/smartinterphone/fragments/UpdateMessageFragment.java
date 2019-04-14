package tn.esprit.innovotors.smartinterphone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tn.esprit.innovotors.smartinterphone.R;

public class UpdateMessageFragment extends Fragment {

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
        return root;
    }

    }
