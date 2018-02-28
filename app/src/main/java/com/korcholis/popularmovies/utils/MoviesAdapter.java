package com.korcholis.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.korcholis.popularmovies.R;
import com.korcholis.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private final Context context;
    private List<Movie> moviesList;
    private OnItemClickListener listener = null;

    public MoviesAdapter(List<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void swapContent(List<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_content_item, parent, false);
        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesHolder holder, int position) {
        Movie movie = moviesList.get(holder.getAdapterPosition());

        holder.title.setText(movie.getTitle());
        Picasso.with(context)
                .load(movie.getPoster())
                .placeholder(R.drawable.movie_loading)
                .error(R.drawable.movie_error)
                .into(holder.moviePoster,
                        PicassoPalette.with(movie.getPoster(), holder.moviePoster)
                                .use(PicassoPalette.Profile.MUTED_DARK)
                                .intoBackground(holder.title)
                                .intoTextColor(holder.title, PicassoPalette.Swatch.TITLE_TEXT_COLOR));

        int proportionalHeight = DynamicSizeUtils.posterHeightInLIst(context);
        ViewGroup.LayoutParams params = holder.viewroot.getLayoutParams();
        params.height = proportionalHeight;
        holder.viewroot.setLayoutParams(params);

        holder.clickableArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(moviesList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface OnItemClickListener {
        void onClick(int id);
    }

    class MoviesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.moviePosterIv)
        ImageView moviePoster;
        @BindView(R.id.titleTv)
        TextView title;
        @BindView(R.id.viewroot)
        View viewroot;
        @BindView(R.id.clickableArea)
        View clickableArea;

        public MoviesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
