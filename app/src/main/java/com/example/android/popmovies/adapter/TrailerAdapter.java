package com.example.android.popmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovies.R;
import com.example.android.popmovies.model.Trailer;

import java.util.List;

/**
 * Created by yuxia on 8/27/18.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<Trailer> mTrailerData;
    private final TrailerAdapterOnClickHandler mTrailerOnClickHandler;

    public TrailerAdapter(TrailerAdapterOnClickHandler mListener) {
        mTrailerOnClickHandler = mListener;
    }


    //the interface that receives onClick messages
    public interface TrailerAdapterOnClickHandler{
        void onClick(Trailer trailer);
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_list_item, viewGroup, false);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailerData.get(position);
        String trailerName = trailer.getName();

        holder.mTrailerName.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null)
            return 0;
        return mTrailerData.size();
    }



    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mPlayIcon;
        public TextView mTrailerName;
        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mPlayIcon = itemView.findViewById(R.id.play_icon);
            mTrailerName = itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Trailer currTrailer = mTrailerData.get(getAdapterPosition());
            mTrailerOnClickHandler.onClick(currTrailer);
        }
    }

    public void setTrailerData(List<Trailer> trailers) {
        mTrailerData = trailers;
        notifyDataSetChanged();
    }

}
