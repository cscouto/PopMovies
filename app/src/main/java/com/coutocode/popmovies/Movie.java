package com.coutocode.popmovies;

import java.util.List;

public class Movie {
    int id;
    boolean video;
    float vote_average;
    String title;
    double popularity;
    String poster_path;
    String original_language;
    String original_title;
    List<Integer> genre_ids;
    String backdrop_path;
    boolean adult;
    String overview;
    String release_date;

    Movie(int id, boolean video, float vote_average, String title, double popularity,
          String poster_path, String original_language,  String original_title,
          List<Integer> genre_ids, String backdrop_path, boolean adult, String overview,
          String release_date){
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
    }
}