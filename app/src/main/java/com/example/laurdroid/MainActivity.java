package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laurdroid.services.Session;

public class MainActivity extends AppCompatActivity {

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new Session(getApplicationContext());


        if(session.isLoggedIn()){
            // user is already logged in, start new activity or do something else
        }
        else{
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // TODO: validate email and password

                session.createLoginSession(email, password);

                // TODO: start new activity or do something else after successful login
            }
        });

    }

}