package com.internship.project.eventshandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.internship.project.eventshandler.util.EmailValidator;
import com.internship.project.eventshandler.util.QRCodeHelper;

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

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllUsers");
            String Uid = databaseReference.push().getKey();
            if (Uid != null)
                databaseReference.child(Uid).setValue(userDataStringHashMap)
                        .addOnSuccessListener(aVoid -> {

                            Bitmap bitmap = QRCodeHelper
                                    .newInstance(this)
                                    .setContent(Uid)
                                    .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                                    .setMargin(2)
                                    .getQRCOde();

                            try {
                                // Store image in Devise database to send image to mail
                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                                Uri screenshotUri = Uri.parse(path);
                                final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                emailIntent1.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                emailIntent1.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                emailIntent1.setType("image/png");
                                startActivity(Intent.createChooser(emailIntent1, "Send email using..."));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });

        });
    }

}
