package dev.dstankovic.popularmoviesjava.util;

import android.util.Log;

import java.util.List;

import dev.dstankovic.popularmoviesjava.models.Movie;

public class Testing {

    public static void printMovies(String tag,List<Movie> movies) {
        for (Movie movie: movies) {
            Log.d(tag, "printMovies: " + movie.getTitle());
        }
    }
}
