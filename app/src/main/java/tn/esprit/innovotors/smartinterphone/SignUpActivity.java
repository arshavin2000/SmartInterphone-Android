package tn.esprit.innovotors.smartinterphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tn.esprit.innovotors.smartinterphone.services.SignupService;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstname , lastname , username , email , password ;
    private Button signup ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstname = findViewById(R.id.name);
        lastname = findViewById(R.id.prename);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname = firstname.getText().toString().concat(lastname.getText().toString());
                SignupService signupService = new SignupService(getApplicationContext());
                signupService.signupWithEmailAndPassword(email.getText().toString(),fullname,username.getText().toString(),password.getText().toString());

            }
        });
    }
}
