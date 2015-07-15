package com.app.mymooviapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.data.MoviesDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mac on 7/1/2015.
 */
public class TestProvider extends AndroidTestCase {

    SQLiteDatabase db;

    public void testCreateDb()
    {
        mContext.deleteDatabase(MoviesDbHelper.DATABASE_NAME);

        db = new MoviesDbHelper(mContext).getWritableDatabase();

        assertTrue(db.isOpen());
    }

    public void testGetType()
    {
        Uri uri = MovieContract.Movie.CONTENT_URI;

        String type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Movie.CONTENT_TYPE,type);

        uri = MovieContract.Movie.buildUriFromId(1);

        type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Movie.CONTENT_ITEM_TYPE,type);

        //cast Types

        uri = MovieContract.Cast.CONTENT_URI;

        type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Cast.CONTENT_TYPE,type);

        uri = MovieContract.Cast.buildMovieCastUri(1);

        type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Cast.CONTENT_TYPE,type);

        //trailer types

        uri = MovieContract.Trailer.CONTENT_URI;

        type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Trailer.CONTENT_TYPE,type);


        uri = MovieContract.Trailer.buildMovieTrailer(1);

        type = mContext.getContentResolver().getType(uri);

        assertEquals(MovieContract.Trailer.CONTENT_TYPE,type);



    }

    public void testInsert()
    {
        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieContract.Movie.MOVIE_TITLE,"Avengers age of ultron");
        movieValues.put(MovieContract.Movie.DIRECTOR,"Jhoss Whedon");
        movieValues.put(MovieContract.Movie.MOVIE_ID,12040);
        movieValues.put(MovieContract.Movie.OVERVIEW,"Some overview of the movie");
        movieValues.put(MovieContract.Movie.RELEASE_DATE,"2015-05-20");
        movieValues.put(MovieContract.Movie.POSTER_PATH,"poster");
        movieValues.put(MovieContract.Movie.BACKDROP_PATH,"backdropath");
        movieValues.put(MovieContract.Movie.USER_RATING, 2.4);
        movieValues.put(MovieContract.Movie.RUNTIME, 124);
        movieValues.put(MovieContract.Movie.IMPORT_HASH_KEY, "someUniqueHash");

        Uri uri = mContext.getContentResolver().insert(MovieContract.Movie.CONTENT_URI,movieValues);

        long id = ContentUris.parseId(uri);

        assertEquals(1, id);

        ContentValues castValues = new ContentValues();

        castValues.put(MovieContract.Cast.ACTOR_NAME,"Rober D. Junior");

        castValues.put(MovieContract.Cast.MOVIE_ID,1);

        castValues.put(MovieContract.Cast.CHARACTER_NAME,"Iron Man");

        castValues.put(MovieContract.Cast.PROFILE_URL,"Profile url");

        castValues.put(MovieContract.Cast.IMPORT_HASH_KEY, "hash key");


        uri = mContext.getContentResolver().insert(MovieContract.Cast.CONTENT_URI,castValues);

        long castId = ContentUris.parseId(uri);

        assertEquals(1, castId);


        ContentValues trailerValues = new ContentValues();

        trailerValues.put(MovieContract.Trailer.MOVIE_ID, 1);

        trailerValues.put(MovieContract.Trailer.NAME,"sample name");

        trailerValues.put(MovieContract.Trailer.YOUTUBE_ID,"youtube id");

        trailerValues.put(MovieContract.Trailer.IMPORT_HASH_KEY,"some Import Hash");

        uri = mContext.getContentResolver().insert(MovieContract.Trailer.CONTENT_URI,trailerValues);

        long traillerId = ContentUris.parseId(uri);

        assertEquals(1,traillerId);


        /**
         * TEST READ FROM PROVIDER
         */

        Uri queryUri = MovieContract.Movie.CONTENT_URI;

        Cursor cursor = mContext.getContentResolver().query(queryUri,null,null,null,null);

        validateCursor(cursor, movieValues);

        cursor.close();

        //query a movie by particular ID

        queryUri = MovieContract.Movie.buildUriFromId(1);

        cursor = mContext.getContentResolver().query(queryUri, null, null, null, null);

        validateCursor(cursor, movieValues);

        cursor.close();

        //query movie with api id
        queryUri = MovieContract.Movie.buildFromMovieApiId(12040);

        cursor = mContext.getContentResolver().query(queryUri,null,null,null,null,null);

        validateCursor(cursor,movieValues);


        //query all castmembers

        queryUri = MovieContract.Cast.CONTENT_URI;

        cursor = mContext.getContentResolver().query(queryUri,null,null,null,null);

        validateCursor(cursor, castValues);

        cursor.close();

        //query all castmembers of a certain movie

        queryUri = MovieContract.Cast.buildMovieCastUri(1);

        cursor = mContext.getContentResolver().query(queryUri,null,null,null,null);

        validateCursor(cursor, castValues);

        cursor.close();

        //query all trailers

        queryUri = MovieContract.Cast.buildMovieCastUri(1);

        cursor = mContext.getContentResolver().query(queryUri,null,null,null,null);

        validateCursor(cursor, castValues);

        cursor.close();

        //query all trailers of a certain movie

        queryUri = MovieContract.Trailer.buildMovieTrailer(1);

        cursor = mContext.getContentResolver().query(queryUri,null,null,null,null);

        validateCursor(cursor, trailerValues);

        cursor.close();


        //TEST DELETE MOVIE
        Uri movieWithIdUri = MovieContract.Movie.buildUriFromId(12040);
        int deletedMovie = mContext.getContentResolver().delete(movieWithIdUri,null,null);

        assertEquals(1 ,deletedMovie);
    }



    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}
