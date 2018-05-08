package com.coutocode.popmovies.model;

import java.util.List;

public class ReviewResponse {
    private final String id;
    public final List<Review> results;

    ReviewResponse(String id, List<Review> results){
        this.id = id;
        this.results = results;
    }
}
