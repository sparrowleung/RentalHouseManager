package com.example.guillotine;

import android.animation.TimeInterpolator;

public class GuillotineViewInterpolator implements TimeInterpolator {
    private static final float ROTATION_TIME = 0.4f;
    public static final float FIRST_BOUNCE_TIME = 0.3f;
    public static final float SECOND_BOUNCE_TIME = 0.3f;

    @Override
    public float getInterpolation(float input) {
        if (input < ROTATION_TIME) {
            return input * input * 4.5f;
        } else if (input < FIRST_BOUNCE_TIME + SECOND_BOUNCE_TIME) {
            return input * input * 2.5f - 3f * input + 1.8f;
        } else {
            return 0.6f * input * input - 1.2f * input + 1.4f;
        }
    }
}
