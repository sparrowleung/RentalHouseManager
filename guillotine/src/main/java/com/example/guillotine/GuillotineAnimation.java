package com.example.guillotine;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewTreeObserver;

public class GuillotineAnimation {
    private static final long DURATION_TIME = 250;
    private static final float OPEN_GUILLOTINE_VIEW = -90f;
    private static final float CLOSE_GUILLOTINE_VIEW = 0f;
    private static final float ACTION_BAR_PART = 3f;

    private View mOpenView;
    private View mCloseView;
    private View mActionBarView;
    private View mGuillotineView;

    private long mDuration;
    private long mStartDelay;
    private boolean isOpening;
    private boolean isClosing;

    private TimeInterpolator mTimeInterpolator;
    private GuillotineListener mGuillotineListener;

    private ObjectAnimator mOpenAnimator;
    private ObjectAnimator mCloseAnimator;

    public GuillotineAnimation(AnimationBuilder builder) {
        this.mDuration = builder.duration > 0 ? builder.duration : DURATION_TIME;
        this.mStartDelay = builder.startDelay;

        this.mOpenView = builder.openView;
        this.mCloseView = builder.closeView;
        this.mActionBarView = builder.actionBarView;
        this.mGuillotineView = builder.guillotineView;

        this.mTimeInterpolator = builder.timeInterpolator != null ? builder.timeInterpolator : new GuillotineViewInterpolator();
        this.mGuillotineListener = builder.guillotineListener;

        initOpenView(mOpenView);
        initCloseView(mCloseView);
        mOpenAnimator = setUpOpenAnimator();
        mCloseAnimator = setUpCloseAnimator();

        if (builder.isClosedOnBegin) {
            mGuillotineView.setRotation(OPEN_GUILLOTINE_VIEW);
            mGuillotineView.setVisibility(View.INVISIBLE);
        }
    }

    private void openGuillotineView() {
        if (!isOpening) {
            mOpenAnimator.start();
        }
    }

    private void closeGuillotineView() {
        if (!isClosing) {
            mCloseAnimator.start();
        }
    }

    private void initOpenView(final View view) {
        mGuillotineView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGuillotineView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mGuillotineView.setPivotX(calculateForViewX(view));
                mGuillotineView.setPivotY(calculateForViewY(view));
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuillotineView();
            }
        });
    }

    private void initCloseView(final View view) {
        mActionBarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mActionBarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mActionBarView.setPivotX(calculateForViewX(view));
                mActionBarView.setPivotY(calculateForViewY(view));
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGuillotineView();
            }
        });
    }

    private ObjectAnimator setUpOpenAnimator() {
        ObjectAnimator openAnimator = ObjectAnimator.ofFloat(mGuillotineView, "rotation", OPEN_GUILLOTINE_VIEW, CLOSE_GUILLOTINE_VIEW);
        openAnimator.setDuration(mDuration);
        openAnimator.setStartDelay(mStartDelay);
        openAnimator.setInterpolator(mTimeInterpolator);
        openAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isOpening = true;
                mGuillotineView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isOpening = false;
                if (mGuillotineListener != null) {
                    mGuillotineListener.onGuillotineViewOpen();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return openAnimator;
    }

    private ObjectAnimator setUpCloseAnimator() {
        ObjectAnimator closeAnimator = ObjectAnimator.ofFloat(mGuillotineView, "rotation", CLOSE_GUILLOTINE_VIEW, OPEN_GUILLOTINE_VIEW);
        closeAnimator.setDuration(mDuration);
        closeAnimator.setStartDelay(mStartDelay);
        closeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isClosing = true;
                mGuillotineView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isClosing = false;
                actionBarAnimator();
                if (mGuillotineListener != null) {
                    mGuillotineListener.onGuillotineViewClose();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return mCloseAnimator;
    }

    public void actionBarAnimator() {
        ObjectAnimator actionBarAnimator = ObjectAnimator.ofFloat(mActionBarView, "rotation", CLOSE_GUILLOTINE_VIEW, ACTION_BAR_PART);
        actionBarAnimator.setStartDelay(mStartDelay);
        actionBarAnimator.setDuration((long) (mDuration * (GuillotineViewInterpolator.FIRST_BOUNCE_TIME + GuillotineViewInterpolator.SECOND_BOUNCE_TIME)));
        actionBarAnimator.setInterpolator(new TargetViewInterpolator());
        actionBarAnimator.start();
    }

    private float calculateForViewX(View view) {
        return (float) (view.getLeft() + view.getWidth() / 2);
    }

    private float calculateForViewY(View view) {
        return (float) (view.getTop() + view.getHeight() / 2);
    }

    public static class AnimationBuilder {
        private View openView;
        private View closeView;
        private View actionBarView;
        private View guillotineView;

        private long duration;
        private long startDelay;
        private boolean isClosedOnBegin;

        private TimeInterpolator timeInterpolator;
        private GuillotineListener guillotineListener;

        public AnimationBuilder(View openView, View closeView, View guillotineView) {
            this.openView = openView;
            this.closeView = closeView;
            this.guillotineView = guillotineView;
        }

        public AnimationBuilder setIsCloseOnBegin(boolean isClosedOnBegin) {
            this.isClosedOnBegin = isClosedOnBegin;
            return this;
        }

        public AnimationBuilder setActionBarView(View view) {
            this.actionBarView = view;
            return this;
        }

        public AnimationBuilder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public AnimationBuilder setStartDelay(long startDelay) {
            this.startDelay = startDelay;
            return this;
        }

        public AnimationBuilder setIneterpolator(TimeInterpolator interpolator) {
            this.timeInterpolator = interpolator;
            return this;
        }

        public AnimationBuilder setListener(GuillotineListener listener) {
            this.guillotineListener = listener;
            return this;
        }

        public GuillotineAnimation build() {
            return new GuillotineAnimation(this);
        }
    }
}
