package com.emmanuel.movieplus.util;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Emmanuel on 8/7/2021.
 */

public class SlideUpAlphaAnimator extends DefaultAnimator<SlideUpAlphaAnimator>{

    @Override
    public void addAnimationPrepare(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(holder.itemView.getHeight());
        holder.itemView.setAlpha(0);
    }

    @Override
    public ViewPropertyAnimatorCompat addAnimation(RecyclerView.ViewHolder holder) {
        return ViewCompat.animate(holder.itemView).translationY(0).alpha(1).setDuration(getMoveDuration()).setInterpolator(getInterpolator());
    }

    @Override
    public void addAnimationCleanup(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(0);
        holder.itemView.setAlpha(1);
    }

    @Override
    public long getAddDelay(long remove, long move, long change) {
        return 0;
    }

    @Override
    public long getRemoveDelay(long remove, long move, long change) {
        return 0;
    }

    @Override
    public ViewPropertyAnimatorCompat removeAnimation(RecyclerView.ViewHolder holder) {
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(holder.itemView);
        return animation.setDuration(getRemoveDuration()).alpha(0).translationY(holder.itemView.getHeight()).setInterpolator(getInterpolator());
    }

    @Override
    public void removeAnimationCleanup(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(0);
        holder.itemView.setAlpha(1);
    }
}
