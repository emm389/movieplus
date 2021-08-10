package com.emmanuel.movieplus.di.modules;

import com.emmanuel.movieplus.network.ApiService;
import com.emmanuel.movieplus.network.UrlManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Emmanuel on 8/5/2021.
 */

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(UrlManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @Singleton
    @Provides
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
