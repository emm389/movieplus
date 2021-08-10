package com.emmanuel.movieplus.movies.fragment;

import android.app.Application;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.movies.model.Movie;
import com.emmanuel.movieplus.repositories.MovieRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MoviesViewModel extends AndroidViewModel {

    @Inject
    public MovieRepository repository;

    private LiveData<PagedList<Movie>> listLiveData;
    private final CompositeDisposable disposable;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        ((MovieApplication) application).getAppComponent().inject(this);
        disposable = new CompositeDisposable();
    }

    public void getMovies(String s, boolean isSearch) {
        listLiveData = repository.getMovies(disposable, s, isSearch);
    }

    public LiveData<PagedList<Movie>> getListLiveData() {
        return listLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
