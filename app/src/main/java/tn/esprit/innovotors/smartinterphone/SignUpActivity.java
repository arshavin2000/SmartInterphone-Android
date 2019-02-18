package tn.esprit.innovotors.smartinterphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

import tn.esprit.innovotors.smartinterphone.services.SignupService;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstname, lastname, username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        firstname = findViewById(R.id.name);
        lastname = findViewById(R.id.prename);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button signup = findViewById(R.id.signup);


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isEmailValid()) {
                    email.setError(getString(R.string.invalid_email));
                }
            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isPasswordValid()) {
                    password.setError(getString(R.string.invalid_password));
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTextEmpty()) {

                    clearAll();
                    if (firstname.getText().toString().isEmpty())
                        firstname.setError(getString(R.string.invalid_empty));
                    if (lastname.getText().toString().isEmpty())
                        lastname.setError(getString(R.string.invalid_empty));
                    if (username.getText().toString().isEmpty())
                        username.setError(getString(R.string.invalid_empty));
                    if (email.getText().toString().isEmpty())
                        email.setError(getString(R.string.invalid_empty));
                    if (password.getText().toString().isEmpty())
                        password.setError(getString(R.string.invalid_empty));

                } else {
                    String fullname = firstname.getText().toString().concat(lastname.getText().toString());
                    SignupService signupService = new SignupService(getApplicationContext());
                    signupService.signupWithEmailAndPassword(email.getText().toString(), fullname, username.getText().toString(), password.getText().toString());
                }
            }
        });
    }


    private boolean isTextEmpty() {
        return firstname.getText().toString().trim().isEmpty() || lastname.getText().toString().trim().isEmpty() ||
                email.getText().toString().trim().isEmpty() || username.getText().toString().trim().isEmpty()
                || password.getText().toString().trim().isEmpty();
    }

    private boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
    }

    private boolean isPasswordValid() {
        return password.getText().toString().length() > 8;
    }

    private void clearAll() {
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        password.setText("");
        username.setText("");
    }
}
