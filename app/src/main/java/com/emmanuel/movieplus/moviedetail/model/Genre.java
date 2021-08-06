package com.emmanuel.movieplus.moviedetail.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class Genre {

    @Expose
    private int id;
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
