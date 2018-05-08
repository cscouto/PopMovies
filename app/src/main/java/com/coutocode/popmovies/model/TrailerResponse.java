package com.coutocode.popmovies.model;

import com.coutocode.popmovies.model.Trailer;

import java.util.List;

public class TrailerResponse {
    private final int id;
    public final List<Trailer> results;


    TrailerResponse(int id, List<Trailer> results){
        this.id = id;
        this.results = results;
    }
}


