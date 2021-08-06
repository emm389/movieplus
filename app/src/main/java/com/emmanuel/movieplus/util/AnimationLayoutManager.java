package com.emmanuel.movieplus.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by Emmanuel on 8/5/2021.
 */

public class AnimationLayoutManager extends LinearLayoutManager {

    private AnticipateOvershootInterpolator enterInterpolation = new AnticipateOvershootInterpolator();

    public AnimationLayoutManager(Context context) {
        super(context);
    }

    public AnimationLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AnimationLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);

        AnimationSet animationSet = new AnimationSet(true);

        float h = 1.0f;

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, index == 0 ? -h : h, Animation.RELATIVE_TO_SELF, 0.0f);
        translateAnimation.setDuration(400);

        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(anim);

        child.startAnimation(animationSet);
    }
}
