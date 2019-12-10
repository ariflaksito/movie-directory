package net.ariflaksito.moviedirectory.Loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import net.ariflaksito.moviedirectory.Alarm.AlarmBroadcast;
import net.ariflaksito.moviedirectory.BuildConfig;
import net.ariflaksito.moviedirectory.Entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    ArrayList<Movie> movieData;
    static String TAG = "net.ariflaksito.moviedirectory";
    public AsyncResponse delegate = null;

    public static final String API_KEY = BuildConfig.TheMovieDBApi;

    public interface AsyncResponse {
        void processFinish(ArrayList<Movie> output);
    }

    public MyAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public MyAsyncTask(ArrayList<Movie> movies){
        this.movieData = movies;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... voids) {
        SyncHttpClient client = new SyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key="+ API_KEY +
                    "&language=en-US";

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

    @Override
    protected void onPostExecute(ArrayList<Movie> movies){
        super.onPostExecute(movies);
        delegate.processFinish(movies);

    }

}
