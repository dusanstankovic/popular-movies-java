package dev.dstankovic.popularmoviesjava.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import dev.dstankovic.popularmoviesjava.models.Movie;
import dev.dstankovic.popularmoviesjava.models.MovieDetails;
import dev.dstankovic.popularmoviesjava.requests.MovieApiClient;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient mMovieApiClient;
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Movie>> mMovieListMediator = new MediatorLiveData<>();

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    public MovieRepository() {
        mMovieApiClient = MovieApiClient.getInstance();
        initMediators();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieListMediator;
    }

    private void initMediators() {
        LiveData<List<Movie>> source = mMovieApiClient.getMovies();
        mMovieListMediator.addSource(source, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    mMovieListMediator.setValue(movies);
                    doneQuery(movies);
                } else {
                    // search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Movie> movies) {
        if (movies != null) {
            if (movies.size() % 20 != 0) {
                // no more results to be returned
                mIsQueryExhausted.setValue(true);
            }
        }
        else {
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted() {
        return mIsQueryExhausted;
    }

    public LiveData<MovieDetails> getMovieDetails() {
        return mMovieApiClient.getMovieDetails();
    }

    public LiveData<Boolean> isMovieDetailsRequestTimedOut() {
        return mMovieApiClient.isMovieDetailsRequestTimedOut();
    }

    public void searchMoviesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        mMovieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchMovieDetails(int movie_id) {
        mMovieApiClient.searchMovieDetails(movie_id);
    }

    public void searchNextPage() {
        searchMoviesApi(mQuery, mPageNumber + 1);
    }

    public void cancelRequest() {
        mMovieApiClient.cancelRequest();
    }
}
