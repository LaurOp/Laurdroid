package com.example.laurdroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.laurdroid.R;
import com.example.laurdroid.ViewAuxiliaries.Movie;
import com.example.laurdroid.ViewAuxiliaries.MovieAdapter;
import com.example.laurdroid.services.MoviesAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PopularMoviesActivity extends AppCompatActivity {

    public PopularMoviesActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        List<Movie> movieList = new ArrayList<>();

        String apiKey = MoviesAPI.getApiKey();
        String apiUrl = getString(R.string.popularURL, apiKey);

        Log.v("API", apiUrl);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    throw new IOException("Failed to retrieve data from API. HTTP error code: " + conn.getResponseCode());
                }

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                bufferedReader.close();
                conn.disconnect();

                String jsonResponse = responseBuilder.toString();

                JSONObject response = new JSONObject(jsonResponse);
                JSONArray results = response.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movie = results.getJSONObject(i);

                    String title = movie.getString("title");
                    Double voteAverage = movie.getDouble("vote_average");
                    String releaseDate = movie.getString("release_date");

                    JSONObject movieFields = new JSONObject();
                    movieFields.put("title", title);
                    movieFields.put("vote_average", voteAverage);
                    movieFields.put("release_date", releaseDate);

                    Movie popularMovie = new Movie(title, voteAverage, releaseDate);
                    movieList.add(popularMovie);
                }

                runOnUiThread(() -> {
                    RecyclerView recyclerView = findViewById(R.id.movie_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    MovieAdapter adapter = new MovieAdapter(movieList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PopularMoviesActivity.this));
                    recyclerView.setAdapter(adapter);
                });

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();

    }

}