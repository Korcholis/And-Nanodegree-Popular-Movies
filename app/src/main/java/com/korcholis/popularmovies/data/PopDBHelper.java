package com.korcholis.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.korcholis.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class PopDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "popmovies.db";
    public static final int DB_VERSION = 2;

    public PopDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @NonNull
    public static List<Movie> cursorToMovies(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();
        cursor.moveToFirst();
        do {
            Movie movie = new Movie(
                    cursor.getString(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_POSTER)),
                    cursor.getString(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_SYNOPSIS)),
                    cursor.getString(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_RELEASE_DATE)),
                    cursor.getInt(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_TITLE)),
                    cursor.getDouble(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_AVG_VOTE))
            );
            movie.setFavorite(cursor.getInt(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_IS_FAVORITE)) == 1);
            movie.setPopular(cursor.getInt(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_IS_POPULAR)) == 1);
            movie.setHighRated(cursor.getInt(cursor.getColumnIndex(PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED)) == 1);
            movies.add(movie);
        } while (cursor.moveToNext());

        return movies;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                PopDBContract.MovieEntry.TABLE_NAME + " (" +
                PopDBContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PopDBContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_AVG_VOTE + " REAL NOT NULL," +
                PopDBContract.MovieEntry.COLUMN_IS_POPULAR + " INTEGER DEFAULT 0," +
                PopDBContract.MovieEntry.COLUMN_IS_HIGH_RATED + " INTEGER DEFAULT 0," +
                PopDBContract.MovieEntry.COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0"+
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DROP_MOVIES_TABLE = "DROP TABLE IF EXISTS " + PopDBContract.MovieEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DROP_MOVIES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
