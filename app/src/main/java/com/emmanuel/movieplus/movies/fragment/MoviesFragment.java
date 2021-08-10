package com.emmanuel.movieplus.movies.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.FragmentMoviesBinding;
import com.emmanuel.movieplus.movies.adapters.MoviesAdapter;
import com.emmanuel.movieplus.util.SlideUpAlphaAnimator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private String category;

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
        category = "Popular";
        if (getArguments() != null) {
            category = getArguments().getString(ARG_SECTION_NUMBER);
        }

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));

        moviesViewModel.getMovies(category, false);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMoviesBinding.inflate(inflater);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        binding.movies.setLayoutManager(manager);
        binding.movies.setHasFixedSize(true);
        binding.movies.setItemAnimator(new SlideUpAlphaAnimator());

        adapter = new MoviesAdapter(this);
        binding.movies.setAdapter(adapter);

        showMovies();

        initSearch();
        return root;
    }

    private void initSearch() {
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.progress.setVisibility(View.VISIBLE);
                adapter.submitList(null);
                moviesViewModel.getMovies(binding.search.getQuery().toString(), true);
                showMovies();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    moviesViewModel.getMovies(category, false);
                    showMovies();
                    return false;
                }
                return true;
            }
        });
    }

    private void showMovies() {
        adapter.submitList(null);
        moviesViewModel.getListLiveData().observe(
                getViewLifecycleOwner(),
                movies -> {
                    binding.progress.setVisibility(View.GONE);
                    adapter.submitList(movies);
                    binding.movies.scheduleLayoutAnimation();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}