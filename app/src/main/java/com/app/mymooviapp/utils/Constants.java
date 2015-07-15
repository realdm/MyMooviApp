package com.app.mymooviapp.utils;

/**
 * Created by Mac on 6/11/2015.
 */
public class Constants {

    public static final String API_KEY="e6a19d1b2b629a804e4a426d6c5a3410";

    public static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

    public static final String SINGLE_MOVIE_BASE_URL="https://api.themoviedb.org/3/movie/";

    public static final String FIELDS_TO_APPEND="&append_to_response=genres,trailers,credits,reviews";

    public static final String POPULAR_DESC_PARAMETER = "sort_by=popularity.desc";

    public static final String VOTED_DESC="sort_by=vote_average.desc";

    public static final String THUMB_URL_BASE_PATH="http://image.tmdb.org/t/p/w185//";

    public static final String BACKDROP_URL_BASE_PATH="http://image.tmdb.org/t/p/w500/";
}
