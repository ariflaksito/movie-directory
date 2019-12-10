package net.ariflaksito.moviedirectory.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = getIntent().getParcelableExtra(SearchActivity.EXTRA_MOVIE);

        TextView title = findViewById(R.id.tx_title);
        title.setText(movie.getMovieTitle());

        TextView date = findViewById(R.id.tx_date);
        date.setText(getResources().getString(R.string.release_text)+": "+movie.getMovieDate());

        TextView desc = findViewById(R.id.tx_desc);
        desc.setText(movie.getMovieDesc());

        ImageView img = findViewById(R.id.tx_img);
        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w500/"+movie.getMovieImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(img);

    }
}
