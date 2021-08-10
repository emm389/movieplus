package com.emmanuel.movieplus.repositories;

import android.app.Application;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.database.daos.MovieDAO;
import com.emmanuel.movieplus.database.daos.MovieDetailDAO;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.moviedetail.pagination.MovieDataSourceFactory;
import com.emmanuel.movieplus.movies.model.Movie;
import com.emmanuel.movieplus.network.ApiService;
import com.emmanuel.movieplus.network.UrlManager;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class MovieRepository {

    public Application application;
    @Inject
    public ApiService apiService;
    @Inject
    public MovieDAO movieDAO;
    @Inject
    public MovieDetailDAO movieDetailDAO;

    public MovieRepository(Application application) {
        this.application = application;
        ((MovieApplication)application).getAppComponent().inject(this);
    }

    public Completable insertMovies(List<Movie> movieList) {
        return movieDAO.insert(movieList);
    }

    public Completable insertMovieDetail(MovieDetail movieDetail) {
        return movieDetailDAO.insert(movieDetail);
    }

    public Flowable<Integer> getMovieCount() {
        return movieDAO.getCount();
    }

    public LiveData<PagedList<Movie>> getMovies(CompositeDisposable disposable, String s, boolean isSearch) {
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(application, apiService, disposable, s, isSearch);

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(20).build();
        return new LivePagedListBuilder<>(movieDataSourceFactory, pagedListConfig).build();
    }

    public Observable<List<Movie>> getMoviesFromDatabase(int page) {
        return movieDAO.getMovies(page);
    }

    public Observable<List<Movie>> getMoviesFromSearch(String search, int page) {
        return movieDAO.searchMovie(search, page);
    }

    public Flowable<MovieDetail> getMovieDetailFromDatabase(int movieId) {
        return movieDetailDAO.getMovieDetail(movieId);
    }

    public void getMovieDetail(CompositeDisposable disposable, int movieId, MutableLiveData<MovieDetail> liveData) {
        disposable.add(apiService
                .getMovieDetail(movieId, UrlManager.API_KEY, "videos")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieDetail>() {
                    @Override
                    public void onSuccess(@NonNull MovieDetail movieDetail) {
                        disposable.add(insertMovieDetail(movieDetail).subscribeOn(Schedulers.io()).subscribe());
                        liveData.postValue(movieDetail);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.add(getMovieDetailFromDatabase(movieId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(liveData::postValue));
                    }
                }));
    }
}
