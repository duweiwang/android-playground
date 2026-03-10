package com.example.lib_gles.video_filter.core.filter;

import android.util.Log;

import com.example.lib_gles.video_filter.core.EFramebufferObject;
import com.example.lib_gles.video_filter.core.GLogger;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;

/**
 * glfiter list , time aware
 * its name is like a list , But, one time one filter.  different with  GlFilterGroup.
 */
public class GlFilterList {

    private static final String TAG = "GlFilterList";
    private final LinkedList<GlFilterPeriod> glFilerPeriod = new LinkedList<>();
    private boolean needLastFrame = false;

    public GlFilterList() {
        glFilerPeriod.add(0, new GlFilterPeriod(0, Long.MAX_VALUE, new GlFilter()));
    }

    public void putGlFilter(GlFilterPeriod period) {
//        period.filter.setup();
        glFilerPeriod.add(0, period);
    }

    /**
     * Keep the built-in default filter and remove all user-added filters.
     */
    public void clearAddedFilters() {
        while (glFilerPeriod.size() > 1) {
            GlFilterPeriod period = glFilerPeriod.removeFirst();
            period.filter.release();
        }
    }

    public GlFilter getGlFilter(long time) {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            if (glFilterPeriod.contains(time)) {
                return glFilterPeriod.filter;
            }
        }
        return null;
    }

    public void draw(int texName, EFramebufferObject fbo, long presentationTimeUs, Map<String, Integer> extraTextureIds) {
        GLogger.d(TAG, "draw: presentationTimeUs:"+presentationTimeUs+", glFilerPeriod:"+glFilerPeriod);
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            if (glFilterPeriod.filter instanceof TimeScaleFilter) {
                // TimeScaleFilter controls timeline speed only; it should not block visual filters.
                continue;
            }
            if (glFilterPeriod.contains(presentationTimeUs / (1000*1000))) {
                needLastFrame = glFilterPeriod.filter.needLastFrame();
                Log.d(TAG, "draw: filter:"+glFilterPeriod.filter.getName());
                glFilterPeriod.filter.draw(texName, fbo, presentationTimeUs, extraTextureIds);
                return;
            }
        }
    }

    public double resolveTimeScaleAtMs(long presentationTimeMs) {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            if (glFilterPeriod.contains(presentationTimeMs)) {
                double scale = glFilterPeriod.filter.resolveTimeScaleAtMs(presentationTimeMs);
                if (Math.abs(scale - 1.0d) > 1e-9) {
                    return scale;
                }
            }
        }
        return 1.0d;
    }

    public boolean hasTimeScaleControl() {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            if (glFilterPeriod.filter.hasTimeScaleControl()) {
                return true;
            }
        }
        return false;
    }

    public void release() {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            glFilterPeriod.filter.release();
        }
    }

    public void setFrameSize(int width, int height) {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            glFilterPeriod.filter.setFrameSize(width, height);
        }
    }

    public void setup() {
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            glFilterPeriod.filter.setup();
        }
    }

    public boolean needLastFrame() {
        return needLastFrame;
    }

    @NotNull
    public GlFilterList copy() {
        GlFilterList filterList = new GlFilterList();
        for (GlFilterPeriod glFilterPeriod : glFilerPeriod) {
            filterList.putGlFilter(glFilterPeriod.copy());
        }
        return filterList;
    }
}
