package com.emmanuel.movieplus;

import android.app.Application;

import com.emmanuel.movieplus.di.modules.ApplicationModule;
import com.emmanuel.movieplus.di.modules.MovieRepositoryModule;
import com.emmanuel.movieplus.di.modules.RoomModule;
import com.emmanuel.movieplus.di.AppComponent;
import com.emmanuel.movieplus.di.DaggerAppComponent;
import com.emmanuel.movieplus.di.modules.NetworkModule;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MovieApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule())
                .roomModule(new RoomModule(this))
                .applicationModule(new ApplicationModule(this))
                .movieRepositoryModule(new MovieRepositoryModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return this.appComponent;
    }
}
