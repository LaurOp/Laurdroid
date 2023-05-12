package com.example.laurdroid.ViewAuxiliaries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laurdroid.Models.Movie;
import com.example.laurdroid.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.voteAverage.setText(String.valueOf(movie.getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView releaseDate;
        TextView voteAverage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            releaseDate = itemView.findViewById(R.id.movie_release_date);
            voteAverage = itemView.findViewById(R.id.movie_vote_average);
        }

        public void bind(JSONObject movie) throws JSONException {
            title.setText(movie.getString("title"));
            voteAverage.setText(String.valueOf(movie.getDouble("vote_average")));
            releaseDate.setText(movie.getString("release_date"));
        }
    }
}

