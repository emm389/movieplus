package com.emmanuel.movieplus.moviedetail;

import com.emmanuel.movieplus.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Emmanuel on 8/5/2021.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface MovieDetailComponent {

    void inject(MovieDetailViewModel movieDetailViewModel);
}
