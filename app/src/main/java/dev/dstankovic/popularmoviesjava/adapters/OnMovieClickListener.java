package dev.dstankovic.popularmoviesjava.adapters;

public interface OnMovieClickListener {

    void onMovieClick(int position);

    void onCategoryClick(String category);
}
