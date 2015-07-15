package com.app.mymooviapp.handlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.app.mymooviapp.MovieDetails;
import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.models.CastMember;
import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.models.Trailer;

import java.util.HashMap;

/**
 * Created by Mac on 7/6/2015.
 */
public class MovieHandler {


    private Movie movie;

    private Context mContext;

    private CastHandler castHandler;

    private TrailerHandler trailerHandler;

    public MovieHandler (Context context){

        mContext = context;
        castHandler = new CastHandler(mContext);
        trailerHandler = new TrailerHandler(mContext);
    }

    //create content values for


    public ContentValues generateMovieContentValues(Movie movie){

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.Movie.MOVIE_TITLE,movie.getTitle());
        movieValues.put(MovieContract.Movie.DIRECTOR,movie.getDirector());
        movieValues.put(MovieContract.Movie.GENRES,movie.getGenres() );
        movieValues.put(MovieContract.Movie.MOVIE_ID,movie.getId());
        movieValues.put(MovieContract.Movie.OVERVIEW,movie.getOverview());
        movieValues.put(MovieContract.Movie.RELEASE_DATE,movie.getReleaseDate());
        movieValues.put(MovieContract.Movie.POSTER_PATH,movie.getPosterPath());
        movieValues.put(MovieContract.Movie.BACKDROP_PATH,movie.getBackDropPath());
        movieValues.put(MovieContract.Movie.USER_RATING, movie.getUserRating());
        movieValues.put(MovieContract.Movie.RUNTIME, movie.getRuntime());
        movieValues.put(MovieContract.Movie.IMPORT_HASH_KEY, movie.getImportedHashCode());

