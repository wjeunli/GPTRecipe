package com.team.gptrecipie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team.gptrecipie.R;
import com.team.gptrecipie.dao.UserDatabaseHelper;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupRedirect;
    private UserDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getSupportActionBar().hide();

        db = new UserDatabaseHelper(this);
        db.openDB();
        emailEditText = findViewById(R.id.emailLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        signupRedirect = findViewById(R.id.signupRedirectTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_LONG).show();
                } else {
                    boolean isAuthenticated = db.authenticateUser(email, password);
                    if (isAuthenticated) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isAuthenticated", true);
                        editor.putString("email", email);
                        editor.apply();

                        final Intent intent = new Intent(Login.this, RecipeListActivity.class);
                        startActivity(intent);

                        Toast.makeText(Login.this, "Welcome " + email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this, "Email or password incorrect. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}