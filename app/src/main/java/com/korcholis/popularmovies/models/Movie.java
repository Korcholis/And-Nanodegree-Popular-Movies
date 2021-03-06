package com.korcholis.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final String TMDB_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    @SerializedName("id")
    private Integer id;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String synopsis;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private Double voteAverage;

    private boolean isFavorite;
    private boolean isPopular;
    private boolean isHighRated;

    public Movie(String poster, String overview, String releaseDate, Integer id,
                 String title, Double voteAverage) {
        this.poster = poster;
        this.synopsis = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public Movie(Integer id, String title, String poster) {
        this.id = id;
        this.title = title;
        this.poster = poster;

    }

    public String getPoster() {
        return getPoster(false);
    }

    public String getPoster(boolean relativePath) {
        return (relativePath? "" : TMDB_IMAGE_URL) + poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public boolean isHighRated() {
        return isHighRated;
    }

    public void setHighRated(boolean highRated) {
        isHighRated = highRated;
    }
}
