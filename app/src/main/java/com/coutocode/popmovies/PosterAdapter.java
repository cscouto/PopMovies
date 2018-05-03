package com.coutocode.popmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.coutocode.popmovies.Constants.IMAGE_BASE_URL;
import static com.coutocode.popmovies.Constants.IMAGE_SIZE_500;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder>{

    ArrayList<Movie> movies;

    PosterAdapter(ArrayList<Movie> movies){
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewPoster)
        ImageView imageViewPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Movie movie){
            String path = IMAGE_BASE_URL + IMAGE_SIZE_500 + movie.poster_path;
            Picasso.with(itemView.getContext())
                    .load(path)
                    .into(imageViewPoster);
        }
    }
}
