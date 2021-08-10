package com.emmanuel.movieplus.moviedetail.pagination;

import android.app.Application;

import com.emmanuel.movieplus.movies.model.Movie;
import com.emmanuel.movieplus.network.ApiService;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<MovieDataSource> liveData;
    private ApiService apiService;
    private CompositeDisposable disposable;
    private String s;
    private boolean isSearch;
    private Application application;

    public MovieDataSourceFactory(Application application, ApiService apiService, CompositeDisposable disposable, String s, boolean isSearch) {
        this.liveData = new MutableLiveData<>();
        this.apiService = apiService;
        this.disposable = disposable;
        this.s = s;
        this.isSearch = isSearch;
        this.application = application;
    }

    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        MovieDataSource dataSource = new MovieDataSource(application, apiService, disposable, s, isSearch);
        liveData.postValue(dataSource);
        return dataSource;
    }
}
