package net.ariflaksito.moviedirectory.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.ariflaksito.moviedirectory.Activities.DetailActivity;
import net.ariflaksito.moviedirectory.Activities.SearchActivity;
import net.ariflaksito.moviedirectory.Entities.Movie;
import net.ariflaksito.moviedirectory.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.CONTENT_URI;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DATE;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.DESCRIPTION;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.PHOTO;
import static net.ariflaksito.moviedirectory.Databases.DatabaseContract.NoteColumns.TITLE;

public class CardMovieAdapter extends RecyclerView.Adapter<CardMovieAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Movie> listMovie;

    public CardMovieAdapter(Context cn){
        context = cn;
    }

    private ArrayList<Movie> getListMovie(){
        return listMovie;
    }

    public void setListMovie(ArrayList<Movie> lsMovie){
        listMovie = lsMovie;
    }

    @NonNull
    @Override
    public CardMovieAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardMovieAdapter.CardViewHolder holder, int position) {
        Movie movie = getListMovie().get(position);

        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w300/" + movie.getMovieImage())
                .override(350, 550)
                .into(holder.imgPhoto);

        holder.tvTitle.setText(movie.getMovieTitle());
        holder.tvInfo.setText(movie.getMovieDesc());

        holder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {

            @Override
            public void onItemClicked(View view, int position) {
                Movie m = listMovie.get(position);

                Intent in = new Intent(context, DetailActivity.class);
                in.putExtra(SearchActivity.EXTRA_MOVIE, m);
                context.startActivity(in);
            }
        }));



        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Movie m = listMovie.get(position);

                Cursor cur = context.getContentResolver().query(CONTENT_URI,
                        null,PHOTO+"=?",new String[]{m.getMovieImage()}, null);
                String info;


                    ContentValues val = new ContentValues();
                    val.put(TITLE, m.getMovieTitle());
                    val.put(DESCRIPTION, m.getMovieDesc());
                    val.put(PHOTO, m.getMovieImage());
                    val.put(DATE, m.getMovieDate());

                    context.getContentResolver().insert(CONTENT_URI, val);
                    info = context.getResources().getString(R.string.toast_add_fav);


                Toast.makeText(context, info,
                        Toast.LENGTH_LONG).show();

            }
        }));
    }

    @Override
    public int getItemCount() {
        if(listMovie==null) return 0;
        else return listMovie.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView tvTitle, tvInfo;
        Button btnDetail, btnShare;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = (ImageView)itemView.findViewById(R.id.img_item_photo);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_item_title);
            tvInfo = (TextView)itemView.findViewById(R.id.tv_item_info);
            btnDetail = (Button)itemView.findViewById(R.id.btn_set_detail);
            btnShare = (Button)itemView.findViewById(R.id.btn_set_share);
        }
    }

}
