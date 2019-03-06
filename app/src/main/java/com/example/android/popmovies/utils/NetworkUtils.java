package com.example.android.popmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * NetworkUtils will be used to communicate with the movie servers.
 */

public final class NetworkUtils {

    private static final String API_KEY = "4428b775da9ec29f90ed76721456bfb8";
    private static final String QUERY_BASE_URL = "http://api.themoviedb.org/3/movie";
    private final static String API_KEY_PARAM = "api_key";
    private static final String VIDEO = "videos";
    private static final String REVIEW = "reviews";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAM = "v";

    private NetworkUtils() { }
    /**
     * Build the URL used to fetch the movie using a sort method. the sort method
     * can be set by the user.
     *
     * @param sortOrder The sort order that will be used for the query
     * @return The URL to use to query the server
     */
    public static URL buildURL(String sortOrder){
        Uri buildUri = Uri.parse(QUERY_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Using the URL to fetch JSON data from the server.
     *
     * @param url The URL used to fetch data
     * @return The content of the HTTP response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else{
                return null;
            }
        } finally{
            urlConnection.disconnect();
        }
    }


    public static URL buildReviewRequestURL(String id){
        Uri buildUri = Uri.parse(QUERY_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(REVIEW)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerRequestURL(String id){
        Uri buildUri = Uri.parse(QUERY_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(VIDEO)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch(MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }


    public static Uri buildTrailerWebsite(String key) {
        Uri trailerUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(VIDEO_PARAM, key)
                .build();

        return trailerUri;
    }
}
