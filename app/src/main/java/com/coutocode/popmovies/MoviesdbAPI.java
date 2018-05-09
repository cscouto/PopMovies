package com.coutocode.popmovies;

import com.coutocode.popmovies.model.Movie;
import com.coutocode.popmovies.model.MovieResponse;
import com.coutocode.popmovies.model.ReviewResponse;
import com.coutocode.popmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MoviesdbAPI {

    @GET("movie/popular")
    Call<MovieResponse> listMostPopularMovies(@Query("api_key") String key);

    @GET("movie/top_rated")
    Call<MovieResponse> listMostHighestRatedMovies(@Query("api_key") String key);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> listTrailers(@Path("id") String movieId, @Query("api_key") String key);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> listReviews(@Path("id") String movieId, @Query("api_key") String key);

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") String movieId, @Query("api_key") String key);

}
