package com.team.gptrecipie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team.gptrecipie.R;
import com.team.gptrecipie.dao.UserDatabaseHelper;
import com.team.gptrecipie.model.User;

public class Signup extends AppCompatActivity {

    private EditText emailEditText, password1EditText, password2EditText;
    private TextView loginRedirect;
    private Button signupButton;
    private UserDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getSupportActionBar().hide();
        db = new UserDatabaseHelper(this);
        db.openDB();

        emailEditText = findViewById(R.id.emailLoginEditText);
        password1EditText = findViewById(R.id.passwordLoginEditTextView);
        password2EditText = findViewById(R.id.password2EditTextView);
        signupButton = findViewById(R.id.signUpButton);
        loginRedirect = findViewById(R.id.loginRedirectTextView);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password1 = password1EditText.getText().toString();
                String password2 = password2EditText.getText().toString();

                if(email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
                } else {
                    if ( password1.equals(password2) ) {
                        User user = new User();
                        user.setEmail(email);
                        user.setPassword(password1);
                        boolean isCreated = db.createUser(user);

                        if (isCreated) {
                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isAuthenticated", true);
                            editor.putString("email", email);
                            editor.apply();

                            final Intent intent = new Intent(Signup.this, RecipeListActivity.class);
                            startActivity(intent);

                            Toast.makeText(Signup.this, "Welcome " + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Signup.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }
}