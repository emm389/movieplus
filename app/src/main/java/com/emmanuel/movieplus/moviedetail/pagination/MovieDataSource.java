package com.emmanuel.movieplus.moviedetail.pagination;

import android.app.Application;
import android.util.Log;

import com.emmanuel.movieplus.MovieApplication;
import com.emmanuel.movieplus.movies.model.Movie;
import com.emmanuel.movieplus.movies.model.MovieResponse;
import com.emmanuel.movieplus.network.ApiService;
import com.emmanuel.movieplus.network.UrlManager;
import com.emmanuel.movieplus.repositories.MovieRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private final ApiService apiService;
    private final String s;
    private final boolean isSearch;
    private final CompositeDisposable disposable;
    @Inject
    MovieRepository repository;

    public MovieDataSource(Application application, ApiService repository, CompositeDisposable compositeDisposable, String s, boolean isSearch) {
        this.apiService = repository;
        this.disposable = compositeDisposable;
        this.s = s;
        this.isSearch = isSearch;

        ((MovieApplication)application).getAppComponent().inject(this);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        if (!isSearch) {
            switch (s) {
                case "Popular":
                    disposable.add(apiService
                            .getPopularMovies(UrlManager.API_KEY, 1)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                                @Override
                                public void onSuccess(@NonNull MovieResponse movieResponse) {
                                    disposable.add(repository
                                            .insertMovies(movieResponse.getResults())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe());
                                    callback.onResult(movieResponse.getResults(), null, movieResponse.getPage() + 1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    disposable.add(repository
                                            .getMoviesFromDatabase(1)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(list -> callback.onResult(list, null, 2)));
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
                                    callback.onResult(movieResponse.getResults(), null, movieResponse.getPage() + 1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    disposable.add(repository
                                            .getMoviesFromDatabase(1)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(list -> callback.onResult(list, null, 2)));
                                }
                            }));
                    break;
            }
        } else {
            disposable.add(apiService
                    .searchMovie(UrlManager.API_KEY, s,1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                        @Override
                        public void onSuccess(@NonNull MovieResponse movieResponse) {
                            disposable.add(repository
                                    .insertMovies(movieResponse.getResults())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe());
                            callback.onResult(movieResponse.getResults(), null, movieResponse.getPage() + 1);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            disposable.add(repository
                                    .getMoviesFromSearch(s, 1)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(list -> callback.onResult(list, null, 2)));
                        }
                    }));
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        //Nothing
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        if (!isSearch) {
            switch (s) {
                case "Popular":
                    disposable.add(apiService
                            .getPopularMovies(UrlManager.API_KEY, params.key)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                                @Override
                                public void onSuccess(@NonNull MovieResponse movieResponse) {
                                    disposable.add(repository
                                            .insertMovies(movieResponse.getResults())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe());
                                    callback.onResult(movieResponse.getResults(), params.key == movieResponse.getTotalPages() ? null : params.key + 1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    disposable.add(repository
                                            .getMoviesFromDatabase(params.key)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(list ->
                                                disposable.add(repository
                                                        .getMovieCount()
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(count -> {
                                                            int totalPages = (int)Math.ceil(count/20);
                                                            callback.onResult(list, params.key.equals(totalPages) ? null : params.key + 1);
                                                        }, ex -> Log.e("Error", e.getMessage(), e)))
                                            ));
                                }
                            }));
                    break;
                case "Top Rated":
                    disposable.add(apiService
                            .getTopRatedMovies(UrlManager.API_KEY, params.key)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                                @Override
                                public void onSuccess(@NonNull MovieResponse movieResponse) {
                                    callback.onResult(movieResponse.getResults(), params.key == movieResponse.getTotalPages() ? null : params.key + 1);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    disposable.add(repository
                                            .getMoviesFromDatabase(params.key)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(list -> disposable.add(repository
                                                        .getMovieCount()
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(count -> {
                                                            int totalPages = (int)Math.ceil(count/20);
                                                            callback.onResult(list, params.key.equals(totalPages) ? null : params.key + 1);
                                                        }))));
                                }
                            }));
                    break;

            }
        } else {
            disposable.add(apiService
                    .searchMovie(UrlManager.API_KEY, s, params.key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                        @Override
                        public void onSuccess(@NonNull MovieResponse movieResponse) {
                            callback.onResult(movieResponse.getResults(), params.key == movieResponse.getTotalPages() ? null : params.key + 1);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            disposable.add(repository
                                    .getMoviesFromSearch(s, params.key)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(list ->
                                        disposable.add(repository
                                                .getMovieCount()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(count -> {
                                                    int totalPages = (int)Math.ceil(count/20);
                                                    callback.onResult(list, params.key.equals(totalPages) ? null : params.key + 1);
                                                }))
                                    )
                            );
                        }
                    }));
        }
    }
}
