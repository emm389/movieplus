package com.emmanuel.movieplus.moviedetail;

import android.app.Application;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
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

public class MovieDetailViewModel extends AndroidViewModel {

    @Inject
    ApiService apiService;
    private final CompositeDisposable disposable;
    private final MutableLiveData<MovieDetail> liveData;
    private final MutableLiveData<String> serverResponse;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        ((MovieApplication) application).getMovieDetailComponent().inject(this);
        disposable = new CompositeDisposable();
        liveData = new MutableLiveData<>();
        serverResponse = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDetail> getMoviesResponseObserver() {
        return liveData;
    }

    public MutableLiveData<String> getServerResponseObserver() {
        return serverResponse;
    }

    public void getMovieDetail(int movieId) {
        disposable.add(apiService
                .getMovieDetail(movieId, UrlManager.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieDetail>() {
                    @Override
                    public void onSuccess(@NonNull MovieDetail movieDetail) {
                        liveData.postValue(movieDetail);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        serverResponse.postValue(e.getMessage());
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
