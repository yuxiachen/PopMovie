package com.example.android.popmovies.model;

import java.io.Serializable;

/**
 * Created by yuxia on 8/27/18.
 */

public class Review implements Serializable{

    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;

    }

    public String getAuthor() {
        return author;
    }
    public String getReview() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setReview(String content) {
        this.content = content;
    }

}
