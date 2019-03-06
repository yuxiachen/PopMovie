package com.example.android.popmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.popmovies.model.Movie;

import java.util.List;

/**
 * Created by yuxia on 8/31/18.
 */

@Dao
public interface MovieDao {
    @Query ("SELECT * FROM favoriteMovie ORDER BY id")
    LiveData<List<Movie>> loadFavoriteMovie();

    @Query("SELECT * FROM favoriteMovie WHERE id = :id")
    Movie loadFavoriteMovieById(String id);

    @Insert
    void insertMovie(Movie movie);

    @Update
    void updateFavoriteMovie(Movie movie);

    @Delete
    void deleteFavoriteMovie(Movie movie);
}

