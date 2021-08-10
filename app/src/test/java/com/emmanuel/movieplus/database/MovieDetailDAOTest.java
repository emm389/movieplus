package com.emmanuel.movieplus.database;

import android.content.Context;

import com.emmanuel.movieplus.database.daos.MovieDetailDAO;
import com.emmanuel.movieplus.moviedetail.model.BelongsToCollection;
import com.emmanuel.movieplus.moviedetail.model.Genre;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.moviedetail.model.Video;
import com.emmanuel.movieplus.moviedetail.model.Videos;

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
 * Created by Emmanuel on 8/10/2021.
 */

@RunWith(AndroidJUnit4.class)
@Config(sdk = {Q})
public class MovieDetailDAOTest {

    private MovieDetailDAO movieDetailDAO;
    private MovieDatabase database;
    private CompositeDisposable disposable;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, MovieDatabase.class).build();
        movieDetailDAO = database.moviedetailDAO();
        disposable = new CompositeDisposable();
    }

    @After
    public void closeDb() {
        database.close();
        disposable.clear();
        disposable = null;
    }

    @Test
    public void writeAndReadMovieDetail() {
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setId(1);
        movieDetail.setAdult(false);
        movieDetail.setBackdropPath("backdrop");
        BelongsToCollection belongsToCollection = new BelongsToCollection();
        belongsToCollection.setId(1);
        belongsToCollection.setName("Belongs");
        belongsToCollection.setBackdropPath("backdrop");
        belongsToCollection.setPosterPath("posterPath");
        movieDetail.setBelongsToCollection(belongsToCollection);

        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Genre");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        movieDetail.setGenres(genres);

        movieDetail.setTitle("Title");
        movieDetail.setOverview("Overview");
        movieDetail.setPosterPath("posterPath");
        movieDetail.setReleaseDate("releaseDate");
        movieDetail.setVoteAverage(7.5f);
        movieDetail.setRuntime(105);

        Video video = new Video();
        video.setId("1");
        video.setIso6391("iso6391");
        video.setIso31661("iso31661");
        video.setName("Video");
        video.setKey("wdff08sdo870");
        video.setOfficial(true);
        video.setPublishedAt("publishedAt");
        video.setSite("site");
        video.setSize(103);
        video.setType("type");
        List<Video> videos = new ArrayList<>();
        videos.add(video);
        Videos videos1 = new Videos();
        videos1.setResults(videos);
        movieDetail.setVideos(videos1);

        disposable.add(movieDetailDAO
                .insert(movieDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() ->
                        disposable.add(movieDetailDAO
                                .getMovieDetail(1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> assertThat(result, equalTo(movieDetail))))
                ));
    }
}