        return movieValues;
    }

    public void insertOrUpdate(Movie movie){

        HashMap<String, String> movieImportHashes = loadMovieHAshes();

        ContentValues values = generateMovieContentValues(movie);

        long _id = -1;

        if(movieImportHashes.get(movie.getId())!=null) {
            if(movieImportHashes.containsKey(movie.getId())&&!movieImportHashes.get(movie.getId()).equals(movie.getImportedHashCode())) {
                //update movie

            }
            else {
                //all gud nothing to update
            }
        }
        else {
            //insert movie into database
            Uri movieUri = mContext.getContentResolver().insert(MovieContract.Movie.CONTENT_URI, values);
            _id = ContentUris.parseId(movieUri);

        }

        //insert or update cast
        if(movie.getCast().size()>0){
            for(int i = 0;i<movie.getCast().size();i++){

                castHandler.insertOrUpdateCast(movie.getCast().get(i),(int)_id);
            }
        }

        if(movie.getTrailers().size()>0){
            for(Trailer trailer : movie.getTrailers()){
                trailerHandler.insertOrUpdateTrailer(trailer,(int)_id);
            }
        }
       //insert or update trailer


    }

    public boolean deleteMovie(Movie movie){

        HashMap<String, String> loadedIds = loadAllMovieIDs();

        String key = movie.getTitle()+movie.getId();

        boolean movieDeleted = false;

        if(loadedIds.get(key) != null){

            if(loadedIds.containsKey(key)){

                int id = Integer.valueOf(loadedIds.get(key));

                Log.i("MovieHandler","MovieId: "+id);
                Uri uri = MovieContract.Movie.buildUriFromId(id);

                int deletedRows = mContext.getContentResolver().delete(uri,null,null);
                Log.i("Movie HAndler", "Movie deleted rows: "+deletedRows);
                if(deletedRows>0){
                    Log.i("Movie HAndler", "Movie deleted");

                    Uri castDeletionUri = MovieContract.Cast.buildMovieCastUri(movie.getId());

                    mContext.getContentResolver().delete(castDeletionUri,null,null);

                    Uri trailerDeletionUri = MovieContract.Trailer.buildUriFromId(movie.getId());

                    mContext.getContentResolver().delete(castDeletionUri,null,null);

                    movieDeleted = true;
                }
            }
        }

        return movieDeleted;

    }

    public static Movie constructMovieFromCursor(Cursor cursor){

        String title = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.MOVIE_TITLE));
        String director = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.DIRECTOR));
        int id = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.MOVIE_ID));
        String genres =  cursor.getString(cursor.getColumnIndex(MovieContract.Movie.GENRES));
        String[] movieGenres = genres.split(" ");
        String overview = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.OVERVIEW));
        String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.RELEASE_DATE));
        String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.POSTER_PATH));
        String backdropath = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.BACKDROP_PATH));
        float userRating = cursor.getFloat(cursor.getColumnIndex(MovieContract.Movie.USER_RATING));
        int runtime = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.RUNTIME));


        Movie movie = new Movie(id,title,overview,releaseDate,posterPath,userRating,backdropath);
        movie.setDirector(director);
        movie.setRuntime(runtime);
        for(String genre : movieGenres){
            movie.addGenres(genre);
        }

        return movie;
    }

    public HashMap<String, String> loadMovieHAshes(){

        Cursor mCursor = mContext.getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);

        HashMap<String, String> movieKeys = new HashMap<>();

        if(mCursor != null){

            while(mCursor.moveToNext())
            {
                String id = mCursor.getString(mCursor.getColumnIndex(MovieContract.Movie.MOVIE_ID));

                String importHash = mCursor.getString(mCursor.getColumnIndex(MovieContract.Movie.IMPORT_HASH_KEY));

                movieKeys.put(id, importHash);
            }
        }
        mCursor.close();

        return movieKeys;
    }

    public HashMap<String,String> loadAllMovieIDs(){

        String[] columns = {
                MovieContract.Movie._ID,
                MovieContract.Movie.MOVIE_ID,
                MovieContract.Movie.MOVIE_TITLE
        };

        HashMap<String,String> movieIds = new HashMap<>();

        Cursor mCursor = mContext.getContentResolver().query(MovieContract.Movie.CONTENT_URI, null, null, null, null);

        if(mCursor!=null){
            while(mCursor.moveToNext()){
                int id = mCursor.getInt(mCursor.getColumnIndex(MovieContract.Movie._ID));
                int apiId = mCursor.getInt(mCursor.getColumnIndex(MovieContract.Movie.MOVIE_ID));
                String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.Movie.MOVIE_TITLE));

                movieIds.put(title+apiId,Integer.toString(id));
            }
        }

        mCursor.close();

        return movieIds;

    }

    /*
        HELPER METHODS
     */
    public static  boolean isMovieFavorited(Movie movie,Context mContext){

        boolean isFavorited = false;

        String[] columns = {
                MovieContract.Movie._ID,
                MovieContract.Movie.MOVIE_ID,
        };



        Uri uri = MovieContract.Movie.buildFromMovieApiId(movie.getId());
        Cursor cursor = mContext.getContentResolver().query(uri,columns,null,null,null,null);

        if(cursor!=null){

            while(cursor.moveToNext()){
                isFavorited = true;

            }

        }

        cursor.close();

        return isFavorited;
    }

    public static Movie loadMovieDetails(Movie movie, Context mContext){

        Uri movieUri = MovieContract.Movie.buildFromMovieApiId(movie.getId());

        Cursor movieCursor = mContext.getContentResolver().query(movieUri,null , null, null, null, null);

        int movieId =-1;

        if(movieCursor != null){
            while(movieCursor.moveToNext()){

                movieId = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.Movie._ID));
                movie = constructMovieFromCursor(movieCursor);
            }

        }

        movieCursor.close();

        Uri castUri = MovieContract.Cast.buildMovieCastUri(movieId);
        Cursor castCursor = mContext.getContentResolver().query(castUri,null,null,null,null,null);

        if(castCursor!=null){
            while(castCursor.moveToNext()){

                String character = castCursor.getString(castCursor.getColumnIndex(MovieContract.Cast.CHARACTER_NAME));
                String actorName = castCursor.getString(castCursor.getColumnIndex(MovieContract.Cast.ACTOR_NAME));
                String profileUrl = castCursor.getString(castCursor.getColumnIndex(MovieContract.Cast.PROFILE_URL));

                CastMember castMember = new CastMember(character,actorName,profileUrl);
                movie.addCastMember(castMember);
            }
            castCursor.close();
        }

        Uri trailerUri = MovieContract.Trailer.buildMovieTrailer(movieId);
        Cursor trailerCursor = mContext.getContentResolver().query(trailerUri,null,null,null,null,null);

        if(trailerCursor!=null){
            while(trailerCursor.moveToNext()){
                String youtubeId = trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.Trailer.YOUTUBE_ID));
                String name = trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.Trailer.NAME));
                Trailer trailer = new Trailer(name, youtubeId);
                movie.addTrailers(trailer);
            }
            trailerCursor.close();
        }

        return movie;
    }
}
