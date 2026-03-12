package com.example.lib_gles.video_filter.composer;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lib_gles.video_filter.core.bean.FillMode;
import com.example.lib_gles.video_filter.core.bean.FillModeCustomItem;
import com.example.lib_gles.video_filter.core.bean.Resolution;
import com.example.lib_gles.video_filter.core.bean.Rotation;
import com.example.lib_gles.video_filter.core.filter.GlFilter;
import com.example.lib_gles.video_filter.core.filter.GlFilterList;
import com.example.lib_gles.video_filter.core.filter.IResolutionFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class Mp4Composer {

    private final static String TAG = Mp4Composer.class.getSimpleName();
    private static final int DEFAULT_AUDIO_BITRATE = 64_000;
    private static final int MIN_VIDEO_BITRATE = 750_000;
    private static final int MAX_VIDEO_BITRATE = 3_500_000;
    private static final double DEFAULT_BITS_PER_PIXEL = 0.10;

    public enum AudioMode {
        ORIGINAL,
        REPLACE,
        MIX
    }

    private final String srcPath;
    private final String destPath;
    private GlFilter filter;
    private GlFilterList filterList;
    private Resolution outputResolution;
    private int bitrate = -1;
    private int frameRate = 30;
    private boolean mute = false;
    private Rotation rotation = Rotation.NORMAL;
    private Listener listener;
    private FillMode fillMode = FillMode.PRESERVE_ASPECT_FIT;
    private FillModeCustomItem fillModeCustomItem;
    private double timeScale = 1;
    private float resolutionScale = 1f;
    private long clipStartMs, clipEndMs;
    private boolean flipVertical = false;
    private boolean flipHorizontal = false;
    private String audioPath;
    private AudioMode audioMode = AudioMode.ORIGINAL;
    private int audioBitrate = DEFAULT_AUDIO_BITRATE;

    private ExecutorService executorService;
    private final Object stateLock = new Object();
    private volatile boolean pauseRequested = false;
    private volatile boolean cancelRequested = false;
    private final AtomicBoolean terminalNotified = new AtomicBoolean(false);


    public Mp4Composer(@NonNull final String srcPath, @NonNull final String destPath) {
        this.srcPath = srcPath;
        this.destPath = destPath;
    }

    public Mp4Composer filter(@NonNull GlFilter filter) {
        this.filter = filter;
        return this;
    }

    public Mp4Composer filterList(@NonNull GlFilterList filterList) {
        this.filterList = filterList;
        Log.d(TAG, "set filterList = " + this.filterList);
        return this;
    }

    public Mp4Composer size(int width, int height) {
        this.outputResolution = new Resolution(width, height);
        return this;
    }

    public Mp4Composer clip(long start, long end) {
        this.clipStartMs = start;
        this.clipEndMs = end;
        return this;
    }

    public Mp4Composer videoBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public Mp4Composer mute(boolean mute) {
        this.mute = mute;
        return this;
    }

    public Mp4Composer frameRate(int value) {
        this.frameRate = value;
        return this;
    }

    public Mp4Composer flipVertical(boolean flipVertical) {
        this.flipVertical = flipVertical;
        return this;
    }

    public Mp4Composer flipHorizontal(boolean flipHorizontal) {
        this.flipHorizontal = flipHorizontal;
        return this;
    }

    public Mp4Composer rotation(@NonNull Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public Mp4Composer fillMode(@NonNull FillMode fillMode) {
        this.fillMode = fillMode;
        return this;
    }

    public Mp4Composer customFillMode(@NonNull FillModeCustomItem fillModeCustomItem) {
        this.fillModeCustomItem = fillModeCustomItem;
        this.fillMode = FillMode.CUSTOM;
        return this;
    }


    public Mp4Composer listener(@NonNull Listener listener) {
        this.listener = listener;
        return this;
    }

    public Mp4Composer timeScale(final double timeScale) {
        this.timeScale = timeScale;
        return this;
    }

    public Mp4Composer audioPath(@NonNull String audioPath) {
        this.audioPath = audioPath;
        return this;
    }

    public Mp4Composer audioMode(@NonNull AudioMode audioMode) {
        this.audioMode = audioMode;
        return this;
    }

    public Mp4Composer audioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate > 0 ? audioBitrate : DEFAULT_AUDIO_BITRATE;
        return this;
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }


    public Mp4Composer start() {
        synchronized (stateLock) {
            pauseRequested = false;
            cancelRequested = false;
        }
        terminalNotified.set(false);
        getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Mp4ComposerEngine engine = new Mp4ComposerEngine();
                    engine.setControl(new Mp4ComposerEngine.Control() {
                        @Override
                        public void awaitIfPaused() throws InterruptedException {
                            synchronized (stateLock) {
                                while (pauseRequested && !cancelRequested) {
                                    stateLock.wait();
                                }
                            }
                        }

                        @Override
                        public boolean isCancelRequested() {
                            return cancelRequested;
                        }
                    });

                    engine.setProgressCallback(new Mp4ComposerEngine.ProgressCallback() {
                        @Override
                        public void onProgress(final double progress) {
                            if (listener != null) {
                                listener.onProgress(progress);
                            }
                        }
                    });

                    File outFile = new File(destPath);
                    if (outFile.exists()) {
                        outFile.delete();
                    }

                    final File srcFile = new File(srcPath);
                    final FileInputStream fileInputStream;
                    try {
                        fileInputStream = new FileInputStream(srcFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        notifyFailed(listener, e);
                        return;
                    }

                    try {
                        engine.setDataSource(fileInputStream.getFD());
                    } catch (IOException e) {
                        e.printStackTrace();
                        notifyFailed(listener, e);
                        return;
                    }

                    final int videoRotate = getVideoRotation(srcPath);
                    final Resolution srcVideoResolution;
                    try {
                        srcVideoResolution = getVideoResolution(srcPath, videoRotate);
                    } catch (IOException e) {
                        e.printStackTrace();
                        notifyFailed(listener, e);
                        return;
                    }

                    if (filter == null) {
                        filter = new GlFilter();
                    }

                    if (fillMode == null) {
                        fillMode = FillMode.PRESERVE_ASPECT_FIT;
                    }

                    if (fillModeCustomItem != null) {
                        fillMode = FillMode.CUSTOM;
                    }

                    Rotation outputRotation = Rotation.fromInt(rotation.getRotation() + videoRotate);
                    Resolution effectiveSourceResolution;
                    if (outputRotation == Rotation.ROTATION_90 || outputRotation == Rotation.ROTATION_270) {
                        effectiveSourceResolution = new Resolution(srcVideoResolution.height(), srcVideoResolution.width());
                    } else {
                        effectiveSourceResolution = srcVideoResolution;
                    }

                    if (outputResolution == null) {
                        if (fillMode == FillMode.CUSTOM) {
                            outputResolution = effectiveSourceResolution;
                        } else {
                            outputResolution = effectiveSourceResolution;
                        }
                    } else {
                        outputResolution = new Resolution(
                                Math.min(outputResolution.width(), effectiveSourceResolution.width()),
                                Math.min(outputResolution.height(), effectiveSourceResolution.height())
                        );
                    }
                    if (filter instanceof IResolutionFilter) {
                        ((IResolutionFilter) filter).setResolution(outputResolution);
                    }

//                    if (timeScale < 2) {
//                        timeScale = 1;
//                    }

                    Log.d(TAG, "filterList = " + filterList);
                    Log.d(TAG, "rotation = " + (rotation.getRotation() + videoRotate));
                    Log.d(TAG, "inputResolution width = " + srcVideoResolution.width() + " height = " + srcVideoResolution.height());
                    outputResolution = new Resolution((int) (outputResolution.width() * resolutionScale), (int) (outputResolution.height() * resolutionScale));
                    Log.d(TAG, "outputResolution width = " + outputResolution.width() + " height = " + outputResolution.height());
                    Log.d(TAG, "fillMode = " + fillMode);

                    if (bitrate < 0) {
                        int sourceBitrate = getSourceBitrate(srcPath);
                        bitrate = calcBitRate(
                                outputResolution.width(),
                                outputResolution.height(),
                                srcVideoResolution.width(),
                                srcVideoResolution.height(),
                                sourceBitrate,
                                frameRate
                        );
                    }
                    engine.compose(
                            destPath,
                            outputResolution,
                            filter,
                            filterList,
                            bitrate,
                            frameRate,
                            mute,
                            outputRotation,
                            srcVideoResolution,
                            fillMode,
                            fillModeCustomItem,
                            timeScale,
                            flipVertical,
                            flipHorizontal,
                            clipStartMs,
                            clipEndMs,
                            audioPath,
                            audioMode,
                            audioBitrate
                    );

                    if (cancelRequested) {
                        if (listener != null && terminalNotified.compareAndSet(false, true)) {
                            listener.onCanceled();
                        }
                    } else if (listener != null && terminalNotified.compareAndSet(false, true)) {
                        listener.onCompleted();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    if (listener != null && terminalNotified.compareAndSet(false, true)) {
                        listener.onCanceled();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null && !cancelRequested && terminalNotified.compareAndSet(false, true)) {
                        listener.onFailed(e, ErrorCode.fromException(e));
                    } else if (listener != null && cancelRequested && terminalNotified.compareAndSet(false, true)) {
                        listener.onCanceled();
                    }
                } finally {
                    if (executorService != null) {
                        executorService.shutdown();
                        executorService = null;
                    }
                }
            }
        });

        return this;
    }

    public void pause() {
        synchronized (stateLock) {
            if (cancelRequested) return;
            pauseRequested = true;
        }
    }

    public void resume() {
        synchronized (stateLock) {
            pauseRequested = false;
            stateLock.notifyAll();
        }
    }

    public boolean isPaused() {
        return pauseRequested;
    }

    public void cancel() {
        synchronized (stateLock) {
            cancelRequested = true;
            pauseRequested = false;
            stateLock.notifyAll();
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }


    public interface Listener {
        /**
         * Called to notify progress.
         *
         * @param progress Progress in [0.0, 1.0] range, or negative value if progress is unknown.
         */
        void onProgress(double progress);

        /**
         * Called when transcode completed.
         */
        void onCompleted();

        /**
         * Called when transcode canceled.
         */
        void onCanceled();


        void onFailed(Exception exception, int errorCode);
    }

    private void notifyFailed(Listener listener, Exception exception) {
        if (listener != null && !cancelRequested && terminalNotified.compareAndSet(false, true)) {
            listener.onFailed(exception, ErrorCode.fromException(exception));
        }
    }

    private int getVideoRotation(String videoFilePath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(videoFilePath);
        } catch (Exception e) {
            Log.e("MediaMetadataRetriever", "getVideoRotation error");
            return 0;
        }
        String orientation = mediaMetadataRetriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        return Integer.valueOf(orientation);
    }

    private int calcBitRate(int width, int height, int srcWidth, int srcHeight, int sourceBitrate, int frameRate) {
        int safeFrameRate = frameRate > 0 ? frameRate : 30;
        int heuristicBitrate = (int) (DEFAULT_BITS_PER_PIXEL * safeFrameRate * width * height);
        int bitrate = heuristicBitrate;

        if (sourceBitrate > 0 && srcWidth > 0 && srcHeight > 0) {
            double pixelRatio = (double) width * height / ((double) srcWidth * srcHeight);
            double normalizedPixelRatio = Math.max(0.5d, Math.min(pixelRatio, 1.0d));
            int sourceAdjustedBitrate = (int) (sourceBitrate * normalizedPixelRatio);
            bitrate = Math.min(heuristicBitrate, sourceAdjustedBitrate);
        }

        bitrate = Math.max(MIN_VIDEO_BITRATE, Math.min(bitrate, MAX_VIDEO_BITRATE));
        Log.i(TAG, "bitrate=" + bitrate);
        return bitrate;
    }

    private int getSourceBitrate(String videoFilePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoFilePath);
            String bitrateValue = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            if (bitrateValue == null) {
                return -1;
            }
            return Integer.parseInt(bitrateValue);
        } catch (Exception e) {
            Log.w(TAG, "Failed to read source bitrate.", e);
            return -1;
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ignore) {
            }
        }
    }

    private Resolution getVideoResolution(final String path, final int rotation) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        retriever.release();

        return new Resolution(width, height);
    }

}
