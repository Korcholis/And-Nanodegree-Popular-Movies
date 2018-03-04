package com.korcholis.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.korcholis.popularmovies.exceptions.ConnectionNotAvailableException;
import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.models.MoviesList;
import com.korcholis.popularmovies.utils.ConnectionChecker;
import com.korcholis.popularmovies.utils.Constants;
import com.korcholis.popularmovies.utils.MoviesActivity;
import com.korcholis.popularmovies.adapters.MoviesAdapter;
import com.korcholis.popularmovies.data.TMDbApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("WeakerAccess")
public class ListActivity extends MoviesActivity {
    @BindView(R.id.movieListRv)
    RecyclerView movieListRv;
    @BindView(R.id.loadingPb)
    ProgressBar loadingPb;
    @BindView(R.id.errorView)
    LinearLayout errorView;
    @BindView(R.id.errorTv)
    TextView errorTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MoviesAdapter adapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TMDbApi.SortCriteria chosenSortCriteria = TMDbApi.SortCriteria.MostPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.launcher_icon);

        List<Movie> movies = new ArrayList<>();
        getSupportActionBar().setTitle(R.string.loading_title);

        adapter = new MoviesAdapter(movies, this);
        adapter.setOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onClick(int movieId) {
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra(Constants.PARAM_MOVIE_ID, movieId);
                startActivity(intent);
            }
        });
        movieListRv.setAdapter(adapter);

        if (savedInstanceState != null) {
            chosenSortCriteria = TMDbApi.SortCriteria.valueOf(savedInstanceState.getString("chosenSortCriteria"));
        }

        loadMovies();
    }

    protected void loadMovies() {
        switch (chosenSortCriteria) {
            case HighRated:
                getSupportActionBar().setTitle(R.string.sub_highest_rated);
                break;
            case MostPopular:
                getSupportActionBar().setTitle(R.string.sub_popular);
                break;
        }

        showProgress();
        compositeDisposable.clear();
        compositeDisposable.add(
                TMDbApi.instance(this).movieList(chosenSortCriteria)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function< MoviesList, List<Movie>>() {

                            @Override
                            public List<Movie> apply(MoviesList moviesList) {
                                return moviesList.getResults();
                            }
                        })
                        .subscribe(new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> movies) {
                                if (movies == null) {
                                    if (!ConnectionChecker.isNetworkAvailable(ListActivity.this)) {
                                        showNoConnectionErrorToast(false);
                                    } else {
                                        showMovieListErrorToast(false);
                                    }
                                } else {
                                    adapter.swapContent(movies);
                                    if (movies.isEmpty()) {
                                        showErrorView(R.string.error_movies_wrong_data);
                                    } else {
                                        showList();
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                if (throwable instanceof ConnectionNotAvailableException) {
                                    showErrorView(R.string.error_no_connection);
                                } else {
                                    showErrorView(R.string.error_movies_wrong_data);
                                }
                            }
                        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.refresh:
                loadMovies();
                return true;
            case R.id.sortHighRated:
                chosenSortCriteria = TMDbApi.SortCriteria.HighRated;
                loadMovies();
                return true;
            case R.id.sortPopularity:
                chosenSortCriteria = TMDbApi.SortCriteria.MostPopular;
                loadMovies();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("chosenSortCriteria", chosenSortCriteria.name());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void showList() {
        movieListRv.setVisibility(View.VISIBLE);
        loadingPb.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    private void showProgress() {
        movieListRv.setVisibility(View.GONE);
        loadingPb.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void showErrorView(int errorMessageId) {
        movieListRv.setVisibility(View.GONE);
        loadingPb.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        errorTv.setText(errorMessageId);
    }
}
