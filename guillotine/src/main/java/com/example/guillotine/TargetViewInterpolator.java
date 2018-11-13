package com.example.guillotine;

import android.animation.TimeInterpolator;

public class TargetViewInterpolator implements TimeInterpolator {

    private static final float FIRST_BOUNCE_TIME = 0.35f;
    private static final float SECOND_BOUNCE_TIME = 0.65f;

    @Override
    public float getInterpolation(float input) {
        if (input < FIRST_BOUNCE_TIME) {
            return input * input * (-28.4f) + 10f * input;
        } else if (input < SECOND_BOUNCE_TIME) {
            return 21.3f * input * input - 21.3f * input + 5.0f;
        } else {
            return (-9f) * input * input + 15.4f * input - 6.0f;
        }
    }
}
