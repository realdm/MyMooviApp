package com.app.mymooviapp.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mac on 6/24/2015.
 */
public class MovieContract {


    public interface MovieColumns
    {
        String MOVIE_TITLE="title";

        String MOVIE_ID = "id";

        String OVERVIEW="overview";

        String RELEASE_DATE="release_date";

        String GENRES="genres";

        String POSTER_PATH="poster_path";

        String BACKDROP_PATH="backdrop_path";

        String DIRECTOR="director";

        String USER_RATING="user_rating";

        String RUNTIME="runtime";

        String IMPORT_HASH_KEY="import_hash";
    }

    public interface CastColumns
    {
        String ACTOR_NAME="actor_name";

        String PROFILE_URL="profile_url";

        String CHARACTER_NAME="character_name";

        String IMPORT_HASH_KEY="import_hash";

        String MOVIE_ID="movie_id";

    }

    public interface TraileColumns
    {
        String NAME="trailer_name";

        String MOVIE_ID="movie_id";

        String YOUTUBE_ID="youtubeID";

        String IMPORT_HASH_KEY="import_hash";
    }

    /** The root of our uri*/
    public static final String CONTENT_AUTHORITY="com.app.mymooviapp";

    /**Uri Base Path*/
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);

    //paths

    private static final String MOVIE_PATH="movie";

    private static final String CAST_PATH="cast";

    private static final String TRAILER_PATH="trailer";

    public static class Movie implements BaseColumns,MovieColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.mymooviapp.movie";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.mymooviapp.movie";


        public static Uri buildUriFromId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildFromMovieApiId(int id){
            return CONTENT_URI.buildUpon().appendPath("api").appendPath(Integer.toString(id)).build();

        }

    }

    public static class Cast implements  BaseColumns,CastColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(CAST_PATH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.mymooviapp.cast";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.mymooviapp.cast";


        public static Uri buildUriFromId(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildMovieCastUri(long movieId) {
            return CONTENT_URI.buildUpon().appendPath("movie").appendPath(Long.toString(movieId)).build();
        }
    }

    public static class Trailer implements BaseColumns,TraileColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(TRAILER_PATH).build();


        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.mymooviapp.trailer";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.mymooviapp.trailer";


        public static Uri buildUriFromId(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }


        public static Uri buildMovieTrailer(long movieId)
        {
            return CONTENT_URI.buildUpon().appendPath("movie").appendPath(Long.toString(movieId)).build();
        }
    }


}
