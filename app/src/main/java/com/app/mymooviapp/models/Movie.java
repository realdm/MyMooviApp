package com.app.mymooviapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.app.mymooviapp.utils.HashUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/11/2015.
 */
public class Movie implements Parcelable {

    private int id;

    private String  title;

    private String overview;

    private String releaseDate;

    private String posterPath;

    private String backDropPath;

    private String director;

    private float userRating;

    private int runtime;

    private boolean isFavorited;

    private List<String> genres = new ArrayList<String>();

    private List<Trailer> trailers = new ArrayList<Trailer>();

    private List<CastMember> cast = new ArrayList<CastMember>();

    private List<String> reviews = new ArrayList<String>();

    public Movie(int id,String title, String overview, String releaseDate, String posterPath, float userRating, String backDropPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.userRating = userRating;
        this.backDropPath = backDropPath;
        this.isFavorited = false;

    }


    public String getImportedHashCode()
    {
        StringBuilder builder= new StringBuilder();

        builder.append("id").append(id==0  ? "":id)
                .append("director").append(director == null ? "" : director)
                .append("title").append(title == null ? "" : title)
                .append("overview").append(overview == null ? "" : overview)
                .append("releaseDate").append(releaseDate == null ? "" : releaseDate)
                .append("posterPath").append(posterPath == null ? "" : posterPath)
                .append("userrating").append(userRating == 0 ? "" : userRating)
                .append("backdrop").append(backDropPath == null ? "" : backDropPath);


        return HashUtils.computeWeakHash(builder.toString());
    }

    /**
     * GETTERS AND SETTERS
     *
     */
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {

        return releaseDate != null ? releaseDate : "N/A";
    }

    public String getPosterPath() {
        return posterPath;
    }

    public float getUserRating() {
        return userRating;
    }

    public String getBackDropPath()
    {
        return backDropPath;
    }

    public boolean isFavorited(){
        return isFavorited;
    }

    public void addCastMember(CastMember cast)
    {
        this.cast.add(cast);
    }

    public void addTrailers(Trailer trailer)
    {
        this.trailers.add(trailer);
    }

    public void addGenres(String genre)
    {
        this.genres.add(genre);
    }

    public void addReviews(String review)
    {
        this.addReviews(review);
    }

    public void setFavorited(boolean isFavorited){
        this.isFavorited = isFavorited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDirector() {

        String[] directorName=null;

        if(this.director!=null){

            directorName = director.split(" ");
        }
        return director != null ? directorName[0] : "N/A";
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<String> getGenresList()
    {
        return genres;
    }

    public String getGenres(){
        String genre="";
        if(getGenresList().size()>0) {
            for(int i = 0; i< getGenresList().size() ; i++) {
                genre=genre+"  "+getGenresList().get(i);
                if(i==2)break;
            }
        }
        else{
            genre= "N/A";
        }
        return genre;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }
    public List<CastMember> getCast() {
        return cast;
    }
    public List<String> getReviews()
    {
        return reviews;
    }
    /**
     * END GETTERS AND SETTERS
     */

    /**
     * PARCELABLE METHODS
     */

    public Movie(Parcel in) {

        id = in.readInt();
        director = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backDropPath = in.readString();
        director = in.readString();
        userRating = in.readFloat();
        runtime = in.readInt();
        in.readList(genres,List.class.getClassLoader());
        in.readList(trailers,List.class.getClassLoader());
        in.readList(cast,List.class.getClassLoader());
        in.readList(reviews,List.class.getClassLoader());
        isFavorited = in.readByte() != 0;


    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(director);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(backDropPath);
        parcel.writeString(director);
        parcel.writeFloat(userRating);
        parcel.writeInt(runtime);
        parcel.writeList(genres);
        parcel.writeList(trailers);
        parcel.writeList(cast);
        parcel.writeList(reviews);
        parcel.writeByte((byte) (isFavorited ? 1 : 0));

    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
