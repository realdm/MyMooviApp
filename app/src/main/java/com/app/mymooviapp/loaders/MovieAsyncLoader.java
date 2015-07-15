package com.app.mymooviapp.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.mymooviapp.MainActivity;
import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.handlers.MovieHandler;
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

    private String queryType;

    private StringBuilder urlBuilder;

    private AppCompatActivity activtiy;

    //JSON Response keys
    private final String TITLE_KEY="original_title";

    private final String SYNOPSIS_KEY="overview";

    private final String RELEASE_DATE_KEY="release_date";

    private final String POSTER_PATH_KEY="poster_path";

    private final String RATING_KEY="vote_average";

    private final String MOVIE_ID="id";

    private final String BACK_DROP_PATH="backdrop_path";


    public MovieAsyncLoader(Context context, String queryType) {
        super(context);

        activtiy = (AppCompatActivity)mContext;

        this.queryType = queryType;
    }

    @Override
    public List<Movie> loadInBackground() {

        List<Movie> movies = new ArrayList<Movie>();


        Log.i("MovieSyncLoader","QueryType is: "+queryType);
        if(!queryType.equals(MainActivity.QUERY_TYPE_FAVORITED)) {
            //make request to API
            String url = buildUrl(queryType);
            try
            {
                movies = loadedMovies(loadMovies(url));
            }
            catch(IOException e)
            {

            }
        }
        else{
            //load favorited movies

            Log.i("MovieSync Adapter","Loading movie from database");
            movies = loadFavoritedMovies();

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



    public String buildUrl(String queryType)
    {
        urlBuilder = new StringBuilder(Constants.BASE_URL);

        if(queryType.equals(MainActivity.QUERY_TYPE_POPULAR)) {
            urlBuilder.append(Constants.POPULAR_DESC_PARAMETER).append("&api_key="+ Constants.API_KEY);
        }
        else {

            urlBuilder.append(Constants.VOTED_DESC).append("&api_key="+ Constants.API_KEY);
        }

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

                int id = singleMovie.getInt(MOVIE_ID);
                String title = singleMovie.getString(TITLE_KEY);
                String overview = singleMovie.getString(SYNOPSIS_KEY);
                String releaseDate = singleMovie.getString(RELEASE_DATE_KEY);
                String posterPath = singleMovie.getString(POSTER_PATH_KEY);
                int rating = singleMovie.getInt(RATING_KEY);
                String backDropPath = singleMovie.getString(BACK_DROP_PATH);

                Movie movie = new Movie(id,title,overview,releaseDate,posterPath,rating,backDropPath);

                loadedMovies.add(movie);
            }
        }
        catch(JSONException e)
        {

        }

        return loadedMovies;

    }

    public List<Movie> loadFavoritedMovies()
    {
        Cursor mLoadedMovies = getContext().getContentResolver().query(MovieContract.Movie.CONTENT_URI,null,null,null,null,null);

        List<Movie> movies = new ArrayList<Movie>();

        if(mLoadedMovies!=null){
            while(mLoadedMovies.moveToNext()){

                Movie movie = MovieHandler.constructMovieFromCursor(mLoadedMovies);

                movies.add(movie);
            }
        }

        Log.i("MovieSyncAdapter","Loaded Movie: "+movies.size());
        return movies;
    }


}


