package com.coutocode.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coutocode.popmovies.apdater.ReviewAdapter;
import com.coutocode.popmovies.apdater.TrailerAdapter;
import com.coutocode.popmovies.data.MovieContract;
import com.coutocode.popmovies.model.Movie;
import com.coutocode.popmovies.model.Review;
import com.coutocode.popmovies.model.ReviewResponse;
import com.coutocode.popmovies.model.Trailer;
import com.coutocode.popmovies.model.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.coutocode.popmovies.Constants.IMAGE_BASE_URL;
import static com.coutocode.popmovies.Constants.IMAGE_SIZE_500;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClick {

    @BindView(R.id.imageViewPoster)
    ImageView imageViewPoster;
    @BindView(R.id.imageViewFavorite)
    ImageView imageViewFavorite;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewRate)
    TextView textViewRate;
    @BindView(R.id.textViewRelease)
    TextView textViewRelease;
    @BindView(R.id.textViewSynopsis)
    TextView textViewSynopsis;
    @BindView(R.id.recyclerViewTrailer)
    RecyclerView recyclerViewTrailer;
    @BindView(R.id.recyclerViewReview)
    RecyclerView recyclerViewReview;

    private MoviesService moviesService;
    private boolean isFavorite = false;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        int movieId = getIntent().getExtras().getInt(Constants.MOVIE_ID_EXTRA_KEY);

        isFavorite = getIntent().getExtras().getBoolean(Constants.FAVORITE_EXTRA_KEY);

        moviesService = new MoviesService();

        callMovie(String.valueOf(movieId));


        if (isFavorite){
            imageViewFavorite.setImageDrawable(getResources()
                    .getDrawable(R.drawable.like_filled, getTheme()));
        }else{
            imageViewFavorite.setImageDrawable(getResources()
                    .getDrawable(R.drawable.like, getTheme()));
        }

        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this);
        recyclerViewReview.setLayoutManager(layoutManagerReview);
        LinearLayoutManager layoutManagerTrailer = new LinearLayoutManager(this);
        recyclerViewTrailer.setLayoutManager(layoutManagerTrailer);

        moviesService = new MoviesService();

        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                if (isFavorite){
                    imageViewFavorite.setImageDrawable(getResources()
                            .getDrawable(R.drawable.like_filled, getTheme()));
                    addMovie();
                }else{
                    imageViewFavorite.setImageDrawable(getResources()
                            .getDrawable(R.drawable.like, getTheme()));
                    deleteVideo();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteVideo(){
        if (movie != null) {
            getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_ID + "=?", new String[]{String.valueOf(movie.id)});
        }
    }

    private void addMovie(){
        if (movie != null) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_ID, movie.id);
            values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.original_title);
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.poster_path);

            getContentResolver().bulkInsert(
                    MovieContract.MovieEntry.CONTENT_URI, new ContentValues[]{values});
        }
    }

    private void updateUI(Movie movie){
        this.movie = movie;
        callReviews(String.valueOf(movie.id));
        callTrailers(String.valueOf(movie.id));

        String path = IMAGE_BASE_URL + IMAGE_SIZE_500 + movie.poster_path;
        Picasso.with(this)
                .load(path)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(imageViewPoster);
        textViewTitle.setText(movie.title);
        textViewRate.setText(String.valueOf(movie.vote_average) + "/10");
        textViewRelease.setText(movie.release_date);
        textViewSynopsis.setText(movie.overview);
    }

    private void callMovie(String movieId){
        Call<Movie> call = moviesService.getMovie(movieId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()){
                    updateUI(response.body());
                }else{
                    Toast.makeText(DetailActivity.this,
                            response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(DetailActivity.this,
                        R.string.request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callReviews(String movieId){
       Call<ReviewResponse> call =  moviesService.listReviews(movieId);
       call.enqueue(new Callback<ReviewResponse>() {
           @Override
           public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
               if (response.isSuccessful()){
                   updateReviews(response.body().results);
               }else{
                   Toast.makeText(DetailActivity.this,
                           response.message(), Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<ReviewResponse> call, Throwable t) {
               Toast.makeText(DetailActivity.this,
                       R.string.request_failed, Toast.LENGTH_LONG).show();
           }
       });
    }

    private void callTrailers(String movieId){
        Call<TrailerResponse> call = moviesService.listTrailers(movieId);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.isSuccessful()){
                    updateTrailers(response.body().results);
                }else{
                    Toast.makeText(DetailActivity.this,
                            response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this,
                        R.string.request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateReviews(List<Review> reviews){
        ReviewAdapter adapter = new ReviewAdapter(reviews);
        recyclerViewReview.setAdapter(adapter);
    }

    private void updateTrailers(List<Trailer> trailers){
        TrailerAdapter adapter = new TrailerAdapter(trailers);
        adapter.delegate = this;
        recyclerViewTrailer.setAdapter(adapter);
    }

    @Override
    public void clickedItem(Trailer trailer) {
        Uri uriYoutube =  Uri.parse(Constants.YOUTUBE_BASE_URL)
                .buildUpon()
                .appendQueryParameter(Constants.YOUTUBE_QUERY_PARAM, trailer.key)
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, uriYoutube));
    }
}
