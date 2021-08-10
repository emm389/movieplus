package com.emmanuel.movieplus.moviedetail.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emmanuel.movieplus.R;
import com.emmanuel.movieplus.databinding.ItemVideoBinding;
import com.emmanuel.movieplus.moviedetail.model.Video;
import com.emmanuel.movieplus.network.UrlManager;
import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.iconics.IconicsDrawable;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final Context context;
    private final List<Video> videos;
    private final RequestOptions requestOptions;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.progress_anim)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.LOW)
                .dontAnimate()
                .dontTransform();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(ItemVideoBinding.inflate(LayoutInflater.from(context)));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.onBind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private final ItemVideoBinding binding;

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(Video video) {
            ((Activity) itemView.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            binding.videoName.setText(video.getName());

            binding.playerView.setVisibility(View.INVISIBLE);
            binding.thumbnail.setVisibility(View.VISIBLE);
            binding.btnPlay.setVisibility(View.VISIBLE);

            binding.btnPlay.setImageDrawable(new IconicsDrawable(context, Entypo.Icon.ent_controller_play).color(ContextCompat.getColor(context, R.color.white)));

            Glide.with(context)
                    .load(UrlManager.VIDEO_THUMBNAIL_URL + video.getKey() + UrlManager.VIDEO_THUMBNAIL_URL_EXTENSION)
                    .apply(requestOptions)
                    .into(binding.thumbnail);

            binding.btnPlay.setOnClickListener(view1 -> {
                binding.playerView.setVisibility(View.VISIBLE);
                binding.thumbnail.setVisibility(View.INVISIBLE);
                binding.btnPlay.setVisibility(View.INVISIBLE);

                binding.playerView.initialize(new YouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.cueVideo(video.getKey(), 0);
                        youTubePlayer.play();

                        binding.playerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                            @Override
                            public void onYouTubePlayerEnterFullScreen() {
                                binding.playerView.enterFullScreen();
                            }

                            @Override
                            public void onYouTubePlayerExitFullScreen() {
                                binding.playerView.exitFullScreen();
                            }
                        });
                    }

                    @Override
                    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {

                    }

                    @Override
                    public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                    }

                    @Override
                    public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

                    }

                    @Override
                    public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

                    }

                    @Override
                    public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {

                    }

                    @Override
                    public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                    }

                    @Override
                    public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                    }
                });
            });
        }
    }
}
