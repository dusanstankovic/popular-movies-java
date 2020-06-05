package dev.dstankovic.popularmoviesjava.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.dstankovic.popularmoviesjava.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title, year, socialScore;
    ImageView image;
    OnMovieClickListener mOnMovieClickListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieClickListener mOnMovieClickListener) {
        super(itemView);

        this.mOnMovieClickListener = mOnMovieClickListener;

        title = itemView.findViewById(R.id.movie_title);
        year = itemView.findViewById(R.id.movie_release_year);
        socialScore = itemView.findViewById(R.id.movie_blank_social_score);
        image = itemView.findViewById(R.id.movie_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mOnMovieClickListener.onMovieClick(getAdapterPosition());
    }
}
