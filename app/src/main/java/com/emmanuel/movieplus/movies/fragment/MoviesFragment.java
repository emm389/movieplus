package com.emmanuel.movieplus.movies.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.FragmentMoviesBinding;
import com.emmanuel.movieplus.movies.adapters.MoviesAdapter;
import com.emmanuel.movieplus.util.AnimationLayoutManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "category";

    private MoviesViewModel moviesViewModel;
    private FragmentMoviesBinding binding;
    public MoviesAdapter adapter;

    public static MoviesFragment newInstance(String category) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NUMBER, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        String category = "Popular";
        if (getArguments() != null) {
            category = getArguments().getString(ARG_SECTION_NUMBER);
        }

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));

        moviesViewModel.getMovies(category);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMoviesBinding.inflate(inflater);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);

        RecyclerView moviesList = binding.movies;
        RecyclerView.LayoutManager manager = new AnimationLayoutManager(getContext());
        moviesList.setLayoutManager(manager);
        moviesList.setHasFixedSize(true);

        adapter = new MoviesAdapter(this, new ArrayList<>());
        moviesList.setAdapter(adapter);

        moviesViewModel.getMoviesResponseObserver().observe(
                getViewLifecycleOwner(),
                movieResponse -> {
                    adapter.setMovieList(movieResponse.getResults());
                    moviesList.scheduleLayoutAnimation();
                });

        moviesViewModel.getServerResponseObserver().observe(
                getViewLifecycleOwner(),
                response -> Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show()
        );
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}