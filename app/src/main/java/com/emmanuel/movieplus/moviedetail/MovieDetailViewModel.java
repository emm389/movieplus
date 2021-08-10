package com.emmanuel.movieplus.moviedetail;

import android.app.Application;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.repositories.MovieRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MovieDetailViewModel extends AndroidViewModel {

    @Inject
    public MovieRepository repository;
    private final CompositeDisposable disposable;
    private final MutableLiveData<MovieDetail> liveData;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        ((MovieApplication) application).getAppComponent().inject(this);
        disposable = new CompositeDisposable();
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDetail> getMovieDetailResponseObserver() {
        return liveData;
    }

    public void getMovieDetail(int movieId) {
        repository.getMovieDetail(disposable, movieId, liveData);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
