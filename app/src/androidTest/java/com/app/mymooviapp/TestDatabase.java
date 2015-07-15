package com.app.mymooviapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.app.mymooviapp.data.MovieContract.Movie;
import com.app.mymooviapp.data.MovieContract.Cast;
import com.app.mymooviapp.data.MovieContract.Trailer;

import com.app.mymooviapp.data.MoviesDbHelper;

/**
 * Created by Mac on 6/25/2015.
 */

public class TestDatabase extends AndroidTestCase {

    public SQLiteDatabase db;

    public void testCreateDb()
    {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);

        db = new MoviesDbHelper(mContext).getWritableDatabase();

        assertTrue(db.isOpen());

    }


    public void testInsertDb()
    {

        db = new MoviesDbHelper(mContext).getWritableDatabase();

        assertTrue(db.isOpen());

        ContentValues movieValues = new ContentValues();

        movieValues.put(Movie.MOVIE_TITLE,"Avengers age of ultron");
        movieValues.put(Movie.DIRECTOR,"Jhoss Whedon");
        movieValues.put(Movie.MOVIE_ID,12040);
        movieValues.put(Movie.OVERVIEW,"Some overview of the movie");
        movieValues.put(Movie.RELEASE_DATE,"2015-05-20");
        movieValues.put(Movie.POSTER_PATH,"poster");
        movieValues.put(Movie.BACKDROP_PATH,"backdropath");
        movieValues.put(Movie.USER_RATING, 2.4);
        movieValues.put(Movie.RUNTIME, 124);
        movieValues.put(Movie.IMPORT_HASH_KEY, "someUniqueHash");

        long movieId = db.insert(MoviesDbHelper.TABLES.MOVIE,null,movieValues);

        assertEquals(1, movieId);

        ContentValues castValues = new ContentValues();

        castValues.put(Cast.ACTOR_NAME,"Rober D. Junior");

        castValues.put(Cast.MOVIE_ID,1);

        castValues.put(Cast.CHARACTER_NAME,"Iron Man");

        castValues.put(Cast.PROFILE_URL,"Profile url");

        castValues.put(Cast.IMPORT_HASH_KEY, "hash key");

        long castId = db.insert(MoviesDbHelper.TABLES.CAST, null, castValues);

        assertEquals(1,castId);


        ContentValues trailerValues = new ContentValues();

        trailerValues.put(Trailer.MOVIE_ID,1);
        
    }

}
