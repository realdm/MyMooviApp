package com.app.mymooviapp.loaders;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.app.mymooviapp.MovieDetails;
import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.handlers.MovieHandler;
import com.app.mymooviapp.models.CastMember;
import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.models.Trailer;
import com.app.mymooviapp.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Mac on 6/19/2015.
 */
public class MovieDetailsLoader extends AsyncTaskLoader<Movie> {

    private Movie receivedMovie;

    private MovieHandler movieHandler;

    //fields

    private static final String RUNTIME="runtime";

    private static final String GENRES_KEY = "genres";

    private  final String GENRE_NAME_KEY="name";

    private  final String TRAIERS_KEY = "trailers";

    private  final String YOUTUBE_KEY= "youtube";

    private  final String TRAILER_SOURCE_KEY = "source";

    private  final String TRAILER_NAME_KEY= "name";

    private  final String CREDITS_KEY="credits";

    private  final String CAST_KEY="cast";

    private  final String CHARACTER_KEY="character";

    private final String CHARACTER_ACTOR_NAME_KEY= "name";

    private final String CHAACTER_PROFILE_PATH_KEY="profile_path";

    private final String REVIEW_KEY="reviews";

    private final String REVIEW_RESULT_KEY="results";

    private final String CONTENT_KEY="content";

    private final String CREW_KEY="crew";

    private final String DIRECTOR_NAME="name";

    public MovieDetailsLoader(Context context,Movie movie) {
        super(context);
        this.receivedMovie  = movie;
    }

    @Override
    public Movie loadInBackground() {

        boolean isFavorited = MovieHandler.isMovieFavorited(receivedMovie,getContext());
        if(isFavorited==true){

          receivedMovie = MovieHandler.loadMovieDetails(receivedMovie,getContext());

          receivedMovie.setFavorited(true);

        }
        else{

            String requestUrl = Constants.SINGLE_MOVIE_BASE_URL+receivedMovie.getId()+"?api_key="+Constants.API_KEY+Constants.FIELDS_TO_APPEND;

            try {

                receivedMovie = addDetailsToMovie(loadMovieDetails(requestUrl));

                receivedMovie.setFavorited(false);

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

        return receivedMovie;
    }

    public String loadMovieDetails(String url) throws IOException
    {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    public Movie addDetailsToMovie(String response)
    {
        Movie movie = this.receivedMovie;

        JSONObject jsonResponse;

        try {


            jsonResponse = new JSONObject(response);

            JSONObject object = jsonResponse;

            movie.setRuntime(object.getInt(RUNTIME));

            JSONArray genres = object.getJSONArray(GENRES_KEY);

            for(int i=0;i<genres.length();i++)
            {
                JSONObject genre = genres.getJSONObject(i);
                String name = genre.getString(GENRE_NAME_KEY);

                movie.addGenres(name);
            }

            JSONObject trailers = object.getJSONObject(TRAIERS_KEY);

            JSONArray youtube = trailers.getJSONArray(YOUTUBE_KEY);

            for(int i = 0 ;i<youtube.length();i++)
            {
                JSONObject trailer = youtube.getJSONObject(i);

                String trailerName = trailer.getString(TRAILER_NAME_KEY);

                String trailerSource = trailer.getString(TRAILER_SOURCE_KEY);

                Log.e("MovieDetailsLoader","TrailerSoure key is: "+trailerSource);

                Trailer trailerToAdd = new Trailer(trailerName,trailerSource);

                Log.e("MovieDetailsLoader","TrailerSoure  from new object is key is: "+trailerToAdd.getSource());

                movie.addTrailers(trailerToAdd);
            }

            JSONObject credits = object.getJSONObject(CREDITS_KEY);

            JSONArray cast = credits.getJSONArray(CAST_KEY);

            for(int i = 0;i<4;i++)
            {
                JSONObject castObject = cast.getJSONObject(i);

                String character = castObject.getString(CHARACTER_KEY);

                String actorName = castObject.getString(CHARACTER_ACTOR_NAME_KEY);

                String photo_url = castObject.getString(CHAACTER_PROFILE_PATH_KEY);

                movie.addCastMember(new CastMember(character,actorName,photo_url));
            }

            JSONArray crew = credits.getJSONArray(CREW_KEY);

            JSONObject crewMember = crew.getJSONObject(0);

            String director = crewMember.getString(DIRECTOR_NAME);

            movie.setDirector(director);

            /*JSONObject reviews = object.getJSONObject(REVIEW_KEY);

            JSONArray results = reviews.getJSONArray(REVIEW_RESULT_KEY);

            if(results.length()>0)
            {
                for(int i = 0; i<results.length();i++)
                {
                    JSONObject reviewObject = results.getJSONObject(i);

                    String content = reviewObject.getString(CONTENT_KEY);

                    movie.addReviews(content);
                }
            }*/



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }


}
