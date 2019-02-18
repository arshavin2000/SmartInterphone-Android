package tn.esprit.innovotors.smartinterphone.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tn.esprit.innovotors.smartinterphone.R;
import tn.esprit.innovotors.smartinterphone.data.UserManager;
import tn.esprit.innovotors.smartinterphone.interfaces.UserCallback;
import tn.esprit.innovotors.smartinterphone.models.User;

public class ProfileFragment extends Fragment {

    private TextView email, name;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        email =  root.findViewById(R.id.email);
        name =  root.findViewById(R.id.name);

        // Inflate the layout for this fragment
        UserManager userManager = new UserManager(getContext());
        userManager.getUser(new UserCallback() {
            @Override
            public void setUser(User user) {

                email.setText(user.getEmail());
                name.setText(user.getName());

            }

            @Override
            public void setError(String msg) {

                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }


}
