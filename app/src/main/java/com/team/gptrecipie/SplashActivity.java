package com.team.gptrecipie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.team.gptrecipie.activity.RecipeListActivity;
import com.team.gptrecipie.activity.Signup;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        SharedPreferences userSession = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isAuthenticated = userSession.getBoolean("isAuthenticated", false);

        final Intent intent;
        if (isAuthenticated) {
            intent = new Intent(SplashActivity.this, RecipeListActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, Signup.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}