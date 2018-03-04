package com.korcholis.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {
    @SerializedName("results")
    private List<Video> results;

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
