package com.app.mymooviapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.mymooviapp.data.MovieContract.Movie;
import com.app.mymooviapp.data.MovieContract.Cast;
import com.app.mymooviapp.data.MovieContract.Trailer;

/**
 * Created by Mac on 6/25/2015.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="moovies_db";

    private static final int DATABASE_VERSION=1;

    public interface TABLES{

        String MOVIE="movies";

        String CAST="cast";

        String TRAILER="trailer";



    }

    String CREATE_TABLE_MOVIE="CREATE TABLE "+TABLES.MOVIE+" ("
            + Movie._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Movie.MOVIE_ID+" INTEGER NOT NULL, "
            + Movie.MOVIE_TITLE+" TEXT NOT NULL, "
            + Movie.OVERVIEW+" TEXT NOT NULL, "
            + Movie.GENRES+" TEXT NOT NULL, "
            + Movie.RELEASE_DATE+" RELEASE DATE, "
            + Movie.POSTER_PATH+" TEXT NOT NULL, "
            + Movie.BACKDROP_PATH+" TEXT NOT NULL, "
            + Movie.DIRECTOR+" TEXT NOT NULL, "
            + Movie.IMPORT_HASH_KEY+" TEXT NOT NULL, "
            + Movie.USER_RATING+" FLOAT NOT NULL, "
            + Movie.RUNTIME+" INTEGER NOT NULL, "
            + "UNIQUE (" + Movie.MOVIE_ID + ") ON CONFLICT REPLACE)";

    String CREATE_TABLE_CAST="CREATE TABLE "+TABLES.CAST+" ("
            + Cast._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Cast.CHARACTER_NAME+" TEXT NOT NULL, "
            + Cast.ACTOR_NAME+" TEXT NOT NULL, "
            + Cast.PROFILE_URL+" TEXT NOT NULL, "
            + Cast.MOVIE_ID+" INTEGER NOT NULL, "
            + Cast.IMPORT_HASH_KEY+" TEXT NOT NULL, "
            + "UNIQUE (" + Cast.CHARACTER_NAME + ") ON CONFLICT REPLACE, "
            + "FOREIGN KEY ("+Cast.MOVIE_ID+") REFERENCES "+
            TABLES.MOVIE+" ("+Movie.MOVIE_ID+"));";

    String CREATE_TABLE_TRAILER="CREATE TABLE "+TABLES.TRAILER+" ("
            + Trailer._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Trailer.NAME+" TEXT NOT NULL, "
            + Trailer.YOUTUBE_ID+" TEXT NOT NULL, "
            + Trailer.IMPORT_HASH_KEY+" TEXT NOT NULL, "
            + Trailer.MOVIE_ID+" INTEGER NOT NULL, "
            +"FOREIGN KEY ("+Trailer.MOVIE_ID+") REFERENCES "
            + TABLES.MOVIE+" ("+Movie.MOVIE_ID+"));";


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MOVIE);
        db.execSQL(CREATE_TABLE_CAST);
        db.execSQL(CREATE_TABLE_TRAILER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
