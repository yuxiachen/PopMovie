package com.example.android.popmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by yuxia on 5/13/18.
 */

@Entity (tableName = "favoriteMovie")
public class Movie implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;

    private String title;
    private String poster;
    private String overview;
    private String releaseDate;
    private String rate;


    //public Movie(){}

    public Movie(String title, String poster, String overview, String releaseDate, String rate, String id){
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rate = rate;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public String getPoster(){
        return poster;
    }

    public String getOverview(){
        return overview;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getRate(){
        return rate;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setPoster(String poster){
        this.poster = poster;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public void setRate(String rate){
        this.rate = rate;
    }

    public void setId(String id) {
        this.id = id;
    }

}
