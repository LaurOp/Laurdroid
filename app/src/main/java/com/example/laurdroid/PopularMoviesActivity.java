package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.laurdroid.services.MoviesAPI;

public class PopularMoviesActivity extends AppCompatActivity {

    private MoviesAPI moviesAPI;

    public PopularMoviesActivity(){
        try{
            moviesAPI = new MoviesAPI();
        }
        catch (Exception ignored){
            moviesAPI = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
    }
}