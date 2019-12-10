package net.ariflaksito.moviedirectory.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ariflaksito.moviedirectory.Adapters.CardMovieAdapter;
import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.Loaders.MyAsyncTaskLoader;
import net.ariflaksito.moviedirectory.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static int LOADER_ID = 1;
    private ProgressDialog dialog;
    RecyclerView rvCategory;
    CardMovieAdapter cardView;
    ArrayList<Movie> listMovie = new ArrayList<>();
    static String DATA_ID = "net.ariflaksito.DATAID";

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);

        dialog = new ProgressDialog(getActivity());

        cardView = new CardMovieAdapter(getActivity());
        cardView.notifyDataSetChanged();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);
        rvCategory = (RecyclerView)rootView.findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        getActivity().setTitle(getResources().getString(R.string.upcoming));

        if (savedInstanceState != null){
            listMovie = savedInstanceState.getParcelableArrayList(DATA_ID);
            cardView.setListMovie(listMovie);

            rvCategory.setAdapter(cardView);
            rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();


        return rootView;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        dialog.setMessage(getResources().getString(R.string.loading_text));
        dialog.show();

        return new MyAsyncTaskLoader(getContext(), "", MyAsyncTaskLoader.TYPE_COMING);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        dialog.dismiss();
        listMovie = movies;
        cardView.setListMovie(listMovie);

        rvCategory.setAdapter(cardView);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        rvCategory.setAdapter(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSupportLoaderManager().destroyLoader(LOADER_ID);

    }

    @Override
    public void onResume() {
        super.onResume();
        cardView.setListMovie(listMovie);

        rvCategory.setAdapter(cardView);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(DATA_ID, listMovie);

    }
}
