package com.example.lib_gles.video_filter.core.filter;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;

import android.opengl.GLES20;


import com.example.lib_gles.video_filter.core.EFramebufferObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GlFilterGroup extends GlFilter {

    private final Collection<GlFilterPeriod> filterPeriods;

    private final ArrayList<GlFilterEntry> list = new ArrayList<GlFilterEntry>();

    static class GlFilterEntry{
        GlFilterPeriod filterPeriod;
        EFramebufferObject fbo;
        String name;

        public GlFilterEntry(GlFilterPeriod filter, EFramebufferObject fbo, String name) {
            this.filterPeriod = filter;
            this.fbo = fbo;
            this.name = name;
        }
    }

    public GlFilterGroup(final GlFilter... glFilters) {
        this(new ArrayList<>());
        for(GlFilter filter : glFilters) {
            add(filter);
        }
    }

    public GlFilterGroup(final GlFilterPeriod... glFilterPeriods) {
        this(Arrays.asList(glFilterPeriods));
    }

    public GlFilterGroup(final Collection<GlFilterPeriod> glFilterPeriods) {
        filterPeriods = glFilterPeriods;
    }

    @Override
    public void setup() {
        super.setup();

        if (filterPeriods != null) {
            for (final GlFilterPeriod period : filterPeriods) {
                period.filter.setup();
                // Period-enabled group needs dynamic active chain, so each entry keeps an intermediate FBO.
                final EFramebufferObject fbo = new EFramebufferObject();
                list.add(new GlFilterEntry(period, fbo, period.filter.getName()));
            }
        }

        if (mWidth > 0 && mHeight > 0) {
            setFrameSize(mWidth, mHeight);
        }
    }

    @Override
    public void release() {
        for (final GlFilterEntry entry : list) {
            if (entry.filterPeriod != null) {
                entry.filterPeriod.filter.release();
            }
            if (entry.fbo != null) {
                entry.fbo.release();
            }
        }
        list.clear();
        super.release();
    }

    @Override
    public void setFrameSize(final int width, final int height) {
        super.setFrameSize(width, height);

        for (final GlFilterEntry entry : list) {
            if (entry.filterPeriod != null) {
                entry.filterPeriod.filter.setFrameSize(width, height);
            }
            if (entry.fbo != null) {
                entry.fbo.setup(width, height);
            }
        }
    }

    private int prevTexName;

    @Override
    public int draw(final int texName, final EFramebufferObject fbo, long presentationTimeUs, Map<String,Integer> extraTextureIds) {
        checkSetUp();
        if ((mWidth <= 0 || mHeight <= 0) && fbo != null) {
            setFrameSize(fbo.getWidth(), fbo.getHeight());
        }
        List<GlFilterEntry> activeEntries = new ArrayList<>();
        long tMs = presentationTimeUs / (1000 * 1000);
        for (final GlFilterEntry entry : list) {
            if (entry.filterPeriod.filter instanceof TimeScaleFilter) {
                continue;
            }
            if (entry.filterPeriod != null && entry.filterPeriod.contains(tMs)) {
                activeEntries.add(entry);
            }
        }

        // No active period filter: passthrough to avoid black frames.
        if (activeEntries.isEmpty()) {
            return super.draw(texName, fbo, presentationTimeUs, extraTextureIds);
        }

        prevTexName = texName;
        int textName = -1;
        Map<String, Integer> extraTexIds = extraTextureIds == null ? new HashMap<>() : new HashMap<>(extraTextureIds);
        for (int i = 0; i < activeEntries.size(); i++) {
            final GlFilterEntry entry = activeEntries.get(i);
            final boolean isLastActive = (i == activeEntries.size() - 1);
            final EFramebufferObject targetFbo = isLastActive ? fbo : entry.fbo;

            if (targetFbo != null) {
                targetFbo.enable();
                GLES20.glClear(GL_COLOR_BUFFER_BIT);
            } else {
                GLES20.glBindFramebuffer(GL_FRAMEBUFFER, 0);
            }

            textName = entry.filterPeriod.filter.draw(prevTexName, targetFbo, presentationTimeUs, extraTexIds);
            extraTexIds.put(entry.name, textName);
            prevTexName = targetFbo != null ? targetFbo.getTexName() : prevTexName;
        }

        return textName;
    }

    @Override
    public double resolveTimeScaleAtMs(long presentationTimeMs) {
        for (final GlFilterEntry entry : list) {
            if (entry.filterPeriod != null && entry.filterPeriod.contains(presentationTimeMs)) {
                double scale = entry.filterPeriod.filter.resolveTimeScaleAtMs(presentationTimeMs);
                if (Math.abs(scale - 1.0d) > 1e-9) {
                    return scale;
                }
            }
        }
        return 1.0d;
    }

    @Override
    public boolean hasTimeScaleControl() {
        for (final GlFilterEntry entry : list) {
            if (entry.filterPeriod != null && entry.filterPeriod.filter.hasTimeScaleControl()) {
                return true;
            }
        }
        return false;
    }

    public void add(GlFilter filter) {
        // 添加filter的话，默认是全时长段生效
        filterPeriods.add(new GlFilterPeriod(0, Long.MAX_VALUE, filter));
    }

    public void add(GlFilterPeriod filterPeriod) {
        filterPeriods.add(filterPeriod);
    }

}
