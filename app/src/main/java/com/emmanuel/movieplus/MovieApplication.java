package com.emmanuel.movieplus;

import android.app.Application;

import com.emmanuel.movieplus.moviedetail.DaggerMovieDetailComponent;
import com.emmanuel.movieplus.moviedetail.MovieDetailComponent;
import com.emmanuel.movieplus.network.DaggerNetworkComponent;
import com.emmanuel.movieplus.network.NetworkComponent;
import com.emmanuel.movieplus.network.NetworkModule;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MovieApplication extends Application {

    private NetworkComponent networkComponent;
    private MovieDetailComponent movieDetailComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule())
                .build();

        movieDetailComponent = DaggerMovieDetailComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return this.networkComponent;
    }

    public MovieDetailComponent getMovieDetailComponent() {
        return this.movieDetailComponent;
    }
}
