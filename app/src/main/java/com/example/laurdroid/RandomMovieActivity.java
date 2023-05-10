package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laurdroid.ViewAux.MovieDetailed;
import com.example.laurdroid.services.MoviesAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class RandomMovieActivity extends AppCompatActivity {

    private MoviesAPI moviesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_movie);

        moviesAPI = new MoviesAPI();

        generateMovie().thenAccept(movie -> {
            View movieDetailView = findViewById(R.id.movie_detail_layout);

            // Get the views in the layout
            TextView titleTextView = movieDetailView.findViewById(R.id.titleTextView);
            TextView releaseDateTextView = movieDetailView.findViewById(R.id.releaseDateTextView);
            TextView voteAverageTextView = movieDetailView.findViewById(R.id.voteAverageTextView);
            TextView voteCountTextView = movieDetailView.findViewById(R.id.voteCountTextView);
            TextView overviewTextView = movieDetailView.findViewById(R.id.overviewTextView);
            TextView genresTextView = movieDetailView.findViewById(R.id.genreTextView);

            runOnUiThread(() -> {
                titleTextView.setText(movie.getTitle());
                releaseDateTextView.setText(getString(R.string.randomReleaseDate, movie.getReleaseDate()));
                voteAverageTextView.setText(getString(R.string.randomVoteAverage, String.valueOf(movie.getVoteAverage())));
                voteCountTextView.setText(getString(R.string.randomVoteCount, String.valueOf(movie.getVoteCount())));
                overviewTextView.setText(movie.getOverview());
                genresTextView.setText(getString(R.string.randomGenres, TextUtils.join(", ", movie.getGenres())));
            });
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });


        Button regenerateButton = findViewById(R.id.regenerateButton);
        regenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }


    public CompletableFuture<MovieDetailed> generateMovie() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000) + 1;

        String apiKey = moviesAPI.getApiKey();
        AtomicReference<String> apiUrl = new AtomicReference<>(getString(R.string.randomURL, randomNumber, apiKey));

        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpURLConnection conn;
                int nrTries = 0;
                do {
                    URL url = new URL(apiUrl.get());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    nrTries++;
                    apiUrl.set(getString(R.string.randomURL, random.nextInt(10000) + 1, apiKey));
                } while (conn.getResponseCode() != 200 && nrTries < 10);

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

                String title = response.getString("title");
                double voteAverage = response.getDouble("vote_average");
                int voteCount = response.getInt("vote_count");
                String releaseDate = response.getString("release_date");
                String overview = response.getString("overview");
                JSONArray genresArray = response.getJSONArray("genres");
                List<String> genres = new ArrayList<>();
                for (int i = 0; i < genresArray.length(); i++) {
                    JSONObject genreObject = genresArray.getJSONObject(i);
                    String genreName = genreObject.getString("name");
                    genres.add(genreName);
                }
                return new MovieDetailed(title, voteAverage, voteCount, releaseDate, overview, genres);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}