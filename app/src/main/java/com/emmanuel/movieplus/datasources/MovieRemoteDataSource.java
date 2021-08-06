package com.emmanuel.movieplus.datasources;

import com.emmanuel.movieplus.network.ApiService;

import javax.inject.Inject;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MovieRemoteDataSource {

    private final ApiService apiService;

    @Inject
    public MovieRemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }
}
