package net.ariflaksito.moviedirectory.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.ariflaksito.moviedirectory.Adapters.CardMovieAdapter;
import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.Loaders.MyAsyncTaskLoader;
import net.ariflaksito.moviedirectory.R;

import java.util.ArrayList;

import cz.msebera.android.httpclient.util.TextUtils;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static int LOADER_ID = 1;

    public static final String SEARCH_KEY = "extra_key";
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String SEARCH_KEYWORD = "extra_keyword";
    private ProgressDialog dialog;
    RecyclerView rvCategory;
    ArrayList<Movie> listMovie;
    CardMovieAdapter cardView;
    private Button buttonCari;
    private EditText editKeyword;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seacrh);

        setTitle(getResources().getString(R.string.search));

        dialog = new ProgressDialog(this);
        listMovie = new ArrayList<>();

        cardView = new CardMovieAdapter(this);
        cardView.setListMovie(listMovie);

        rvCategory = (RecyclerView)findViewById(R.id.rv_category);
        rvCategory.setHasFixedSize(true);

        rvCategory.setAdapter(cardView);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        String key = "";
        if(extras!=null)
            key = extras.getString(SearchActivity.SEARCH_KEY);

        editKeyword = findViewById(R.id.edit_key);
        editKeyword.setText(key);
        keyword = editKeyword.getText().toString();

        buttonCari = findViewById(R.id.btn_cari);
        buttonCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String k = editKeyword.getText().toString();
                if (TextUtils.isEmpty(k))return;

                Bundle bundle = new Bundle();
                bundle.putString(SEARCH_KEYWORD, k);
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, SearchActivity.this).forceLoad();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_KEYWORD, key);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this).forceLoad();

    }


    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        dialog.setMessage(getResources().getString(R.string.loading_text));
        dialog.show();

        String text = "";
        if (bundle != null){
            text = bundle.getString(SEARCH_KEYWORD);
        }
        return new MyAsyncTaskLoader(this, text, MyAsyncTaskLoader.TYPE_SEARCH);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        dialog.dismiss();
        cardView.setListMovie(movies);

        rvCategory.setAdapter(cardView);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }

}
