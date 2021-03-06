package com.emmanuel.movieplus.moviedetail.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class BelongsToCollection {
    
    private int id;
    private String name;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    
    private String backdropPath;

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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}
