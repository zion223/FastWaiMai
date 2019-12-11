package com.zrp.latte.ui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zrp.latte.delegates.LatteDelegate;

public final class BazierAnimation {

    public static void addToCart(LatteDelegate delegate, View start, View end,
                                ImageView target){

    }
    public static void addToShopCart(final View imageView, final ViewGroup fromView, float[] medianPosition, final View targetView) {
        final LinearLayout.LayoutParams params;
        final float[] mCurrentPosition = new float[2];
        final int iconWidth = targetView.getWidth();
        final int iconHeidht = targetView.getHeight();
        final int height = fromView.getHeight();
        final int width = fromView.getWidth();

        final Drawable drawable = ((ImageView) imageView).getDrawable();
        if (drawable != null) {
            params = new LinearLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        imageView.setLayoutParams(params);
        fromView.addView(imageView);
        Path path = new Path();
        path.moveTo(width / 2, -height / 2);
        path.cubicTo(width / 2, -height / 2, medianPosition[0], medianPosition[1], -iconWidth / 2, iconHeidht);
        final PathMeasure mPathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(5000);
        //线性插值器:匀速动画
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setVisibility(View.INVISIBLE);
            }
        });
    }
    public static void addToShopCart(final View imageView, final ViewGroup fromView, float[] medianPosition, final float[] targetPosition) {
        final LinearLayout.LayoutParams params;
        final float[] mCurrentPosition = new float[2];

        final Drawable drawable = ((ImageView) imageView).getDrawable();
        if (drawable != null) {
            params = new LinearLayout.LayoutParams(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        imageView.setLayoutParams(params);
        fromView.addView(imageView);
        Path path = new Path();
        //path.moveTo(width / 2, -height / 2);
        path.cubicTo(0,0, 0, -1500, -1000,2000);
        final PathMeasure mPathMeasure = new PathMeasure(path, false);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(5000);
        //线性插值器:匀速动画
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //imageView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
