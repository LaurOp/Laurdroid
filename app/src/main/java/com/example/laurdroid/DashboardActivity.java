package com.example.laurdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laurdroid.ViewAux.HeaderFragment;
import com.example.laurdroid.services.MoviesAPI;
import com.example.laurdroid.services.Session;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DashboardActivity extends AppCompatActivity {

    private MoviesAPI moviesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        String email = Session.getInstance(getApplicationContext()).getEmailFromPrefs();

        int index = email.indexOf("@");
        String username = (index == -1) ? email : email.substring(0, index);

        TextView welcomeText = findViewById(R.id.welcomeNameText);
        welcomeText.setText(getString(R.string.welcomeName , username));

        HeaderFragment headerFragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.headerFragment);
        if (headerFragment != null) {
            ImageButton signOutButton = headerFragment.getView().findViewById(R.id.signOutHeaderButton);
            if (signOutButton != null) {
                signOutButton.setVisibility(View.GONE);
            }
        }

        Log.v("Welcome", "user is "+username);
        Log.v("Welcome", "email is "+email);

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> GoogleSignIn.getClient(DashboardActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
                .addOnCompleteListener(task -> {
                    Session.getInstance(getApplicationContext()).logoutUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(DashboardActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                }));

        Button randomMovieBut = findViewById(R.id.button1);
        Button topRatedBut = findViewById(R.id.button2);
        Button popularBut = findViewById(R.id.button3);
        Button galleryBut = findViewById(R.id.button4);

        randomMovieBut.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, RandomMovieActivity.class);
            startActivity(intent);
        });

        topRatedBut.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TopRatedActivity.class);
            startActivity(intent);
        });

        popularBut.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PopularMoviesActivity.class);
            startActivity(intent);
        });

        galleryBut.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, GalleryActivity.class);
            startActivity(intent);
        });


    }
}