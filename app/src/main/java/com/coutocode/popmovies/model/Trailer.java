package com.coutocode.popmovies.model;

public class Trailer {
    private final String id;
    public final String name;
    private final String site;

    Trailer(String id, String name, String site){
        this.id = id;
        this.name = name;
        this.site = site;
    }
}
