package com.korcholis.popularmovies.data;

import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.models.MoviesList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TMDbApiSignature {
    String PATH_MOVIES = "movie";
    String QUERY_SORT_CRITERIA = "sortCriteria";
    String QUERY_MOVIE_ID = "movieId";

    @GET(PATH_MOVIES + "/{" + QUERY_SORT_CRITERIA + "}")
    Single<MoviesList> movieList(@Path(QUERY_SORT_CRITERIA) TMDbApi.SortCriteria sortCriteria);

    @GET(PATH_MOVIES + "/{" + QUERY_MOVIE_ID + "}")
    Single<Movie> movie(@Path(QUERY_MOVIE_ID) int movieId);

}
