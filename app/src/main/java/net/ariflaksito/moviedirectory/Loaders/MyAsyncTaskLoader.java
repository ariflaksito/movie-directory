package net.ariflaksito.moviedirectory.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import net.ariflaksito.moviedirectory.BuildConfig;
import net.ariflaksito.moviedirectory.Entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ariflaksito on 06/07/18.
 */

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private String searchMovie, typeMovie;
    public static final String API_KEY = BuildConfig.TheMovieDBApi;
    public static final String TYPE_NOW = "now_type";
    public static final String TYPE_COMING = "coming_type";
    public static final String TYPE_SEARCH = "search_type";


    public MyAsyncTaskLoader(Context context, String keyword, String type) {
        super(context);

        onContentChanged();
        typeMovie = type;
        searchMovie = keyword.replaceAll(" ", "+").toLowerCase();

    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        String url = null;

        if(typeMovie==TYPE_NOW)
            url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+ API_KEY +
                    "&language=en-US";
        else if(typeMovie==TYPE_COMING)
            url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ API_KEY +
                    "&language=en-US";
        else if(typeMovie==TYPE_SEARCH)
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY +
                "&language=en-US&query=" + searchMovie;

        Log.d("URL", url);

        final ArrayList<Movie> listMovie = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);
                try {
                    JSONObject objResponse = new JSONObject(result);
                    JSONArray list = objResponse.getJSONArray("results");

                    for(int i=0; i<list.length(); i++){
                        JSONObject objMovie = list.getJSONObject(i);
                        Movie movieItems = new Movie(objMovie);

                        listMovie.add(movieItems);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });

        return listMovie;
    }

}
