package com.coutocode.popmovies.model;

import java.util.List;

public class MovieResponse {
    private final String page;
    private final String total_results;
    private final String total_pages;
    public final List<Movie> results;

    MovieResponse(String page, String total_results, String total_pages, List<Movie> results){
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }
}
