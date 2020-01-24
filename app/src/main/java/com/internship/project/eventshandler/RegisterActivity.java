package com.internship.project.eventshandler;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.internship.project.eventshandler.util.EmailValidator;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.register).setOnClickListener(v -> {
            String firstName = ((TextView) findViewById(R.id.first_name)).getText().toString();
            String lastName = ((TextView) findViewById(R.id.last_name)).getText().toString();
            String email = ((TextView) findViewById(R.id.email_address)).getText().toString();

            if (!EmailValidator.isEmailValid(email))
                return;

            HashMap<String, String> userDataStringHashMap = new HashMap<>();
            userDataStringHashMap.put("first_name", firstName);
            userDataStringHashMap.put("last_name", lastName);
            userDataStringHashMap.put("email", email);

            FirebaseDatabase.getInstance().getReference("AllUsers")
                    .push().setValue(userDataStringHashMap)
                    .addOnSuccessListener(aVoid -> {
                        //TODO: ADD code to generate and send QR cod
                    });

        });
    }

}
