package com.coutocode.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.coutocode.popmovies.Constants.IMAGE_BASE_URL;
import static com.coutocode.popmovies.Constants.IMAGE_SIZE_500;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imageViewPoster)
    ImageView imageViewPoster;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewRate)
    TextView textViewRate;
    @BindView(R.id.textViewRelease)
    TextView textViewRelease;
    @BindView(R.id.textViewSynopsis)
    TextView textViewSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        Movie movie = getIntent().getExtras().getParcelable(Constants.DETAIL_EXTRA_KEY);

        String path = IMAGE_BASE_URL + IMAGE_SIZE_500 + movie.poster_path;
        Picasso.with(this)
                .load(path)
                .into(imageViewPoster);
        textViewTitle.setText(movie.title);
        textViewRate.setText(String.valueOf(movie.vote_average));
        textViewRelease.setText(movie.release_date);
        textViewSynopsis.setText(movie.overview);
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
}
