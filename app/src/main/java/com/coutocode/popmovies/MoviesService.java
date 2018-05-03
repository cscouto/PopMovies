package com.coutocode.popmovies;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesService {

    MoviesdbAPI api;

    MoviesService(){
       Retrofit retrofit = new Retrofit
               .Builder()
               .baseUrl(Constants.BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       api = retrofit.create(MoviesdbAPI.class);
    }

    Call<MovieResponse> getMostPopular() {
        return api.getMostPopularMovies(Constants.API_KEY);
    }

    Call<MovieResponse> getMostHighestRatedMovies() {
        return api.getMostHighestRatedMovies(Constants.API_KEY);
    }
}
