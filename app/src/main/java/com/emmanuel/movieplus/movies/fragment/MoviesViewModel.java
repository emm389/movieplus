package com.emmanuel.movieplus.movies.fragment;

import android.app.Application;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.movies.model.MovieResponse;
import com.emmanuel.movieplus.network.ApiService;
import com.emmanuel.movieplus.network.UrlManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MoviesViewModel extends AndroidViewModel {

    @Inject
    public ApiService apiService;
    private final MutableLiveData<MovieResponse> movieResponseData;
    private final MutableLiveData<String> serverResponse;
    private final CompositeDisposable disposable;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        ((MovieApplication) application).getNetworkComponent().inject(this);
        disposable = new CompositeDisposable();
        movieResponseData = new MutableLiveData<>();
        serverResponse = new MutableLiveData<>();
    }

    public MutableLiveData<MovieResponse> getMoviesResponseObserver() {
        return movieResponseData;
    }

    public MutableLiveData<String> getServerResponseObserver() {
        return serverResponse;
    }

    public void getMovies(String category) {
        switch (category) {
            case "Popular":
                disposable.add(apiService
                        .getPopularMovies(UrlManager.API_KEY, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                            @Override
                            public void onSuccess(@NonNull MovieResponse movieResponse) {
                                movieResponseData.postValue(movieResponse);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                serverResponse.postValue(e.getMessage());
                            }
                        }));
                break;
            case "Top Rated":
                disposable.add(apiService
                        .getTopRatedMovies(UrlManager.API_KEY, 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                            @Override
                            public void onSuccess(@NonNull MovieResponse movieResponse) {
                                movieResponseData.postValue(movieResponse);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                serverResponse.postValue(e.getMessage());
                            }
                        }));
                break;

        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
