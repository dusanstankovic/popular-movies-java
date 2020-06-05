package dev.dstankovic.popularmoviesjava.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import dev.dstankovic.popularmoviesjava.models.MovieDetails;
import dev.dstankovic.popularmoviesjava.repositories.MovieRepository;

public class MovieDetailsViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private int mMovie_id;
    private boolean mDidRetrieveMovieDetails;

    public MovieDetailsViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        mDidRetrieveMovieDetails = false;
    }

    public LiveData<MovieDetails> getMovieDetails() {
        return mMovieRepository.getMovieDetails();
    }

    public void searchMovieDetails(int movie_id) {
        mMovie_id = movie_id;
        mMovieRepository.searchMovieDetails(movie_id);
    }

    public LiveData<Boolean> isMovieDetailsRequestTimedOut() {
        return mMovieRepository.isMovieDetailsRequestTimedOut();
    }

    public void setDidRetrieveMovieDetails(boolean isMovieDetailsRetrieved) {
        mDidRetrieveMovieDetails = isMovieDetailsRetrieved;
    }

    public boolean didRetrieveMovieDetails() {
        return mDidRetrieveMovieDetails;
    }

    public int getMovie_id() {
        return mMovie_id;
    }

}
