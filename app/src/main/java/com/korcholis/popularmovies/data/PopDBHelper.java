package com.korcholis.popularmovies.data;

import android.content.ContentValues;
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
    public static final int DB_VERSION = 1;

    public PopDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @NonNull
    public static List<Movie> cursorToMovies(Cursor cursor) {
        List<Movie> favs = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            favs.add(new Movie(
                    cursor.getInt(cursor.getColumnIndex(PopDBContract.FavEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(PopDBContract.FavEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(PopDBContract.FavEntry.COLUMN_POSTER))
            ));
        }

        return favs;
    }

    public static long addFav(int movieId, String title, String poster, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(PopDBContract.FavEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(PopDBContract.FavEntry.COLUMN_TITLE, title);
        cv.put(PopDBContract.FavEntry.COLUMN_POSTER, poster);

        return db.insert(PopDBContract.FavEntry.TABLE_NAME, null, cv);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVS_TABLE = "CREATE TABLE " +
                PopDBContract.FavEntry.TABLE_NAME + " (" +
                PopDBContract.FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PopDBContract.FavEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                PopDBContract.FavEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                PopDBContract.FavEntry.COLUMN_POSTER + " TEXT NOT NULL," +
                PopDBContract.FavEntry.COLUMN_FAV_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL_DROP_FAVS_TABLE = "DROP TABLE IF EXISTS " + PopDBContract.FavEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DROP_FAVS_TABLE);
        onCreate(sqLiteDatabase);
    }
}
