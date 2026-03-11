package com.example.lib_gles.video_filter.core.filter;

/**
 * A marker filter for timeline speed control.
 * It keeps pass-through rendering behavior while exposing a desired timescale.
 */
public class TimeScaleFilter extends GlFilter {

    private final double timeScale;

    public TimeScaleFilter(double timeScale) {
        super();
        if (timeScale <= 0) {
            throw new IllegalArgumentException("timeScale must be > 0");
        }
        this.timeScale = timeScale;
    }

    @Override
    public double resolveTimeScaleAtMs(long presentationTimeMs) {
        return timeScale;
    }

    @Override
    public boolean hasTimeScaleControl() {
        return true;
    }
}
