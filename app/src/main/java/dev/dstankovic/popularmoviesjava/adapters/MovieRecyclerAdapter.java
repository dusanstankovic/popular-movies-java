package dev.dstankovic.popularmoviesjava.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.dstankovic.popularmoviesjava.R;
import dev.dstankovic.popularmoviesjava.models.Movie;
import dev.dstankovic.popularmoviesjava.util.Constants;

import static dev.dstankovic.popularmoviesjava.util.Constants.IMAGE_BACKDROP_BASE_URL;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieRecyclerAdapter.class.getSimpleName();

    private static final int MOVIE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;


    private List<Movie> mMovies;
    private OnMovieClickListener mOnMovieClickListener;

    public MovieRecyclerAdapter(OnMovieClickListener mOnMovieClickListener) {
        this.mOnMovieClickListener = mOnMovieClickListener;
        mMovies = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {
            case MOVIE_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_movie_list_item, parent, false);
                return new MovieViewHolder(view, mOnMovieClickListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);
            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, mOnMovieClickListener);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_movie_list_item, parent, false);
                return new MovieViewHolder(view, mOnMovieClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if (itemViewType == MOVIE_TYPE) {
            // Picasso setup
            Picasso.get().load(IMAGE_BACKDROP_BASE_URL + mMovies.get(position).getBackdropPath())
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(((MovieViewHolder) holder).image);

            ((MovieViewHolder) holder).title.setText(mMovies.get(position).getTitle());
            ((MovieViewHolder) holder).year.setText(mMovies.get(position).getReleaseDate().substring(0, 4));
            ((MovieViewHolder) holder).socialScore.setText("");
        } else if (itemViewType == CATEGORY_TYPE) {
            Uri path = Uri.parse("android.resource://dev.dstankovic.popularmoviesjava/drawable/" + mMovies.get(position).getTitle().toLowerCase());
            Log.d(TAG, path.toString());
            // Picasso setup
            Picasso.get().load(path)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(((CategoryViewHolder) holder).mCircleImageView);

            ((CategoryViewHolder) holder).mCategoryTitle.setText(Constants.DEFAULT_MOVIE_CATEGORIES.get(position).getName());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mMovies.get(position).getVoteCount() == -1) {
            return CATEGORY_TYPE;
        }
        else if (mMovies.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        }
        else if (mMovies.get(position).getTitle().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        }
        else if (position == mMovies.size()-1
                && position != 0
                && !mMovies.get(position).getTitle().equals("EXHAUSTED...") ) {
            return LOADING_TYPE;
        }
        else {
            return MOVIE_TYPE;
        }
    }

    public void setQueryExhausted() {
        hideLoading();
        Movie exhaustedMovie = new Movie();
        exhaustedMovie.setTitle("EXHAUSTED...");
        mMovies.add(exhaustedMovie);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if (isLoading()) {
            for (Movie movie: mMovies) {
                if (movie.getTitle().equals("LOADING...")) {
                    mMovies.remove(movie);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displaySearchCategories() {
        List<Movie> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_MOVIE_CATEGORIES.size(); i++) {
            Movie movie = new Movie();
            movie.setVoteCount(-1);
            movie.setTitle(Constants.DEFAULT_MOVIE_CATEGORIES.get(i).getName());
            categories.add(movie);
        }
        mMovies = categories;
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (!isLoading()) {
            Movie movie = new Movie();
            movie.setTitle("LOADING...");
            List<Movie> loadingList = new ArrayList<>();
            loadingList.add(movie);
            mMovies = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                if (mMovies.get(mMovies.size() - 1).getTitle().equals("LOADING...")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public Movie getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }
}
