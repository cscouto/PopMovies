package com.coutocode.popmovies;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.coutocode.popmovies.apdater.PosterAdapter;
import com.coutocode.popmovies.data.MovieContract;
import com.coutocode.popmovies.model.Movie;
import com.coutocode.popmovies.model.MovieResponse;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PosterAdapter.ItemClick {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private MoviesService moviesService;
    private final String KEY_STATE = "state";

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_most_popular:
                    progressBar.setVisibility(View.VISIBLE);
                    callPopularMovies();
                    return true;
                case  R.id.action_highest_rated:
                    progressBar.setVisibility(View.VISIBLE);
                    callHighestRatedMovies();
                    return true;
                case  R.id.action_favorites:
                    progressBar.setVisibility(View.VISIBLE);
                    List<Movie> movies = loadFavoriteMovies();
                    updateUI(movies);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        moviesService = new MoviesService();

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);

        mRecyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);

        if (savedInstanceState != null) {
            int state = savedInstanceState.getInt(KEY_STATE);
            navigation.setSelectedItemId(state);
        }else{
            callPopularMovies();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_STATE, navigation.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    private void updateUI(List<Movie> movies){
        progressBar.setVisibility(View.GONE);
        PosterAdapter adapter = new PosterAdapter(movies);
        adapter.delegate = this;
        mRecyclerView.setAdapter(adapter);
    }

    private void callPopularMovies(){
        Call call = moviesService.listMostPopular();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    updateUI(response.body().results);
                }else{
                    Toast.makeText(MainActivity.this,
                            response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        R.string.request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callHighestRatedMovies(){
        Call call = moviesService.listMostHighestRatedMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    updateUI(response.body().results);
                }else{
                    Toast.makeText(MainActivity.this,
                            response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        R.string.request_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<Movie> loadFavoriteMovies(){
        ArrayList<Movie> movies = new ArrayList();
        Uri uriMovies = Uri.parse(String.valueOf(MovieContract.MovieEntry.CONTENT_URI));
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uriMovies,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
                String original_title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
                String poster_path = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                movies.add(new Movie(id, original_title, poster_path));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movies;
    }

    @Override
    public void clickedItem(Movie movie) {
        Intent intentToDetail = new Intent(this, DetailActivity.class);

        boolean isFavorite = false;

        List<Movie> movies = loadFavoriteMovies();

        for(Movie m: movies) {
            if (m.id == movie.id) {
                isFavorite = true;
                break;
            }
        }
        intentToDetail.putExtra(Constants.FAVORITE_EXTRA_KEY, isFavorite);
        intentToDetail.putExtra(Constants.MOVIE_ID_EXTRA_KEY, movie.id);
        startActivity(intentToDetail);
    }
}
