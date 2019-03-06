package com.example.android.popmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.example.android.popmovies.model.Movie;

/**
 * Created by yuxia on 9/1/18.
 */
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movie_list";
    private static FavoriteMovieDatabase sInstance;

    public static FavoriteMovieDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized(LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, FavoriteMovieDatabase.DATABASE_NAME)
                        .build();
            }
        }
        String path = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        Log.v("database path", path);
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
