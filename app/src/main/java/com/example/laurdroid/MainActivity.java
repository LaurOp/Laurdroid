package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laurdroid.services.Session;

public class MainActivity extends AppCompatActivity {

    Session session;
    public static AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = Session.getInstance(getApplicationContext());
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        if(session.isLoggedIn()){
            // user is already logged in, start new activity or do something else
        }
        else{
            Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRegisterButtonClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void onLoginButtonClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}