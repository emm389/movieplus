package com.emmanuel.movieplus.network;

import com.emmanuel.movieplus.movies.fragment.MoviesViewModel;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Emmanuel on 8/5/2021.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {
    void inject(MoviesViewModel moviesViewModel);
}
