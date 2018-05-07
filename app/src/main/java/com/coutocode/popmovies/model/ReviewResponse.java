package com.coutocode.popmovies.model;

import java.util.List;

public class ReviewResponse {
    String id;
    public List<Review> results;

    ReviewResponse(String id, List<Review> results){
        this.id = id;
        this.results = results;
    }
}
