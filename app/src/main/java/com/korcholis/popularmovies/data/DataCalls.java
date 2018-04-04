package com.korcholis.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;

import com.korcholis.popularmovies.models.Movie;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

public class DataCalls {

    public static Observable<List<Movie>> listFavorites(@NonNull final ContentResolver resolver) {
        final Observable<List<Movie>> observable = Observable.fromCallable(new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() {
                Cursor cursor = resolver.query(PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(PopDBContract.PATH_FAVORITE).build(), null, null, null, null);
                return PopDBHelper.cursorToMovies(cursor);
            }
        });

        return observable;
    }

    public static Observable<Boolean> setMovieAsFavorite(@NonNull final Movie movie, @NonNull final ContentResolver resolver) {
        final Observable<Boolean> observable = Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                resolver.insert(PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movie.getId().toString()).appendPath(PopDBContract.PATH_FAVORITE).build(), null);
                return true;
            }
        });

        return observable;
    }

    public static Observable<Boolean> unsetMovieAsFavorite(@NonNull final Movie movie, @NonNull final ContentResolver resolver) {
        return unsetMovieAsFavorite(movie.getId() + "", resolver);
    }

    public static Observable<Boolean> unsetMovieAsFavorite(@NonNull final String movieId, @NonNull final ContentResolver resolver) {
        final Observable<Boolean> observable = Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                resolver.delete(PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId).appendPath(PopDBContract.PATH_FAVORITE).build(), null, null);
                return true;
            }
        });

        return observable;
    }

    public static Observable<Movie> getMovie(@NonNull final int movieId, @NonNull final ContentResolver resolver) {
        return getMovie(movieId + "", resolver);
    }

    public static Observable<Movie> getMovie(@NonNull final String movieId, @NonNull final ContentResolver resolver) {
        final Observable<Movie> observable = Observable.fromCallable(new Callable<Movie>() {
            @Override
            public Movie call() {
                Cursor cursor = resolver.query(PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId).build(), null, null, null, null);
                int count = cursor.getCount();
                if (count == 0) {
                    throw new SQLException("The movie with id " + movieId + " is not saved");
                }
                return PopDBHelper.cursorToMovies(cursor).get(0);
            }
        });

        return observable;
    }

    public static Observable<Boolean> insertMovie(@NonNull final Movie movie, @NonNull final ContentResolver resolver) {
        final Observable<Boolean> observable = Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                ContentValues cv = new ContentValues();
                cv.put(PopDBContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                cv.put(PopDBContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                cv.put(PopDBContract.MovieEntry.COLUMN_POSTER, movie.getPoster(true));
                cv.put(PopDBContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                cv.put(PopDBContract.MovieEntry.COLUMN_AVG_VOTE, movie.getVoteAverage());
                cv.put(PopDBContract.MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
                try {
                    resolver.insert(PopDBContract.MovieEntry.CONTENT_URI, cv);
                } catch (SQLiteConstraintException constraintException) {

                }
                return true;
            }
        });

        return observable;
    }

    public static Observable<Boolean> unsetCriteriaToAllMovies(@NonNull final TMDbApi.SortCriteria chosenSortCriteria, @NonNull final ContentResolver resolver) {
        final Observable<Boolean> observable = Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() {
                resolver.delete(PopDBContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(chosenSortCriteria.toString()).build(), null, null);
                return true;
            }
        });

        return observable;
    }

    public static Observable<Boolean> setCriteriaToMovie(@NonNull final Movie movie, @NonNull final TMDbApi.SortCriteria chosenSortCriteria, @NonNull final ContentResolver resolver) {
        Observable<Boolean> observable = Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                resolver.insert(
                        PopDBContract.MovieEntry.CONTENT_URI.buildUpon()
                                .appendPath(movie.getId().toString())
                                .appendPath(chosenSortCriteria.toString()).build(), null);
                return true;
            }
        });

        return observable;
    }
}
