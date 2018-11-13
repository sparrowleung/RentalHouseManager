package com.example.guillotine;

import android.animation.TimeInterpolator;
import android.view.View;

public class GuillotineAnimation {

    public GuillotineAnimation() {

    }

    public static class AnimationBuilder {
        private View openView;
        private View closeView;
        private View actionBarView;
        private View guillotineView;

        private long duration;
        private long startDelay;

        private TimeInterpolator timeInterpolator;
        private GuillotineListener guillotineListener;

        public AnimationBuilder(View openView, View closeView, View guillotineView) {
            this.openView = openView;
            this.closeView = closeView;
            this.guillotineView = guillotineView;
        }
    }
}
