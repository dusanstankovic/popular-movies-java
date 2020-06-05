package dev.dstankovic.popularmoviesjava.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import dev.dstankovic.popularmoviesjava.AppExecutorService;
import dev.dstankovic.popularmoviesjava.models.Movie;
import dev.dstankovic.popularmoviesjava.models.MovieDetails;
import dev.dstankovic.popularmoviesjava.requests.responses.MovieSearchResponse;
import retrofit2.Call;
import retrofit2.Response;

import static dev.dstankovic.popularmoviesjava.util.Constants.API_KEY;
import static dev.dstankovic.popularmoviesjava.util.Constants.NETWORK_TIMEOUT;

public class MovieApiClient {

    private static final String TAG = MovieApiClient.class.getSimpleName();
    private static MovieApiClient instance;
    private MutableLiveData<List<Movie>> mMovies;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;
    private MutableLiveData<MovieDetails> mMovieDetails;
    private RetrieveMovieDetailsRunnable mRetrieveMovieDetailsRunnable;
    private MutableLiveData<Boolean> mMovieDetailsRequestTimedOut = new MutableLiveData<>();

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMovieDetails = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public LiveData<MovieDetails> getMovieDetails() {
        return mMovieDetails;
    }

    public LiveData<Boolean> isMovieDetailsRequestTimedOut() {
        return mMovieDetailsRequestTimedOut;
    }

    public void searchMoviesApi(String query, int pageNumber) {
        if (mRetrieveMoviesRunnable != null) {
            mRetrieveMoviesRunnable = null;
        }

        mRetrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future handler = AppExecutorService.getInstance()
                .getScheduledExecutorService()
                .submit(mRetrieveMoviesRunnable);

        AppExecutorService.getInstance()
                .getScheduledExecutorService()
                .schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchMovieDetails(int movie_id) {
        if (mRetrieveMovieDetailsRunnable != null) {
            mRetrieveMovieDetailsRunnable = null;
        }
        mRetrieveMovieDetailsRunnable = new RetrieveMovieDetailsRunnable(movie_id);

        final Future handler = AppExecutorService.getInstance()
                .getScheduledExecutorService()
                .submit(mRetrieveMovieDetailsRunnable);

        mMovieDetailsRequestTimedOut.setValue(false);

        AppExecutorService.getInstance()
                .getScheduledExecutorService()
                .schedule(new Runnable() {
                    @Override
                    public void run() {
                        // let the user know its timed out
                        mMovieDetailsRequestTimedOut.postValue(true);
                        handler.cancel(true);
                    }
                }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) { // SUCCESS
                    List<Movie> moviesList =
                            new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        mMovies.postValue(moviesList);
                    } else {
                        List<Movie> currentMovies = mMovies.getValue();
                        currentMovies.addAll(moviesList);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String errorMessage = response.errorBody().string();
                    Log.e(TAG, "run: " + errorMessage);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return ServiceGenerator.getMovieApi().searchMovie(API_KEY, query, pageNumber);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancelling search request...");
            cancelRequest = true;
        }
    }

    private class RetrieveMovieDetailsRunnable implements Runnable {

        int movie_id;
        private boolean cancelRequest;

        public RetrieveMovieDetailsRunnable(int movie_id) {
            this.movie_id = movie_id;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovieDetails(movie_id).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) { // SUCCESS
                    MovieDetails movieDetails = (MovieDetails) response.body();
                    mMovieDetails.postValue(movieDetails);
                } else {
                    String errorMessage = response.errorBody().string();
                    Log.e(TAG, "run: " + errorMessage);
                    mMovieDetails.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovieDetails.postValue(null);
            }
        }

        private Call<MovieDetails> getMovieDetails(int movie_id) {
            return ServiceGenerator.getMovieApi().getMovieDetails(movie_id, API_KEY);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: cancelling search request...");
            cancelRequest = true;
        }
    }

    public void cancelRequest() {
        if (mRetrieveMoviesRunnable != null) {
            mRetrieveMoviesRunnable.cancelRequest();
        }
        if (mRetrieveMovieDetailsRunnable != null) {
            mRetrieveMovieDetailsRunnable.cancelRequest();
        }
    }
}
