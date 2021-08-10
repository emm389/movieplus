package com.emmanuel.movieplus.database.daos;

import com.emmanuel.movieplus.movies.model.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by Emmanuel on 8/7/2021.
 */

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Movie> movies);

    @Query("SELECT DISTINCT * FROM movie LIMIT :page, 20")
    Observable<List<Movie>> getMovies(int page);

    @Query("SELECT COUNT(1) FROM movie")
    Flowable<Integer> getCount();

    @Query("SELECT DISTINCT * FROM movie WHERE movie.title = :search LIMIT 20 OFFSET :page")
    Observable<List<Movie>> searchMovie(String search, int page);
}
