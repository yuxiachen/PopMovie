package com.example.android.popmovies.utils;

import com.example.android.popmovies.model.Movie;
import com.example.android.popmovies.model.Review;
import com.example.android.popmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * JsonUtils will be used to parse the movie JSON data
 */

public class JsonUtils {

    //Movie information label
    private static final String RESULT = "results";
    private static final String TITLE = "title";
    private static final String POSTER = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String RATE = "vote_average";
    private static final String ID = "id";

    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";

    private static final String TRAILER_NAME = "name";
    private static final String TRAILER_KEY = "key";


    /**
     * This method parses JSON from a web response and returns an array of Movie objects
     * describing the movie
     *
     * @param jsonResponse JSON response from server
     * @return arraylist of Movie object describing the movie
     */
    public static ArrayList<Movie> parseMovieJson(String jsonResponse){

        //new ArrayList<Movie> to hold the movie details
        ArrayList<Movie> parseMovieData = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = jsonObject.getJSONArray(RESULT);


            for(int i = 0; i < resultArray.length(); i++){
                //get the json object representing the movie
                JSONObject currentMovie = resultArray.getJSONObject(i);
                //build the movie object by collecting its responding data
                String title = currentMovie.getString(TITLE);
                String poster = currentMovie.getString(POSTER);
                String overview = currentMovie.getString(OVERVIEW);
                String releaseDate = currentMovie.getString(RELEASE_DATE);
                String rate = currentMovie.getString(RATE);
                String id = currentMovie.getString(ID);

                parseMovieData.add(new Movie(title, poster, overview, releaseDate, rate, id));
            }
        } catch(JSONException e){
            //If an error is thrown when executing any of the above statement in the "try" block
            //catch the exception here so the app won't crash
            e.printStackTrace();

        }
        return parseMovieData;
    }


    public static ArrayList<Review> parseReviewJson(String jsonResponse) {
        ArrayList<Review> parseReviewData = new ArrayList<Review>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = jsonObject.getJSONArray(RESULT);
            for(int i = 0; i < resultArray.length(); i++){
                //get the json object representing the review
                JSONObject currentReview = resultArray.getJSONObject(i);
                //build the review object by collecting its responding data
                String author = currentReview.getString(REVIEW_AUTHOR);
                String content = currentReview.getString(REVIEW_CONTENT);

                parseReviewData.add(new Review(author, content));
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

        if (parseReviewData.isEmpty()) return null;

        return parseReviewData;
    }

    public static ArrayList<Trailer> parseTrailerJson(String jsonResponse) {
        ArrayList<Trailer> parseTrailerData = new ArrayList<Trailer>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultArray = jsonObject.getJSONArray(RESULT);
            for(int i = 0; i < resultArray.length(); i++){
                //get the json object representing the Trailer
                JSONObject currentTrailer = resultArray.getJSONObject(i);
                //build the Trailer object by collecting its responding data
                String name = currentTrailer.getString(TRAILER_NAME);
                String key = currentTrailer.getString(TRAILER_KEY);

                parseTrailerData.add(new Trailer(key, name));
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

        if (parseTrailerData.isEmpty()) return null;

        return parseTrailerData;
    }
}
