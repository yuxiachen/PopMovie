package com.example.android.popmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popmovies.MovieExecutor;
import com.example.android.popmovies.R;
import com.example.android.popmovies.adapter.ReviewAdapter;
import com.example.android.popmovies.adapter.TrailerAdapter;
import com.example.android.popmovies.data.FavoriteMovieDatabase;
import com.example.android.popmovies.model.Movie;
import com.example.android.popmovies.model.Review;
import com.example.android.popmovies.model.Trailer;
import com.example.android.popmovies.utils.JsonUtils;
import com.example.android.popmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;


import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks, TrailerAdapter.TrailerAdapterOnClickHandler
{

    private static final String MOVIE_DATA = "movie_data";
    private final String FAVORITE_STATUS = "IS_FAVORITE";

    private Boolean isFavorite = false;
    private ImageView mPoster;
    private TextView mOverview;
    private TextView mRate;
    private TextView mReleaseDate;
    private ImageView mFavorite;
    private Movie movie;
    private String movieId;
    private RecyclerView mTrailerView;
    private RecyclerView mReviewView;
    private ProgressBar trailerLoadIndicate;
    private ProgressBar reviewLoadIndicate;
    private TextView noTrailerMessage;
    private TextView noReviewMessage;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;




    private FavoriteMovieDatabase mDb;

    //A number to identify the Trailer_loader
    private static final int TRAILER_LOADER_ID = 11;
    //A number to identify the review_loader
    private static final int REVIEW_LOADER_ID = 12;

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.poster);
        mOverview = findViewById(R.id.plot_synopsis);
        mRate = findViewById(R.id.rate);
        mReleaseDate = findViewById(R.id.release_date);
        mFavorite = findViewById(R.id.favorite_button);
        mTrailerView = findViewById(R.id.recyclerview_trailers);
        mReviewView = findViewById(R.id.recyclerview_reviews);

        trailerLoadIndicate = findViewById(R.id.trailer_load_progress);
        reviewLoadIndicate = findViewById(R.id.review_load_progress);

        noTrailerMessage = findViewById(R.id.no_trailer);
        noReviewMessage = findViewById(R.id.no_review);

        //add divisor to the items in each recyclerview
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mTrailerView.addItemDecoration(itemDecor);
        mReviewView.addItemDecoration(itemDecor);

        if(savedInstanceState != null)
            isFavorite = savedInstanceState.getBoolean(FAVORITE_STATUS);

        mDb = FavoriteMovieDatabase.getInstance(getApplicationContext());
        //set the click listener to the favorite icon
        mFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                if(isFavorite) {
                    isFavorite = false;
                    Toast.makeText(view.getContext(), R.string.unfavorite_movie, Toast.LENGTH_LONG).show();
                }
                else {
                    isFavorite = true;
                    Toast.makeText(view.getContext(), R.string.favorite_movie, Toast.LENGTH_LONG).show();
                }


                setFavoriteButtonImage();

                MovieExecutor.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (isFavorite)
                                mDb.movieDao().insertMovie(movie);
                            else
                                mDb.movieDao().deleteFavoriteMovie(movie);
                        }
                    });
            }
        });

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailerView.setLayoutManager(layoutManager1);
        mReviewView.setLayoutManager(layoutManager2);

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerView.setAdapter(mTrailerAdapter);
        mReviewAdapter = new ReviewAdapter();
        mReviewView.setAdapter(mReviewAdapter);
        //get the intent and Movie object passing from main activity, then load the detail value of Movie to
        //the corresponding view.
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra(MOVIE_DATA)){
                movie = (Movie) intent.getSerializableExtra(MOVIE_DATA);
            }
        }

        movieId = movie.getId();
        checkMovieInFavoriteList();
        loadMovieInfo();
        loadTrailersAndReviews();
    }

    /**
     * Method to check if this movie has been already added in the favorite movie database or not,
     * and update the corresponding boolean isFavorite.
     */
    private void checkMovieInFavoriteList(){
        MovieExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Movie movieInDatabase = mDb.movieDao().loadFavoriteMovieById(movieId);
                if(movieInDatabase == null)
                    isFavorite = false;
                else
                    isFavorite = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFavoriteButtonImage();
                    }
                });
            }
        });

    }

    /**
     * load the detail information of the clicked movie item
     */
    private void loadMovieInfo()
    {
        //set the title of the detail activity
        setTitle(movie.getTitle());
        //load the image with the current poster path passing by the Intent
        Picasso.with(this).load(POSTER_BASE_URL + SIZE + movie.getPoster())
                .into(mPoster);
        //set the overview textview with the overview of the movie
        mOverview.setText(movie.getOverview());
        //set and format the rate of the movie
        mRate.setText(movie.getRate() + "/10");
        //set the releasedate textview with the release date of the movie
        mReleaseDate.setText(movie.getReleaseDate());
        //set the favorite image button
    }

    /**
     * helper method to setup the favorite button according the exist status of this movie in the
     * database.
     */
    public void setFavoriteButtonImage()
    {
        if(isFavorite)
            mFavorite.setImageResource(R.drawable.favorite);
        else
            mFavorite.setImageResource(R.drawable.unfavorite);
    }

    /**
     * load the trailers and reviews about this movie
     */
    public void loadTrailersAndReviews() {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Trailer>> trailerLoader = loaderManager.getLoader(TRAILER_LOADER_ID);
        if(trailerLoader == null){
            loaderManager.initLoader(TRAILER_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(TRAILER_LOADER_ID, null, this);
        }

        Loader<List<Review>> ReviewLoader = loaderManager.getLoader(REVIEW_LOADER_ID);
        if(ReviewLoader == null){
            loaderManager.initLoader(REVIEW_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(REVIEW_LOADER_ID, null, this);
        }
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        if (id == TRAILER_LOADER_ID)
            return new AsyncTaskLoader<List<Trailer>>(this) {
                List<Trailer> trailers;

                @Override
                protected void onStartLoading() {
                    if (trailers != null)
                        deliverResult(trailers);
                    else {
                        trailerLoadIndicate.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public List<Trailer> loadInBackground() {
                    URL trailerRequestUrl = NetworkUtils.buildTrailerRequestURL(movieId);
                    try{
                        String trailerJsonResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                        List<Trailer> trailerData = JsonUtils.parseTrailerJson(trailerJsonResponse);
                        return trailerData;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(List<Trailer> data) {
                    trailers = data;
                    super.deliverResult(data);
                }
            };
        else if (id == REVIEW_LOADER_ID)
            return new AsyncTaskLoader<List<Review>>(this){

                List<Review> reviews;

                @Override
                protected void onStartLoading() {
                    if (reviews != null)
                        deliverResult(reviews);
                    else {
                        reviewLoadIndicate.setVisibility(View.INVISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public List<Review> loadInBackground() {
                    URL reviewRequestUrl = NetworkUtils.buildReviewRequestURL(movieId);
                    try{
                        String reviewJsonResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                        List<Review> reviewData = JsonUtils.parseReviewJson(reviewJsonResponse);
                        return reviewData;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(List<Review> data) {
                    reviews = data;
                    super.deliverResult(data);
                }
            };
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if (loader.getId() == TRAILER_LOADER_ID)
        {
            trailerLoadIndicate.setVisibility(View.INVISIBLE);
            if (data != null){
                noTrailerMessage.setVisibility(View.INVISIBLE);
                mTrailerAdapter.setTrailerData((List<Trailer>) data);
            } else {
                mTrailerView.setVisibility(View.INVISIBLE);
                noTrailerMessage.setVisibility(View.VISIBLE);
            }
        }
        else if (loader.getId() == REVIEW_LOADER_ID)
        {
            reviewLoadIndicate.setVisibility(View.INVISIBLE);
            if (data != null)
            {
                noReviewMessage.setVisibility(View.INVISIBLE);
                mReviewAdapter.setReviewDate((List<Review>) data);
            } else {
                mReviewView.setVisibility(View.INVISIBLE);
                noReviewMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onClick(Trailer trailer) {
        String key = trailer.getKey();
        Uri trailerUri = NetworkUtils.buildTrailerWebsite(key);
        Intent openTrailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
        if(openTrailerIntent.resolveActivity(getPackageManager()) != null)
            startActivity(openTrailerIntent);
    }

    /**
     * save the instance state when the activity is temporarily destroyed.
     * @param outState activity state, put the boolean isFavorite in the outState.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FAVORITE_STATUS, isFavorite);
    }
}
