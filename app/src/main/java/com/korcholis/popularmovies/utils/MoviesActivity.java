package com.korcholis.popularmovies.utils;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.korcholis.popularmovies.R;

public class MoviesActivity extends AppCompatActivity {


    protected void showMovieListErrorToast(boolean alsoExit) {
        if (alsoExit) {
            finish();
        }
        Toast.makeText(this, R.string.error_movies_wrong_data, Toast.LENGTH_SHORT).show();
    }


    protected void showMovieErrorToast(boolean alsoExit) {
        if (alsoExit) {
            finish();
        }
        Toast.makeText(this, R.string.error_movie_not_exists, Toast.LENGTH_SHORT).show();
    }

    protected void showNoConnectionErrorToast(boolean alsoExit) {
        if (alsoExit) {
            finish();
        }
        Toast.makeText(this, R.string.error_no_connection, Toast.LENGTH_SHORT).show();
    }
}
