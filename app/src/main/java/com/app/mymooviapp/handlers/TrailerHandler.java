package com.app.mymooviapp.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.models.Trailer;

import java.util.HashMap;

/**
 * Created by Mac on 7/9/2015.
 */
public class TrailerHandler {

    private Context mContext;

    public TrailerHandler(Context context){
        this.mContext = context;
    }

    public void insertOrUpdateTrailer(Trailer trailer, int movieId){

        HashMap<String, String> trailerHashes = loadTrailerHashes();

        ContentValues values = generateContentValues(trailer,movieId);

        if(trailerHashes.get(trailer.getSource())!=null){

            if(trailerHashes.containsKey(trailer.getSource()) && !trailerHashes.get(trailer.getSource()).equals(trailer.getImportedHashCode())){

                //update trailer
                Log.i("TrailerHandler", "trailer should be updated");
            }
        }
        else{

            mContext.getContentResolver().insert(MovieContract.Trailer.CONTENT_URI,values);
            Log.i("TrailerHandler", "TrailerInserted");
        }

    }

    //HELPER METHODS

    //generate content values
    public ContentValues generateContentValues(Trailer trailer, int movieId){
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieContract.Trailer.NAME,trailer.getName());
        trailerValues.put(MovieContract.Trailer.YOUTUBE_ID,trailer.getSource());
        trailerValues.put(MovieContract.Trailer.MOVIE_ID,movieId);
        trailerValues.put(MovieContract.Trailer.IMPORT_HASH_KEY,trailer.getImportedHashCode());

        return trailerValues;
    }

    //load trailer import hash keys
    public HashMap<String, String> loadTrailerHashes()
    {
        HashMap<String, String> trailerHashes = new HashMap<>();

        String[] columns = {
                MovieContract.Trailer._ID,
                MovieContract.Trailer.YOUTUBE_ID,
                MovieContract.Trailer.IMPORT_HASH_KEY
        };

        Uri uri = MovieContract.Trailer.CONTENT_URI;

        Cursor cursor = mContext.getContentResolver().query(uri,columns,null,null,null,null);

        if(cursor != null){
            while(cursor.moveToNext()){

                String youtubeID = cursor.getString(cursor.getColumnIndex(columns[1]));

                String importHashKey = cursor.getString(cursor.getColumnIndex(columns[2]));

                trailerHashes.put(youtubeID,importHashKey);
            }
        }

        return trailerHashes;
    }

}

