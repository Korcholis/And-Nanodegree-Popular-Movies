package com.korcholis.popularmovies.data;

import com.korcholis.popularmovies.models.Movie;
import com.korcholis.popularmovies.models.MoviesList;
import com.korcholis.popularmovies.models.ReviewList;
import com.korcholis.popularmovies.models.VideoList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TMDbApiSignature {
    String PATH_MOVIES = "movie";
    String PATH_REVIEWS = "reviews";
    String PATH_VIDEOS = "videos";
    String QUERY_SORT_CRITERIA = "sortCriteria";
    String QUERY_MOVIE_ID = "movieId";

    @GET(PATH_MOVIES + "/{" + QUERY_SORT_CRITERIA + "}")
    Single<MoviesList> movieList(@Path(QUERY_SORT_CRITERIA) TMDbApi.SortCriteria sortCriteria);

    @GET(PATH_MOVIES + "/{" + QUERY_MOVIE_ID + "}")
    Single<Movie> movie(@Path(QUERY_MOVIE_ID) int movieId);

    @GET(PATH_MOVIES + "/{" + QUERY_MOVIE_ID + "}/" + PATH_REVIEWS)
    Single<ReviewList> reviewsForMovie(@Path(QUERY_MOVIE_ID) int movieId);

    @GET(PATH_MOVIES + "/{" + QUERY_MOVIE_ID + "}/" + PATH_VIDEOS)
    Single<VideoList> videosForMovie(@Path(QUERY_MOVIE_ID) int movieId);

}
