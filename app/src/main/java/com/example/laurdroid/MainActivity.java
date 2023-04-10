package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.laurdroid.services.Session;

public class MainActivity extends AppCompatActivity {

    Session session;
    public static AppDatabase appDatabase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = Session.getInstance(getApplicationContext());
        appDatabase = AppDatabase.getDatabase(getApplicationContext());

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);

    }


    public void onLoginButtonClick(View view) throws InterruptedException {
//        ImageView loadingSpinner = findViewById(R.id.loading_spinner);
//        AnimatedVectorDrawableCompat spinnerDrawable = (AnimatedVectorDrawableCompat) loadingSpinner.getDrawable();
//        spinnerDrawable.start();
//        Thread.sleep(2000);
//        spinnerDrawable.stop();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                progressBar.setVisibility(View.GONE);
            }
        }, 500);

    }
}