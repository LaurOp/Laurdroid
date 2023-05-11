package com.example.laurdroid.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laurdroid.services.AppDatabase;
import com.example.laurdroid.R;
import com.example.laurdroid.services.Session;

public class MainActivity extends AppCompatActivity {

    Session session;
    public static AppDatabase appDatabase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = Session.getInstance(getApplicationContext());
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRegisterButtonClick(View view) {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }, 500);

    }


    public void onLoginButtonClick(View view) {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }, 500);

    }
}