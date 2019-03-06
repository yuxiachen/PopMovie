package com.example.android.popmovies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popmovies.R;
import com.example.android.popmovies.adapter.MovieAdapter;
import com.example.android.popmovies.data.FavoriteMovieDatabase;
import com.example.android.popmovies.model.Movie;
import com.example.android.popmovies.utils.JsonUtils;
import com.example.android.popmovies.utils.NetworkUtils;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Movie>>,
        MovieAdapter.MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressIndicator;
    private MovieAdapter mMovieAdapter;
    private final String ERROR_LOG_TAG = "doInBackground";
    //A constant to save and store the sort
    private final String SORT_ORDER = "sort_by";
    public static final String MOVIE_DATA = "movie_data";
    private final String sortByPopular = "popular";
    private final String sortByTopRated = "top_rated";
    private final String sortByFavorite = "favorite";
    private final String LIST_STATE = "list_state";
    private Parcelable mListState;

    //A number to identify the loader
    private static final int LOADER_ID = 10;

    private String sort = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = findViewById(R.id.error_message);
        mProgressIndicator =findViewById(R.id.progress);

        //Using the GridLayoutManager to populate the recyclerview in a grid, with 2 row
        GridLayoutManager layoutManager= new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null){
            sort = savedInstanceState.getString(SORT_ORDER);
            mListState = savedInstanceState.getParcelable(LIST_STATE);
        }

        //once all the views are setup, load the data
        loadMovieData();
    }

    /**
     * This method will using the current sort order to fetch the data
     */

    private void loadMovieData(){
        //new a adapter to link the movie data with views
        mMovieAdapter = new MovieAdapter(this);
        //Setting the adapter attaches it to the recyclerview in our layout
        mRecyclerView.setAdapter(mMovieAdapter);
        //Pass the loader id and create a loader or restart a loader
        if (sort == sortByPopular || sort == sortByTopRated)
        {
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<List<Movie>> movieLoader = loaderManager.getLoader(LOADER_ID);
            if(movieLoader == null){
                loaderManager.initLoader(LOADER_ID, null, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, null, this);
            }
        }
        else if (sort == sortByFavorite)
        {
            FavoriteMovieDatabase mDb = FavoriteMovieDatabase.getInstance(getApplicationContext());
            setupViewModel();
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovieData(movies);
            }
        });
    }


    /**
     * This method override the onclick method in order to handle recyclerview item clicks, build
     * the explicit intent to open the detail activity
     *
     * @param movie the movie that was clicked
     */
    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_DATA, movie);
        startActivity(intent);
    }

    /**
     * Instantiate and return a new loader with the given loader id
     * @param i the loader id used to create the loader
     * @param bundle any argument supplied by the caller, we set null here
     * @return return a new loader instance that is ready to loading
     */
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<List<Movie>>(this){

            //help cache the movie data
            List<Movie> movieList;

            //subclasses of AsynTaskLoader
            @Override
            protected void onStartLoading() {

                if(movieList != null){
                    deliverResult(movieList);
                } else {
                    mProgressIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {

                URL movieRequestUrl = NetworkUtils.buildURL(sort);

                try{
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    List<Movie> movieData = JsonUtils.parseMovieJson(jsonMovieResponse);
                    return movieData;
                } catch (Exception e) {
                    Log.e(ERROR_LOG_TAG, e.getMessage(), e);
                    return null;
                }

            }

            /**
             *  Sends the results of the load to the registered listener.
             * @param data the result of the loader
             */
            @Override
            public void deliverResult(List<Movie> data) {
                movieList = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Called when a previously created loader has finished its load.
     * @param loader the loader that has finished
     * @param data the data generated by the loader
     */
    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mProgressIndicator.setVisibility(View.INVISIBLE);

        if (data != null) {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mMovieAdapter.setMovieData(data);
            if (mListState != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                mListState = null;
            }

        } else {

            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Called when a previously created is being reset
     * @param loader the loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    /**
     * Create and inflate the menu layout
     * @param menu
     * @return return a boolean to show if the menu will be display in the toolbar successfully or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by, menu);
        MenuItem sortMenuItem = menu.findItem(R.id.sort_by);
        getMenuInflater().inflate(R.menu.sort_by_options, sortMenuItem.getSubMenu());
        return true;
    }

    /** Handle the condition when the corresponding menu item is clicked
     *
     * @param item the menu item this is clicked
     * @return return a boolean to show the click is handled successfully or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortOrderSelected = item.getItemId();

        if (sortOrderSelected == R.id.popular){
            sort = sortByPopular;
            mListState = null;
            loadMovieData();
            return true;
        } else if (sortOrderSelected == R.id.top_rated){
            sort = sortByTopRated;
            mListState = null;
            loadMovieData();
            return true;
        }
        else if (sortOrderSelected == R.id.favorite_movies) {
            sort = sortByFavorite;
            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * save the instance state when the activity is temporarily destroyed.
     * @param outState activity state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER, sort);
        outState.putParcelable(LIST_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }
}

