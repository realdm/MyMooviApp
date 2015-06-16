package com.app.mymooviapp.models;

import java.io.Serializable;

/**
 * Created by Mac on 6/11/2015.
 */
public class Movie implements Serializable {

    private String  title;

    private String overview;

    private String releaseDate;

    private String posterPath;

    private float userRating;

    public Movie(String title, String overview, String releaseDate, String posterPath, float userRating) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.userRating = userRating;
    }


    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public float getUserRating() {
        return userRating;
    }
}
