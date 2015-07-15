package com.app.mymooviapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.mymooviapp.utils.HashUtils;

import java.io.Serializable;

/**
 * Created by Mac on 6/19/2015.
 */
public class Trailer implements Parcelable {

    private String name;

    private String source;

    public Trailer(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getImportedHashCode()
    {
        StringBuilder builder= new StringBuilder();

        builder.append("name").append(name == null  ? "" : name)
                .append("source").append(source == null ? "" : source);

        return HashUtils.computeWeakHash(builder.toString());
    }

    /**
     * GETTERS AND SETTERS
     *
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * PARCELABLE METHODS
     */

    public Trailer(Parcel in ){

        name = in.readString();

        source = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);

        parcel.writeString(source);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {

        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
