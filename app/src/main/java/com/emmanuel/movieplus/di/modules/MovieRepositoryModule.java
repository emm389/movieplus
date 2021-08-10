package com.emmanuel.movieplus.di.modules;

import android.app.Application;

import com.emmanuel.movieplus.repositories.MovieRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Emmanuel on 8/8/2021.
 */

@Module
public class MovieRepositoryModule {

    private final Application application;
    public MovieRepositoryModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public MovieRepository providesMovieRepository() {
        return new MovieRepository(application);
    }
}
