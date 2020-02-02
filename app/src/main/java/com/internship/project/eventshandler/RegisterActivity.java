package com.internship.project.eventshandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.internship.project.eventshandler.util.EmailValidator;
import com.internship.project.eventshandler.util.QRCodeHelper;

import java.io.File;
import java.io.FileOutputStream;
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

                            saveImage(bitmap);

                            try {
                                // Store image in Devise database to send image to mail
                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                                        String.valueOf(System.currentTimeMillis()), null);
                                Uri screenshotUri = Uri.parse(path);
                                final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                emailIntent1.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                emailIntent1.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                emailIntent1.setType("image/png");
                                emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "Event QR Code");
                                emailIntent1.putExtra(Intent.EXTRA_TEXT, "Keep this QR Code safe.\n" +
                                        "You may require it throughout the event.");
                                startActivity(Intent.createChooser(emailIntent1, "Send email using..."));
                            } catch (android.content.ActivityNotFoundException e) {
                                Toast.makeText(RegisterActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });

        });
    }

    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        if (myDir.mkdirs())
            Log.d("RegisterActivity", "saveImage: Directories created");
        String fileName = "Image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            this.sendBroadcast(intent);

            Toast.makeText(this, "QR Code saved to: " + root + fileName, Toast.LENGTH_LONG).show();
            out.flush();
            out.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving file to gallery", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
