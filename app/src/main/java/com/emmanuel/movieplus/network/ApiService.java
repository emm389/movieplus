package com.emmanuel.movieplus.network;

import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.movies.model.MovieResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public interface ApiService {

    @GET(UrlManager.POPULAR_MOVIES)
    Single<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET(UrlManager.TOP_RATED_MOVIES)
    Single<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET(UrlManager.GET_MOVIE_DETAIL)
    Single<MovieDetail> getMovieDetail(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
