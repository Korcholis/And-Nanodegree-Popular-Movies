package com.korcholis.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PopDBContract {
    public static final String AUTHORITY = "com.korcholis.popularmovies";
    public static final String PATH_FAVS = "favorites";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private PopDBContract() {}

    public static class FavEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVS).build();

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_FAV_TIME = "fav_time";
    }
}
