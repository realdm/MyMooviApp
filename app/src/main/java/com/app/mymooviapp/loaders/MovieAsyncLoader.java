package com.app.mymooviapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/11/2015.
 */
public class MovieAsyncLoader extends AsyncTaskLoader<List<Movie>> {

    private Context mContext;

    private boolean isPoplular = true;

    private StringBuilder urlBuilder;

    private AppCompatActivity activtiy;

    //JSON Response keys
    private final String TITLE_KEY="original_title";

    private final String SYNOPSIS_KEY="overview";

    private final String RELEASE_DATE_KEY="release_date";

    private final String POSTER_PATH_KEY="poster_path";

    private final String RATING_KEY="vote_average";


    public MovieAsyncLoader(Context context, boolean isPopular) {
        super(context);

        activtiy = (AppCompatActivity)mContext;

        this.isPoplular = isPopular;
    }

    @Override
    public List<Movie> loadInBackground() {

        List<Movie> movies = new ArrayList<Movie>();

        String url = buildUrl(isPoplular);
        try
        {
            movies = loadedMovies(loadMovies(url));
        }
        catch(IOException e)
        {

        }

        return movies;
    }


    public String loadMovies(String url) throws IOException
    {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }



    public String buildUrl(boolean isPoplular)
    {
        urlBuilder = new StringBuilder(Constants.BASE_URL);
        if(isPoplular)
        {
            urlBuilder.append(Constants.POPULAR_DESC_PARAMETER).append("&api_key="+ Constants.API_KEY);
        }
        else
        {
            urlBuilder.append(Constants.VOTED_DESC).append("&api_key="+ Constants.API_KEY);
        }

        Log.i("MovieSyncLoader", "The url to query is: " + urlBuilder.toString());

        return urlBuilder.toString();
    }

    public List<Movie> loadedMovies(String response)
    {

        List<Movie> loadedMovies = new ArrayList<Movie>();

        JSONObject jsonResponse;

        try
        {
            jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");

            for(int i = 0;i<results.length();i++)
            {

                JSONObject singleMovie = results.getJSONObject(i);

                String title = singleMovie.getString(TITLE_KEY);
                String overview = singleMovie.getString(SYNOPSIS_KEY);
                String releaseDate = singleMovie.getString(RELEASE_DATE_KEY);
                String posterPath = singleMovie.getString(POSTER_PATH_KEY);
                int rating = singleMovie.getInt(RATING_KEY);

                Movie movie = new Movie(title,overview,releaseDate,posterPath,rating);


                loadedMovies.add(movie);
            }
        }
        catch(JSONException e)
        {

        }

        return loadedMovies;

    }


}


