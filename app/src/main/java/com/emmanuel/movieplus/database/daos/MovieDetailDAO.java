package com.emmanuel.movieplus.database.daos;

import com.emmanuel.movieplus.moviedetail.model.MovieDetail;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Emmanuel on 8/7/2021.
 */

@Dao
public interface MovieDetailDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(MovieDetail movieDetail);

    @Query("SELECT * FROM movie_detail WHERE id = :id LIMIT 1")
    Flowable<MovieDetail> getMovieDetail(int id);
}
