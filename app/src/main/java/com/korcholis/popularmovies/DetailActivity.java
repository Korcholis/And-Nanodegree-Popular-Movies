package com.korcholis.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.korcholis.popularmovies.data.TMDbApi;
import com.korcholis.popularmovies.exceptions.ConnectionNotAvailableException;
import com.korcholis.popularmovies.fragments.OverviewFragment;
import com.korcholis.popularmovies.fragments.ReviewsFragment;
import com.korcholis.popularmovies.fragments.VideosFragment;
import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.utils.Constants;
import com.korcholis.popularmovies.utils.MoviesActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.korcholis.popularmovies.DetailActivity.ScreensAdapter.POS_OVERVIEW;
import static com.korcholis.popularmovies.DetailActivity.ScreensAdapter.POS_REVIEWS;
import static com.korcholis.popularmovies.DetailActivity.ScreensAdapter.POS_VIDEOS;

public class DetailActivity extends MoviesActivity implements OverviewFragment.OnOverviewFragmentListener, ReviewsFragment.OnReviewsFragmentListener, VideosFragment.OnVideosFragmentListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loadingPb)
    FrameLayout loadingPb;
    @BindView(R.id.bottomNavBar)
    BottomNavigationView bottomNavBar;
    @BindView(R.id.pager)
    ViewPager pager;

    private Movie movie;
    private ScreensAdapter screensAdapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.launcher_icon);

        getSupportActionBar().setTitle(R.string.loading_title);
        getSupportActionBar().setSubtitle(R.string.page_details);

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
                                screensAdapter = new ScreensAdapter(getSupportFragmentManager(), movie);
                                pager.setAdapter(screensAdapter);
                                loadingPb.setVisibility(View.GONE);
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

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case POS_OVERVIEW:
                        getSupportActionBar().setSubtitle(R.string.page_details);
                        break;
                    case POS_VIDEOS:
                        getSupportActionBar().setSubtitle(R.string.page_videos);
                        break;
                    case POS_REVIEWS:
                        getSupportActionBar().setSubtitle(R.string.page_reviews);
                        break;
                }


                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavBar.getMenu().getItem(0).setChecked(false);
                }

                bottomNavBar.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavBar.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_details:
                        pager.setCurrentItem(POS_OVERVIEW);
                        return true;

                    case R.id.option_trailers:
                        pager.setCurrentItem(POS_VIDEOS);
                        return true;

                    case R.id.option_reviews:
                        pager.setCurrentItem(POS_REVIEWS);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onOverviewInteraction(Uri uri) {

    }

    @Override
    public void onReviewsInteraction(Uri uri) {

    }

    @Override
    public void onVideosInteraction(Uri uri) {

    }

    public static class ScreensAdapter extends FragmentPagerAdapter {
        private static final int NUM_ITEMS = 3;
        public static final int POS_OVERVIEW = 0;
        public static final int POS_VIDEOS = 1;
        public static final int POS_REVIEWS = 2;
        private Movie movie;

        public ScreensAdapter(FragmentManager fm, Movie theMovie) {
            super(fm);
            movie = theMovie;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POS_OVERVIEW:
                    return OverviewFragment.newInstance(movie);
                case POS_VIDEOS:
                    return VideosFragment.newInstance(movie.getId());
                case POS_REVIEWS:
                    return ReviewsFragment.newInstance(movie.getId());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
