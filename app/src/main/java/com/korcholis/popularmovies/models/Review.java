package com.korcholis.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    String id;
    @SerializedName("author")
    String author;
    @SerializedName("content")
    String content;
    @SerializedName("url")
    String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
