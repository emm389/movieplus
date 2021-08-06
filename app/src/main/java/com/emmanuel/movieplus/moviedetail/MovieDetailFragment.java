package com.emmanuel.movieplus.moviedetail;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.FragmentMovieDetailBinding;
import com.emmanuel.movieplus.moviedetail.model.Genre;
import com.emmanuel.movieplus.moviedetail.model.MovieDetail;
import com.emmanuel.movieplus.network.UrlManager;

import java.util.List;
import java.util.zip.Inflater;

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

        movieDetailViewModel.getMoviesResponseObserver().observe(getViewLifecycleOwner(), this::showInfo);

        movieDetailViewModel.getServerResponseObserver()
                .observe(getViewLifecycleOwner(), s -> Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show());

        return binding.getRoot();
    }

    private void showInfo(MovieDetail movieDetail) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.progress_anim)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        Glide.with(requireContext())
                .load(UrlManager.BASE_IMAGE_URL + "/w500" + movieDetail.getBackdropPath())
                .apply(requestOptions)
                .into(binding.movieImage);

        binding.collapingToolbar.setTitle(movieDetail.getTitle());
        binding.average.setText(String.valueOf(movieDetail.getVoteAverage()));
        binding.year.setText(movieDetail.getReleaseDate().split("-")[0]);
        binding.overview.setText(movieDetail.getOverview());
        binding.forAdults.setVisibility(movieDetail.isAdult() ? View.VISIBLE : View.INVISIBLE);

        loadGenres(movieDetail.getGenres());
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
}