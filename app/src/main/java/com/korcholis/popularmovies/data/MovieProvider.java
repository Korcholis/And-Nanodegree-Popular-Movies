package com.korcholis.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {
    private PopDBHelper dbHelper;

    public static final int MOVIES = 100;
    public static final int MOVIES_MOST_POPULAR = 200;
    public static final int MOVIES_HIGH_RATED = 300;
    public static final int MOVIES_FAVORITE = 400;

    public static final int MOVIE_WITH_ID = 101;
    public static final int MOVIE_MOST_POPULAR_WITH_ID = 201;
    public static final int MOVIE_HIGH_RATED_WITH_ID = 301;
    public static final int MOVIE_FAVORITE_WITH_ID = 401;

    public static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/" + PopDBContract.PATH_MOST_POPULAR, MOVIES_MOST_POPULAR);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/" + PopDBContract.PATH_HIGH_RATED, MOVIES_HIGH_RATED);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/" + PopDBContract.PATH_FAVORITE, MOVIES_FAVORITE);

        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/#/" + PopDBContract.PATH_MOST_POPULAR, MOVIE_MOST_POPULAR_WITH_ID);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/#/" + PopDBContract.PATH_HIGH_RATED, MOVIE_HIGH_RATED_WITH_ID);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_MOVIES + "/#/" + PopDBContract.PATH_FAVORITE, MOVIE_FAVORITE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new PopDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (match) {
            case MOVIES:
                return db.query(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            case MOVIES_FAVORITE:
                return db.query(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        PopDBContract.MovieEntry.COLUMN_IS_FAVORITE + "=1",
                        null,
                        null,
                        null,
                        sortOrder);
            case MOVIES_HIGH_RATED:
                return db.query(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED + "=1",
                        null,
                        null,
                        null,
                        sortOrder);
            case MOVIES_MOST_POPULAR:
                return db.query(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        PopDBContract.MovieEntry.COLUMN_IS_POPULAR + "=1",
                        null,
                        null,
                        null,
                        sortOrder);
            case MOVIE_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                return db.query(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=" + movieId,
                        null,
                        null,
                        null,
                        sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (match) {
            case MOVIES:
                try {
                    long newId = db.insertOrThrow(PopDBContract.MovieEntry.TABLE_NAME, null, contentValues);
                    if (newId > 0) {
                        return ContentUris.withAppendedId(PopDBContract.MovieEntry.CONTENT_URI, newId);
                    } else {
                        return ContentUris.withAppendedId(PopDBContract.MovieEntry.CONTENT_URI, 0);
                    }
                } catch(SQLiteConstraintException exception) {
                    return ContentUris.withAppendedId(PopDBContract.MovieEntry.CONTENT_URI, 0);
                }

            case MOVIE_FAVORITE_WITH_ID:
                String favMovieId = uri.getPathSegments().get(1);
                ContentValues favCv = new ContentValues();
                favCv.put(PopDBContract.MovieEntry.COLUMN_IS_FAVORITE, 1);
                String favWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] favWhereArgs = { favMovieId };
                int favAmount = db.update(PopDBContract.MovieEntry.TABLE_NAME, favCv, favWhere, favWhereArgs);
                if (favAmount > 0) {
                    return PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(favMovieId).build();
                } else {
                    throw new android.database.SQLException("Movie " + favMovieId + " doesn't exist");
                }
            case MOVIE_MOST_POPULAR_WITH_ID:
                String popMovieId = uri.getPathSegments().get(1);
                ContentValues popCv = new ContentValues();
                popCv.put(PopDBContract.MovieEntry.COLUMN_IS_POPULAR, 1);
                String popWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] popWhereArgs = { popMovieId };
                int popAmount = db.update(PopDBContract.MovieEntry.TABLE_NAME, popCv, popWhere, popWhereArgs);
                if (popAmount > 0) {
                    return PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(popMovieId).build();
                } else {
                    throw new android.database.SQLException("Movie " + popMovieId + " doesn't exist");
                }
            case MOVIE_HIGH_RATED_WITH_ID:
                String ratedMovieId = uri.getPathSegments().get(1);
                ContentValues ratedCv = new ContentValues();
                ratedCv.put(PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED, 1);
                String ratedWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] ratedWhereArgs = { ratedMovieId };
                int ratedAmount = db.update(PopDBContract.MovieEntry.TABLE_NAME, ratedCv, ratedWhere, ratedWhereArgs);
                if (ratedAmount > 0) {
                    return PopDBContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(ratedMovieId).build();
                } else {
                    throw new android.database.SQLException("Movie " + ratedMovieId + " doesn't exist");
                }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (match) {
            case MOVIE_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                return db.delete(
                        PopDBContract.MovieEntry.TABLE_NAME,
                        PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{ movieId });
            case MOVIES_FAVORITE:
                ContentValues favsCv = new ContentValues();
                favsCv.put(PopDBContract.MovieEntry.COLUMN_IS_FAVORITE, 0);
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, favsCv, null, null);
            case MOVIE_FAVORITE_WITH_ID:
                String favMovieId = uri.getPathSegments().get(1);
                ContentValues favCv = new ContentValues();
                favCv.put(PopDBContract.MovieEntry.COLUMN_IS_FAVORITE, 0);
                String favWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] favWhereArgs = { favMovieId };
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, favCv, favWhere, favWhereArgs);
            case MOVIES_MOST_POPULAR:
                ContentValues popsCv = new ContentValues();
                popsCv.put(PopDBContract.MovieEntry.COLUMN_IS_POPULAR, 0);
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, popsCv, null, null);
            case MOVIE_MOST_POPULAR_WITH_ID:
                String popMovieId = uri.getPathSegments().get(1);
                ContentValues popCv = new ContentValues();
                popCv.put(PopDBContract.MovieEntry.COLUMN_IS_POPULAR, 0);
                String popWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] popWhereArgs = { popMovieId };
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, popCv, popWhere, popWhereArgs);
            case MOVIES_HIGH_RATED:
                ContentValues ratedsCv = new ContentValues();
                ratedsCv.put(PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED, 0);
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, ratedsCv, null, null);
            case MOVIE_HIGH_RATED_WITH_ID:
                String ratedMovieId = uri.getPathSegments().get(1);
                ContentValues ratedCv = new ContentValues();
                ratedCv.put(PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED, 0);
                String ratedWhere = PopDBContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] ratedWhereArgs = { ratedMovieId };
                return db.update(PopDBContract.MovieEntry.TABLE_NAME, ratedCv, ratedWhere, ratedWhereArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
