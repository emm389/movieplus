package com.emmanuel.movieplus.moviedetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class ProductionCompany {

    @Expose
    private int id;
    @SerializedName("logo_path")
    @Expose
    private String logoPath;
    @Expose
    private String name;
    @SerializedName("origin_country")
    @Expose
    private String originCountry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
}
