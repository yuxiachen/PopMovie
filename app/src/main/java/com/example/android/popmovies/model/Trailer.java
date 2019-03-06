package com.example.android.popmovies.model;

import java.io.Serializable;

/**
 * Created by yuxia on 8/27/18.
 */

public class Trailer implements Serializable{

    private String key;
    private String name;


    public Trailer(String key, String name)
    {

        this.key = key;
        this.name = name;
    }


    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

}
