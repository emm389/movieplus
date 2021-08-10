package com.emmanuel.movieplus.di.modules;

import android.app.Application;

import com.emmanuel.movieplus.database.MovieDatabase;
import com.emmanuel.movieplus.database.daos.MovieDAO;
import com.emmanuel.movieplus.database.daos.MovieDetailDAO;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Emmanuel on 8/7/2021.
 */

@Module
public class RoomModule {

    private final MovieDatabase database;

    public RoomModule(Application application) {
        database = Room.databaseBuilder(application, MovieDatabase.class, "movie_database").build();
    }

    @Singleton
    @Provides
    public MovieDatabase getInstance() {
        return database;
    }

    @Singleton
    @Provides
    public MovieDAO providesMovieDAO(MovieDatabase database) {
        return database.movieDAO();
    }

    @Singleton
    @Provides
    public MovieDetailDAO providesMovieDetailDAO(MovieDatabase database) {
        return database.moviedetailDAO();
    }
}
