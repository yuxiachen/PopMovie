package com.example.android.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popmovies.R;
import com.example.android.popmovies.model.Movie;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * The MovieAdapter exposes a list of Movie to a recyclerview
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    //The base URL and size used to load the poster of the movie
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";

    private List<Movie> mMovieData;
    //An on-click handler used to handle the click on the viewholder in a recyclerview
    private final MovieAdapterOnClickHandler mClickHandler;

    //the interface that receives onClick messages
    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    /**
     * MovieAdapter constructor
     * @param clickHandler the on-click handler for the adapter
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children view for a movie list item
     */
    public final class MovieAdapterViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        public final ImageView mImageView;


        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }


        /**
         * This method get called  by the child view during a click
         * @param view the view that is clicked
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(currentMovie);
        }
    }

    /**
     * Create the viewholder to fill the screen and allow for scrolling
     * @param viewGroup the ViewGroup that these ViewHolder are contained within
     * @param viewType If your recyclerview has more than one type of item, you can use this viewType
     *                 to provide a different layout
     * @return A new MoiveAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdListItem, viewGroup, false);

        return new MovieAdapterViewHolder(view);

    }

    /**
     * This method updates the contents of the viewholder to display the movie poster for this
     * particular position
     * @param holder the viewholder which should be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie currentMovie = mMovieData.get(position);

        String posterPath = POSTER_BASE_URL + SIZE + currentMovie.getPoster();
        Picasso.with(holder.itemView.getContext()).load(posterPath).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        if (mMovieData == null)
            return 0;
        return mMovieData.size();
    }

    /**
     * This method set the movie date on a MovierAdapter
     * @param movies The new moive data to be displayed
     */
    public void setMovieData(List<Movie> movies){
        mMovieData = movies;
        notifyDataSetChanged();
    }

}

