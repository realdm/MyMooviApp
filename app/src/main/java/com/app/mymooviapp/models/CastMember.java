package com.app.mymooviapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.mymooviapp.utils.HashUtils;

import java.io.Serializable;

/**
 * Created by Mac on 6/19/2015.
 */
public class CastMember implements Parcelable {

    private String character;

    private String actorName;

    private String profile_photo;

    public CastMember(String character, String actorName, String profile_photo) {
        this.character = character;
        this.actorName = actorName;
        this.profile_photo = profile_photo;
    }

    public String getImportedHashCode()
    {
        StringBuilder builder= new StringBuilder();

        builder.append("character").append(character==null  ? "":character).
                append("actorName").append(actorName==null?"":actorName)
                .append("profile_photo").append(profile_photo==null?"":profile_photo);



        return HashUtils.computeWeakHash(builder.toString());
    }

    /**
     * GETTERS AND SETTERS
     *
     */

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    /**
     * END GETTERS AND SETTERS
     */

    /**
     * PARCELABLE METHODS
     */

    public CastMember(Parcel in)
    {
        character = in.readString();

        actorName = in.readString();

        profile_photo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(character);

        parcel.writeString(actorName);

        parcel.writeString(profile_photo);
    }

    public static final Parcelable.Creator<CastMember> CREATOR = new Parcelable.Creator<CastMember>() {

        public CastMember createFromParcel(Parcel in) {
            return new CastMember(in);
        }

        public CastMember[] newArray(int size) {
            return new CastMember[size];
        }
    };
}
