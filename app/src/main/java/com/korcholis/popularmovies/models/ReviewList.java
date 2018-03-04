package com.korcholis.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewList {
    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
