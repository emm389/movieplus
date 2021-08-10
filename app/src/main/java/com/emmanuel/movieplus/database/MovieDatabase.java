package com.emmanuel.movieplus.database;

import com.emmanuel.movieplus.database.converters.BelongsToCollectionConverter;
import com.emmanuel.movieplus.database.converters.GenresConverter;
import com.emmanuel.movieplus.database.converters.ListConverter;
import com.emmanuel.movieplus.database.converters.VideosConverter;
import com.emmanuel.movieplus.database.daos.MovieDAO;
import com.emmanuel.movieplus.database.daos.MovieDetailDAO;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.movies.model.Movie;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Created by Emmanuel on 8/7/2021.
 */

@Database(entities = {
        Movie.class,
        MovieDetail.class
}, version = 1)
@TypeConverters({
        ListConverter.class,
        BelongsToCollectionConverter.class,
        GenresConverter.class,
        VideosConverter.class
})
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDAO movieDAO();
    public abstract MovieDetailDAO moviedetailDAO();
}
