package com.example.android.popmovies.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popmovies.data.FavoriteMovieDatabase;
import com.example.android.popmovies.model.Movie;

import java.util.List;

/**
 * Created by yuxia on 9/2/18.
 */

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;
    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteMovieDatabase db = FavoriteMovieDatabase.getInstance(this.getApplication());
        movies = db.movieDao().loadFavoriteMovie();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movies;
    }
}
