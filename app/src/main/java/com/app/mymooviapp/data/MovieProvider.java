package com.app.mymooviapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.Selection;
import android.util.Log;

import com.app.mymooviapp.utils.SelectionBuilder;

/**
 * Created by Mac on 7/1/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final int MOVIE=100;
    private static final int MOVIE_ID=101;
    private static final int MOVIE_API_ID=102;

    private static final int CAST=200;
    private static final int CAST_MOVIE_ID=201;

    private static final int TRAILER=300;
    private static final int TRAILER_MOVIE_ID=301;

    private MoviesDbHelper mOpenHelper;


    static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,"movie",MOVIE);

        matcher.addURI(authority,"movie/#",MOVIE_ID);

        matcher.addURI(authority,"movie/api/#",MOVIE_API_ID);

        matcher.addURI(authority,"cast",CAST);

        matcher.addURI(authority,"cast/movie/#",CAST_MOVIE_ID);

        matcher.addURI(authority,"trailer",TRAILER);

        matcher.addURI(authority,"trailer/movie/#",TRAILER_MOVIE_ID);


        return matcher;
    }

    UriMatcher matcher = buildUriMatcher();

    @Override
    public boolean onCreate() {

        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db=mOpenHelper.getReadableDatabase();

        SelectionBuilder builder= buildSelection(uri);

        Cursor cursor=builder.query(db,projection,null);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int match = matcher.match(uri);

        String type = null;

        switch (match)
        {
            case MOVIE:{
                type = MovieContract.Movie.CONTENT_TYPE;
                break;
            }
            case MOVIE_ID:{
                type = MovieContract.Movie.CONTENT_ITEM_TYPE;
                break;
            }
            case CAST:{
                type = MovieContract.Cast.CONTENT_TYPE;
                break;
            }
            case CAST_MOVIE_ID:{
                type = MovieContract.Cast.CONTENT_TYPE;
                break;
            }
            case TRAILER:{
                type = MovieContract.Trailer.CONTENT_TYPE;
                break;
            }
            case TRAILER_MOVIE_ID:{
                type = MovieContract.Trailer.CONTENT_TYPE;
                break;
            }
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = matcher.match(uri);

        Uri returnUri;

        switch (match){
            case MOVIE:{

                long _id  = db.insert(MoviesDbHelper.TABLES.MOVIE,null,values);

                returnUri = MovieContract.Movie.buildUriFromId(_id);

                break;
            }
            case CAST:{

                long _id  = db.insert(MoviesDbHelper.TABLES.CAST,null,values);

                returnUri = MovieContract.Cast.buildUriFromId(_id);
                break;
            }
            case TRAILER:{

                long _id = db.insert(MoviesDbHelper.TABLES.TRAILER, null, values);

                returnUri = MovieContract.Trailer.buildUriFromId(_id);

                break;

            }
            default:{
                throw new UnsupportedOperationException("Invalid Uri");
            }
        }

        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    private SelectionBuilder buildSelection(Uri uri){

        final SelectionBuilder mSelectionBuidler = new SelectionBuilder();

        int match = matcher.match(uri);

        switch (match){

            case MOVIE:{

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.MOVIE);
            }
            case MOVIE_ID:{

                Long _id = ContentUris.parseId(uri);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.MOVIE)
                        .where("_ID" + "=?", Long.toString(_id));
            }
            case MOVIE_API_ID:{

                String _id = uri.getPathSegments().get(2);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.MOVIE)
                        .where("id" + "=?", _id);
            }

            case CAST:{

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.CAST);
            }
            case CAST_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.CAST)
                        .where("movie_id"+"=?",movieId);
            }
            case TRAILER:{

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.TRAILER);
            }
            case TRAILER_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.TRAILER)
                        .where("movie_id"+"=?",movieId);
            }

            default:{

                throw new UnsupportedOperationException("Invalid Uri");
            }

        }

    }

    private SelectionBuilder buildUpdate(Uri uri){

        final SelectionBuilder mSelectionBuidler = new SelectionBuilder();

        int match = matcher.match(uri);

        switch (match){

            case MOVIE_ID:{

                Long _id = ContentUris.parseId(uri);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.MOVIE)
                        .where("id" + "=?", Long.toString(_id));
            }

            case CAST_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.CAST)
                        .where("movie_id" + "=?", movieId);
            }

            case TRAILER_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuidler.table(MoviesDbHelper.TABLES.TRAILER)
                        .where("movie_id"+"=?",movieId);
            }

            default:{

                throw new UnsupportedOperationException("Invalid Uri");
            }


        }
    }

    private SelectionBuilder buildDeletion(Uri uri){

        final SelectionBuilder mSelectionBuilder = new SelectionBuilder();

        int match = matcher.match(uri);

        switch (match){

            case MOVIE_ID:{

                Log.i("MovieProvider", "Inside deletion from user ID");
                Long _id = ContentUris.parseId(uri);
                return mSelectionBuilder.table(MoviesDbHelper.TABLES.MOVIE)
                        .where("_ID"+"=?",Long.toString(_id));
            }

            case CAST_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuilder.table(MoviesDbHelper.TABLES.CAST)
                        .where("movie_id" + "=?", movieId);
            }

            case TRAILER_MOVIE_ID:{

                String movieId = uri.getPathSegments().get(2);

                return mSelectionBuilder.table(MoviesDbHelper.TABLES.TRAILER)
                        .where("movie_id"+"=?",movieId);
            }


            default:{
                throw new UnsupportedOperationException("Invalid Uri");
            }
        }


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();

        final SelectionBuilder mDeleteBuilder= buildDeletion(uri);

        int deletedRows = mDeleteBuilder.delete(db);

        getContext().getContentResolver().notifyChange(MovieContract.Movie.CONTENT_URI, null);

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();

        final SelectionBuilder mUpdateBuilder= buildUpdate(uri);

        int updatedRows = mUpdateBuilder.update(db,values);

        return updatedRows;
    }
}
