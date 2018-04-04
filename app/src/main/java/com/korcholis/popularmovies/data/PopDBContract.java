package com.korcholis.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PopDBContract {
    public static final String AUTHORITY = "com.korcholis.popularmovies";
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_MOST_POPULAR = "popular";
    public static final String PATH_HIGH_RATED = "top_rated";
    public static final String PATH_FAVORITE = "favs";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private PopDBContract() {}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_AVG_VOTE = "avg_vote";
        public static final String COLUMN_IS_POPULAR = "is_popular";
        public static final String COLUMN_IS_HIGH_RATED = "is_high_rated";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
    }
}
