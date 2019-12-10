package net.ariflaksito.moviedirectory.Fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.ariflaksito.moviedirectory.Adapters.CardMovieAdapter;
import net.ariflaksito.moviedirectory.Databases.MovieHelper;
import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.R;

import java.util.ArrayList;

import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private static int LOADER_ID = 1;
    MovieHelper movieHelper;
    Context context;
    RecyclerView rvCategory;
    CardMovieAdapter cardView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        this.context = context;
        movieHelper = new MovieHelper(context);
        movieHelper.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        rvCategory = (RecyclerView)rootView.findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        getActivity().setTitle(getResources().getString(R.string.favorite));
        new AsyncTask<String, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(String... strings) {
                return context.getContentResolver().query(CONTENT_URI,null,null,null,null);
            }

            @Override
            protected void onPostExecute(Cursor cur){
                super.onPostExecute(cur);

                ArrayList<Movie> list = new ArrayList<>();
                if(cur.getCount()>0){
                    while (cur.moveToNext()){
                        Movie m = new Movie(cur);
                        list.add(m);
                    }

                    cardView = new CardMovieAdapter(getActivity());
                    rvCategory.setAdapter(cardView);
                    rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

                    cardView.setListMovie(list);
                    cardView.notifyDataSetChanged();
                }else {
                    Toast.makeText(context, context.getResources().getString(R.string.toast_empty),
                            Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

        return rootView;
    }

    @Override
    public void onDetach(){
        super.onDetach();

        if (movieHelper != null){
            movieHelper.close();
        }
    }
}
