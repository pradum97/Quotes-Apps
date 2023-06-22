package com.pk.Shayari2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.pk.Shayari2.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        startActivity(new Intent(SplashScreen.this, Welcome_Activity.class));
        finish();


    }
}