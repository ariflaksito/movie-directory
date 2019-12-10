package net.ariflaksito.moviedirectory.Entities;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DATE;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DESCRIPTION;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.PHOTO;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.TITLE;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.getColumnInt;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.getColumnString;

/**
 * Created by ariflaksito on 06/07/18.
 */

public class Movie implements Parcelable {
    private int id;
    private String movieTitle;
    private String movieDesc;
    private String movieDate;
    private String movieImage;

    public Movie(){}

    public Movie(JSONObject jsonObj){
        try {
            setMovieTitle(jsonObj.getString("title"));
            setMovieDesc(jsonObj.getString("overview"));
            setMovieDate(jsonObj.getString("release_date"));
            setMovieImage(jsonObj.getString("backdrop_path"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Movie(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.movieTitle = getColumnString(cursor, TITLE);
        this.movieDesc = getColumnString(cursor, DESCRIPTION);
        this.movieDate = getColumnString(cursor, DATE);
        this.movieImage = getColumnString(cursor, PHOTO);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getMovieTitle());
        dest.writeString(this.getMovieDesc());
        dest.writeString(this.getMovieDate());
        dest.writeString(this.getMovieImage());
    }

    protected Movie(Parcel in) {
        this.setMovieTitle(in.readString());
        this.setMovieDesc(in.readString());
        this.setMovieDate(in.readString());
        this.setMovieImage(in.readString());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", movieTitle='" + movieTitle + '\'' +
                ", movieDesc='" + movieDesc + '\'' +
                ", movieDate='" + movieDate + '\'' +
                ", movieImage='" + movieImage + '\'' +
                '}';
    }
}

