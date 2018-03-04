package com.korcholis.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korcholis.popularmovies.R;
import com.korcholis.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private Context context;

    public ReviewsAdapter(Context context) {
        this.context = context;
        this.reviews = new ArrayList<>();
    }

    public void swapContent(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.author)
        TextView author;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
