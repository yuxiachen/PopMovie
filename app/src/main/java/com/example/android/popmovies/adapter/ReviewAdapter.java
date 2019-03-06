package com.example.android.popmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popmovies.R;
import com.example.android.popmovies.model.Review;

import java.util.List;

/**
 * Created by yuxia on 8/27/18.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Review> mReviewData;


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_list_item, viewGroup, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviewData.get(position);
        String author = review.getAuthor();
        String content = review.getReview();

        holder.mAuthor.setText(author);
        holder.mContent.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mReviewData == null)
            return 0;
        return mReviewData.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthor;
        public TextView mContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.review_author);
            mContent = itemView.findViewById(R.id.review_content);
        }
    }

    public void setReviewDate(List<Review> reviews) {
        mReviewData = reviews;
        notifyDataSetChanged();
    }

}
