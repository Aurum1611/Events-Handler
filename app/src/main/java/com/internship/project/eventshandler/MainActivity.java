package com.internship.project.eventshandler;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.register)
                .setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        findViewById(R.id.scanner)
                .setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QRScannerActivity.class)));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
    }
}
