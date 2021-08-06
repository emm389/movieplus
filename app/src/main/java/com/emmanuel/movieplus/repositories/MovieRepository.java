package com.emmanuel.movieplus.repositories;

import com.emmanuel.movieplus.datasources.MovieLocalDataSource;
import com.emmanuel.movieplus.datasources.MovieRemoteDataSource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Emmanuel on 8/5/2021.
 */

@Singleton
public class MovieRepository {

    private final MovieLocalDataSource movieLocalDataSource;
    private final MovieRemoteDataSource movieRemoteDataSource;

    @Inject
    public MovieRepository(MovieLocalDataSource movieLocalDataSource, MovieRemoteDataSource movieRemoteDataSource) {
        this.movieLocalDataSource = movieLocalDataSource;
        this.movieRemoteDataSource = movieRemoteDataSource;
    }
}
