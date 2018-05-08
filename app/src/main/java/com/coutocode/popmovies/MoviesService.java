package com.coutocode.popmovies;

import com.coutocode.popmovies.model.MovieResponse;
import com.coutocode.popmovies.model.ReviewResponse;
import com.coutocode.popmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MoviesService {

    private final MoviesdbAPI api;

    MoviesService(){
       Retrofit retrofit = new Retrofit
               .Builder()
               .baseUrl(Constants.BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       api = retrofit.create(MoviesdbAPI.class);
    }

    Call<MovieResponse> listMostPopular() {
        return api.listMostPopularMovies(BuildConfig.MOVIE_DB_API_KEY);
    }

    Call<MovieResponse> listMostHighestRatedMovies() {
        return api.listMostHighestRatedMovies(BuildConfig.MOVIE_DB_API_KEY);
    }

    Call<TrailerResponse> listTrailers(String movieId){
        return api.listTrailers(movieId, BuildConfig.MOVIE_DB_API_KEY);
    }

    Call<ReviewResponse> listReviews(String movieId){
        return  api.listReviews(movieId, BuildConfig.MOVIE_DB_API_KEY);
    }
}
