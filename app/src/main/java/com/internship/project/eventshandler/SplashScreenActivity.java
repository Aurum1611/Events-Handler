package com.internship.project.eventshandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            findViewById(R.id.progress_circular).setVisibility(View.GONE);
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }, 2 * 1000);
    }
}
