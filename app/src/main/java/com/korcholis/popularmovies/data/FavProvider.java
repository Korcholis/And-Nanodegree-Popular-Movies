package com.korcholis.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavProvider extends ContentProvider {
    private PopDBHelper dbHelper;

    public static final int FAVS = 100;
    public static final int FAV_WITH_ID = 101;
    public static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_FAVS, FAVS);
        uriMatcher.addURI(PopDBContract.AUTHORITY, PopDBContract.PATH_FAVS + "/#", FAV_WITH_ID);

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
            case FAVS:
                return db.query(
                        PopDBContract.FavEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        PopDBContract.FavEntry.COLUMN_FAV_TIME + " ASC");
            case FAV_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                return db.query(
                        PopDBContract.FavEntry.TABLE_NAME,
                        projection,
                        PopDBContract.FavEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{ movieId },
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
            case FAVS:
                long newId = db.insert(PopDBContract.FavEntry.TABLE_NAME, null, contentValues);
                if (newId > 0) {
                    return ContentUris.withAppendedId(PopDBContract.FavEntry.CONTENT_URI, newId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
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
            case FAV_WITH_ID:
                String movieId = uri.getPathSegments().get(1);
                return db.delete(
                        PopDBContract.FavEntry.TABLE_NAME,
                        PopDBContract.FavEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{ movieId });
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
