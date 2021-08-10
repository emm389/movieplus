package com.emmanuel.movieplus.movies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.ItemMovieBinding;
import com.emmanuel.movieplus.moviedetail.MovieDetailFragment;
import com.emmanuel.movieplus.movies.fragment.MoviesFragment;
import com.emmanuel.movieplus.movies.model.Movie;
import com.emmanuel.movieplus.network.UrlManager;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class MoviesAdapter extends PagedListAdapter<Movie, MoviesAdapter.MovieViewHolder> {

    private final MoviesFragment context;
    private final RequestOptions requestOptions;

    public MoviesAdapter(MoviesFragment context) {
        super(Movie.DIFF_CALLBACK);
        this.context = context;

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_anim)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.NORMAL)
                .dontAnimate()
                .dontTransform();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context.getContext()), R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.onBind(Objects.requireNonNull(getItem(position)));
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        public MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(Movie movie) {

            Glide.with(context)
                    .load(UrlManager.BASE_IMAGE_URL + "/w200" + movie.getPosterPath())
                    .apply(requestOptions)
                    .into(binding.movieImage);

            binding.title.setText(movie.getTitle());
            binding.votes.setText(String.valueOf(movie.getVoteAverage()));
            binding.description.setText(movie.getOverview());

            if (movie.getReleaseDate() != null)
                binding.releaseDate.setText(movie.getReleaseDate().split("-")[0]);

            binding.forAdults.setVisibility(movie.isAdult() ? View.VISIBLE : View.GONE);

            binding.contentMovie.setOnClickListener(view -> {

                MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie.getId());
                FragmentManager fragmentManager = context.requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out);
                transaction.add(R.id.contentFragment, movieDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }
    }
}
