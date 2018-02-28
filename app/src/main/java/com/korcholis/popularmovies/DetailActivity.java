package com.korcholis.popularmovies;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.korcholis.popularmovies.exceptions.ConnectionNotAvailableException;
import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.utils.Constants;
import com.korcholis.popularmovies.utils.DynamicSizeUtils;
import com.korcholis.popularmovies.utils.MoviesActivity;
import com.korcholis.popularmovies.data.TMDbApi;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends MoviesActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.posterIv)
    ImageView posterIv;
    @BindView(R.id.releaseTv)
    TextView releaseTv;
    @BindView(R.id.ratingTv)
    TextView ratingTv;
    @BindView(R.id.synopsisTv)
    TextView synopsisTv;
    @BindView(R.id.loadingPb)
    FrameLayout loadingPb;

    private Movie movie;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.launcher_icon);

        getSupportActionBar().setTitle(R.string.loading_title);

        int movieId = getIntent().getIntExtra(Constants.PARAM_MOVIE_ID, Constants.DEFAULT_MOVIE_ID);

        if (movieId == Constants.DEFAULT_MOVIE_ID) {
            showMovieErrorToast(true);
            return;
        }
        loadingPb.setVisibility(View.VISIBLE);

        compositeDisposable.add(
            TMDbApi.instance(this).movie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie fetchedMovie) {
                        movie = fetchedMovie;
                        loadingPb.setVisibility(View.GONE);
                        populateUI();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (throwable instanceof ConnectionNotAvailableException) {
                            showNoConnectionErrorToast(true);
                        } else {
                            showMovieErrorToast(true);
                        }
                    }
                }));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateUI() {
        getSupportActionBar().setTitle(movie.getTitle());
        Picasso.with(this)
                .load(movie.getPoster())
                .placeholder(R.drawable.movie_loading)
                .error(R.drawable.movie_error)
                .into(posterIv);

        int imageWidth = posterIv.getWidth();
        int proportionalHeight = DynamicSizeUtils.posterHeight(imageWidth);

        ViewGroup.LayoutParams params = posterIv.getLayoutParams();
        params.height = proportionalHeight;
        posterIv.setLayoutParams(params);
        synopsisTv.setText(movie.getSynopsis());
        ratingTv.setText(getString(R.string.movie_rating_out_of, movie.getVoteAverage().toString()));



        SimpleDateFormat parser = new SimpleDateFormat(getString(R.string.tmdb_date_format));
        Date date = null;
        try {
            date = parser.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.human_date_format), Locale.getDefault());
        String formattedDate = formatter.format(date);

        releaseTv.setText(formattedDate);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
