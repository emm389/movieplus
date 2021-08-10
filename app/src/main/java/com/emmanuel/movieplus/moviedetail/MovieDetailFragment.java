package com.emmanuel.movieplus.moviedetail;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.FragmentMovieDetailBinding;
import com.emmanuel.movieplus.moviedetail.adapters.VideoAdapter;
import com.emmanuel.movieplus.moviedetail.model.Genre;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.moviedetail.model.Video;
import com.emmanuel.movieplus.network.UrlManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "movieId";
    private int movieId;
    private FragmentMovieDetailBinding binding;
    private MovieDetailViewModel movieDetailViewModel;
    private RequestOptions requestOptions;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(int movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getInt(ARG_PARAM1);
        }

        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.getMovieDetail(movieId);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide));

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .remove(MovieDetailFragment.this)
                        .commit();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.progress_anim)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMovieDetailBinding.inflate(inflater);
        binding.setViewModel(movieDetailViewModel);

        binding.toolbar.setNavigationOnClickListener(view ->
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .remove(MovieDetailFragment.this)
                        .commit());

        movieDetailViewModel.getMovieDetailResponseObserver().observe(getViewLifecycleOwner(), this::showInfo);

        return binding.getRoot();
    }

    private void showInfo(MovieDetail movieDetail) {
        Glide.with(requireContext())
                .load(UrlManager.BASE_IMAGE_URL + "/w500" + movieDetail.getBackdropPath())
                .apply(requestOptions)
                .into(binding.movieImage);

        binding.collapingToolbar.setTitle(movieDetail.getTitle());
        binding.average.setText(String.valueOf(movieDetail.getVoteAverage()));
        binding.year.setText(movieDetail.getReleaseDate().split("-")[0]);
        String duration = movieDetail.getRuntime() + " min.";
        binding.duration.setText(duration);
        binding.overview.setText(movieDetail.getOverview());
        binding.forAdults.setVisibility(movieDetail.isAdult() ? View.VISIBLE : View.INVISIBLE);

        if (movieDetail.getBelongsToCollection() != null) {
            Glide.with(requireContext())
                    .load(UrlManager.BASE_IMAGE_URL + "/w200" + movieDetail.getBelongsToCollection().getPosterPath())
                    .apply(requestOptions)
                    .into(binding.movieImageBelongs);
            binding.nameBelongs.setText(movieDetail.getBelongsToCollection().getName());
        } else {
            binding.movieImageBelongs.setVisibility(View.GONE);
            binding.nameBelongs.setVisibility(View.GONE);
            binding.belongsToCollectionLbl.setVisibility(View.GONE);
        }

        new Handler().postDelayed(() -> {
            loadGenres(movieDetail.getGenres());
            loadVideos(movieDetail.getVideos().getResults());
        }, 500);
    }

    private void loadGenres(List<Genre> genreList) {
        binding.contentGenres.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (Genre genre : genreList) {
            TextView view = (TextView) inflater.inflate(R.layout.item_genre, binding.contentGenres, false);
            view.setText(genre.getName());

            binding.contentGenres.addView(view);
        }
    }

    private void loadVideos(List<Video> videos) {
        if (videos.isEmpty()) {
            binding.trailers.setVisibility(View.GONE);
            return;
        }
        RecyclerView.LayoutManager manager = new LinearLayoutManager(requireContext());
        binding.contentVideos.setLayoutManager(manager);
        binding.contentVideos.setItemAnimator(new DefaultItemAnimator());

        VideoAdapter adapter = new VideoAdapter(requireContext(), videos);
        binding.contentVideos.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}