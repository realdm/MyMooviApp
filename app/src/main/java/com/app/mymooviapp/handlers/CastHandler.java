package com.app.mymooviapp.handlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.app.mymooviapp.data.MovieContract;
import com.app.mymooviapp.models.CastMember;

import java.util.HashMap;

/**
 * Created by Mac on 7/9/2015.
 */
public class CastHandler {

    private Context mContext;

    public CastHandler(Context context){

        this.mContext = context;
    }


    //insert or update cast
    public void insertOrUpdateCast(CastMember castMember,int movieId){

        HashMap<String, String> castHashKeys = loadCastHashes();

        ContentValues castValues = buildContentValues(castMember,movieId);

        if(castHashKeys.get(castMember.getCharacter())!=null){

            if(castHashKeys.containsKey(castMember.getCharacter()) && !castHashKeys.get(castMember.getCharacter()).equals(castMember.getImportedHashCode())){

               //update cast
                Log.i("CastHandler", "Cast should be updated");
            }
        }
        else{

            Uri returnUri= mContext.getContentResolver().insert(MovieContract.Cast.CONTENT_URI,castValues);

            Log.i("CastHandler", "Cast inserted and id is: "+ ContentUris.parseId(returnUri));
        }
    }


    //load all cast keys into hashmap

    public HashMap<String, String> loadCastHashes()
    {
        HashMap<String, String> castHashes = new HashMap<>();

        String[] columns = {
                MovieContract.Cast._ID,
                MovieContract.Cast.CHARACTER_NAME,
                MovieContract.Cast.IMPORT_HASH_KEY
        };

        Uri uri = MovieContract.Cast.CONTENT_URI;

        Cursor cursor = mContext.getContentResolver().query(uri,columns,null,null,null,null);

        if(cursor != null){
            while(cursor.moveToNext()){

                String characterName = cursor.getString(cursor.getColumnIndex(columns[1]));

                String importHashKey = cursor.getString(cursor.getColumnIndex(columns[2]));

                castHashes.put(characterName,importHashKey);
            }
        }

        return castHashes;
    }

    public ContentValues buildContentValues(CastMember castMember,int movieId){
        ContentValues castValues = new ContentValues();
        castValues.put(MovieContract.Cast.CHARACTER_NAME,castMember.getCharacter());
        castValues.put(MovieContract.Cast.ACTOR_NAME,castMember.getActorName());
        castValues.put(MovieContract.Cast.PROFILE_URL,castMember.getProfile_photo());
        castValues.put(MovieContract.Cast.MOVIE_ID,movieId);
        castValues.put(MovieContract.Cast.IMPORT_HASH_KEY,castMember.getImportedHashCode());

        return castValues;

    }

}
