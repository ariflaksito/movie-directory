package net.ariflaksito.moviedirectory.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.ariflaksito.moviedirectory.Entities.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DATE;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DESCRIPTION;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.PHOTO;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.TITLE;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.TABLE_NOTE;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_NOTE;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context cn){
        this.context = cn;
    }

    public MovieHelper open() throws SQLException{
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<Movie> query(){
        ArrayList<Movie> arrayList = new ArrayList<Movie>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID +" DESC",null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount()>0) {
            do {

                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setMovieTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setMovieDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                movie.setMovieDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                movie.setMovieImage(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(TITLE, movie.getMovieTitle());
        initialValues.put(DESCRIPTION, movie.getMovieDesc());
        initialValues.put(DATE, movie.getMovieDate());
        initialValues.put(PHOTO, movie.getMovieImage());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }
    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }
    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }
    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }
    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }

}
