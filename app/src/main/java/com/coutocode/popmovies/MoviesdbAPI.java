package com.coutocode.popmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesdbAPI {

    @GET("/movie/popular")
    Call<MovieResponse> getMostPopularMovies(@Query("api_key") String key);

    @GET("/movie/popular")
    Call<MovieResponse> getMostHighestRatedMovies(@Query("api_key") String key);

}
