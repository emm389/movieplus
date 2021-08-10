package com.emmanuel.movieplus.di;

import com.emmanuel.movieplus.di.modules.ApplicationModule;
import com.emmanuel.movieplus.di.modules.MovieRepositoryModule;
import com.emmanuel.movieplus.di.modules.RoomModule;
import com.emmanuel.movieplus.moviedetail.MovieDetailViewModel;
import com.emmanuel.movieplus.moviedetail.pagination.MovieDataSource;
import com.emmanuel.movieplus.movies.MoviesActivity;
import com.emmanuel.movieplus.movies.fragment.MoviesViewModel;
import com.emmanuel.movieplus.di.modules.NetworkModule;
import com.emmanuel.movieplus.repositories.MovieRepository;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Emmanuel on 8/5/2021.
 */

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, RoomModule.class, MovieRepositoryModule.class})
public interface AppComponent {
    void inject(MoviesActivity activity);
    void inject(MoviesViewModel moviesViewModel);
    void inject(MovieDetailViewModel moviesDetailViewModel);
    void inject(MovieDataSource movieDataSource);
    void inject(MovieRepository movieRepository);
}
