package dev.dstankovic.popularmoviesjava.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dev.dstankovic.popularmoviesjava.models.Movie;
import dev.dstankovic.popularmoviesjava.repositories.MovieRepository;

public class MovieListViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private boolean isViewingMovies;
    private boolean isPerformingQuery;

    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        isPerformingQuery = false;
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieRepository.getMovies();
    }

    public void searchMoviesApi(String query, int pageNumber) {
        isViewingMovies = true;
        isPerformingQuery = true;
        mMovieRepository.searchMoviesApi(query, pageNumber);
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mMovieRepository.isQueryExhausted();
    }

    public void searchNextPage() {
        if (!isPerformingQuery && isViewingMovies && !isQueryExhausted().getValue()) {
            mMovieRepository.searchNextPage();
        }
    }

    public boolean isViewingMovies() {
        return isViewingMovies;
    }

    public void setIsViewingMovies(boolean isViewingMovies) {
        this.isViewingMovies = isViewingMovies;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery) {
        this.isPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public boolean onBackPressed() {
        if (isPerformingQuery) {
            // cancel query
            mMovieRepository.cancelRequest();
            isPerformingQuery = false;
        }
        if (isViewingMovies) {
            setIsViewingMovies(false);
            return false;
        }
        return true;
    }
}
