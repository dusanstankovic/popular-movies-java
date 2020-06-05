package dev.dstankovic.popularmoviesjava;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import dev.dstankovic.popularmoviesjava.models.MovieDetails;
import dev.dstankovic.popularmoviesjava.viewmodels.MovieDetailsViewModel;

import static dev.dstankovic.popularmoviesjava.util.Constants.IMAGE_BACKDROP_BASE_URL;

public class MovieDetailsActivity extends BaseActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private MovieDetailsViewModel mMovieDetailsViewModel;

    // UI components
    private ImageView mMovieImageView;
    private TextView mMovieTitle;
    private TextView mMovieOverview;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mScrollView = findViewById(R.id.parent);
        mMovieImageView = findViewById(R.id.movie_backdrop_image);
        mMovieTitle = findViewById(R.id.movie_details_title);
        mMovieOverview = findViewById(R.id.movie_details_overview);

        mMovieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);

        showProgressBar(true);

        getIncomingIntent();
        subscribeObservers();

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("movie_id")) {
            int movie_id = getIntent().getIntExtra("movie_id", 0);
            mMovieDetailsViewModel.searchMovieDetails(movie_id);
        }
    }

    private void subscribeObservers() {
        mMovieDetailsViewModel.getMovieDetails().observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                if (movieDetails != null) {
                    if (movieDetails.getId() == mMovieDetailsViewModel.getMovie_id()) {
                        setMovieDetailsProperties(movieDetails);
                        mMovieDetailsViewModel.setDidRetrieveMovieDetails(true);
                    }
                }
            }
        });
        mMovieDetailsViewModel.isMovieDetailsRequestTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimedOut) {
                if (isTimedOut && !mMovieDetailsViewModel.didRetrieveMovieDetails()) {
                    displayErrorMessage("Request has timed out. Check network connection!");
                }
            }
        });
    }

    private void setMovieDetailsProperties(MovieDetails movieDetails) {
        if (movieDetails != null) {
            // Picasso
            Picasso.get()
                    .load(IMAGE_BACKDROP_BASE_URL + movieDetails.getBackdropPath())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mMovieImageView);
            mMovieTitle.setText(movieDetails.getTitle());
            mMovieOverview.setText(movieDetails.getOverview());
        }
        showParent();
        showProgressBar(false);
    }

    private void displayErrorMessage(String errorMessage) {
        mMovieOverview.setText("");

        if (!errorMessage.equals("")) {
            mMovieTitle.setText(errorMessage);
        } else {
            mMovieTitle.setText("Error");
        }

        Picasso.get()
                .load(R.drawable.ic_launcher_background)
                .into(mMovieImageView);

        showParent();
        showProgressBar(false);
    }

    private void showParent() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}