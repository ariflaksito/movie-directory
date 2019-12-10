package net.ariflaksito.moviedirectory.Widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import net.ariflaksito.moviedirectory.Databases.MovieHelper;
import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.Provider.ImageWidget;
import net.ariflaksito.moviedirectory.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<Movie> listMovie;
    Context mContext;
    MovieHelper movieHelper;
    static String TAG = "net.ariflaksito.moviedirectory";

    public StackViewsFactory(Context applicationContext, Intent intent) {
        this.mContext = applicationContext;
        movieHelper = new MovieHelper(mContext);
    }

    @Override
    public void onCreate() {
        movieHelper.open();
        listMovie = movieHelper.query();
        movieHelper.close();
    }

    @Override
    public void onDataSetChanged() {
        
        movieHelper.open();
        listMovie = movieHelper.query();
        movieHelper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listMovie.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Movie movie = listMovie.get(i);
        Bitmap bmp = null;
        try {

            bmp = Glide.with(mContext)
                    .load("http://image.tmdb.org/t/p/w300/" + movie.getMovieImage())
                    .asBitmap()
                    .error(new ColorDrawable(mContext.getResources().getColor(R.color.black_overlay)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        rv.setImageViewBitmap(R.id.imageView, bmp);
        rv.setTextViewText(R.id.textDate, movie.getMovieDate());

        Bundle extras = new Bundle();
        extras.putInt(ImageWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
