package com.emmanuel.movieplus.database;

import android.content.Context;

import com.emmanuel.movieplus.database.daos.MovieDAO;
import com.emmanuel.movieplus.movies.model.Movie;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.Q;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by Emmanuel on 8/9/2021.
 */

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Q})
public class MovieDAOTest {

    private MovieDAO movieDAO;
    private MovieDatabase database;
    private CompositeDisposable disposable;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        movieDAO = database.movieDAO();
        disposable = new CompositeDisposable();
    }

    @After
    public void closeDb() {
        database.close();
        disposable.clear();
        disposable = null;
    }

    @Test
    public void writeAndReadMovie() {
        Movie movie = new Movie();
        movie.setAdult(false);
        movie.setBackdropPath("backdrop");
        movie.setTitle("Title");
        List<Integer> genres = new ArrayList<>();
        genres.add(1);
        genres.add(2);
        genres.add(3);
        movie.setGenreIds(genres);
        movie.setId(123);
        movie.setOriginalLanguage("En");
        movie.setOriginalTitle("OriginalTitle");
        movie.setPopularity(10.5f);
        movie.setOverview("Overview");
        movie.setPosterPath("posterPath");
        movie.setReleaseDate("releaseDate");
        movie.setVideo(false);
        movie.setVoteAverage(7.5f);
        movie.setVoteCount(7);

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        disposable.add(movieDAO
                .insert(movies)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> disposable.add(movieDAO
                        .getMovies(1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> assertThat(result.get(0), equalTo(movie))))));
    }
}
