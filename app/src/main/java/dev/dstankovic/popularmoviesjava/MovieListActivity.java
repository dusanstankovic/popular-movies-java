package dev.dstankovic.popularmoviesjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.dstankovic.popularmoviesjava.adapters.MovieRecyclerAdapter;
import dev.dstankovic.popularmoviesjava.adapters.OnMovieClickListener;
import dev.dstankovic.popularmoviesjava.models.Movie;
import dev.dstankovic.popularmoviesjava.util.VerticalSpacingItemDecorator;
import dev.dstankovic.popularmoviesjava.viewmodels.MovieListViewModel;

public class MovieListActivity extends BaseActivity implements OnMovieClickListener {

    private static final String TAG = MovieListActivity.class.getSimpleName();
    private MovieListViewModel mMovieListViewModel;
    private RecyclerView mRecyclerView;
    private MovieRecyclerAdapter mAdapter;
    public SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = findViewById(R.id.movie_list);
        mSearchView = findViewById(R.id.search_view);

        mMovieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
        if (!mMovieListViewModel.isViewingMovies()) {
            // display categories
            displaySearchCategories();
        }

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void initRecyclerView() {
        mAdapter = new MovieRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // search the next page
                    mMovieListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.displayLoading();
                mSearchView.clearFocus();
                mMovieListViewModel.searchMoviesApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers() {
        mMovieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies != null) {
                    if (mMovieListViewModel.isViewingMovies()) {
                        mMovieListViewModel.setIsPerformingQuery(false);
                        mAdapter.setMovies(movies);
                    }
                }
            }
        });
        mMovieListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExhausted) {
                if (isExhausted) {
                    Log.d(TAG, "onChanged: the query is exhausted");
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void displaySearchCategories() {
        mMovieListViewModel.setIsViewingMovies(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie_id", mAdapter.getSelectedMovie(position).getId());
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mMovieListViewModel.searchMoviesApi(category, 1);
        mSearchView.clearFocus();
    }

    @Override
    public void onBackPressed() {
        if (mMovieListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            displaySearchCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}