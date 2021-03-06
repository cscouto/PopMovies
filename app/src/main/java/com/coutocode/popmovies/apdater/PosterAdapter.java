package com.coutocode.popmovies.apdater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coutocode.popmovies.R;
import com.coutocode.popmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.coutocode.popmovies.Constants.IMAGE_BASE_URL;
import static com.coutocode.popmovies.Constants.IMAGE_SIZE_500;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder>{

    public interface ItemClick {
        void clickedItem(Movie movie);
    }

    private final List<Movie> movies;
    public ItemClick delegate;

    public PosterAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_item, parent, false);
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(final Movie movie){
            String path = IMAGE_BASE_URL + IMAGE_SIZE_500 + movie.poster_path;
            Picasso.with(itemView.getContext())
                    .load(path)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(imageViewPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delegate != null){
                        delegate.clickedItem(movie);
                    }
                }
            });
        }
    }
}
