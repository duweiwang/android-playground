package com.example.lib_gles.video_filter.composer;

import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.util.Log;

import com.example.lib_gles.video_filter.core.bean.FillMode;
import com.example.lib_gles.video_filter.core.bean.FillModeCustomItem;
import com.example.lib_gles.video_filter.core.bean.Resolution;
import com.example.lib_gles.video_filter.core.bean.Rotation;
import com.example.lib_gles.video_filter.core.filter.GlFilter;
import com.example.lib_gles.video_filter.core.filter.GlFilterList;

import java.io.FileDescriptor;
import java.io.IOException;

// Refer: https://github.com/ypresto/android-transcoder/blob/master/lib/src/main/java/net/ypresto/androidtranscoder/engine/MediaTranscoderEngine.java

/**
 * Internal engine, do not use this directly.
 */
class Mp4ComposerEngine {
    private static final String TAG = "Mp4ComposerEngine";
    private static final double PROGRESS_UNKNOWN = -1.0;
    private static final long SLEEP_TO_WAIT_TRACK_TRANSCODERS = 10;
    private static final long PROGRESS_INTERVAL_STEPS = 10;
    private FileDescriptor inputFileDescriptor;
    private VideoComposer videoComposer;
    private IAudioComposer audioComposer;
    private MediaExtractor mediaExtractor;
    private MediaMuxer mediaMuxer;
    private ProgressCallback progressCallback;
    private long durationUs;
    private Control control;


    void setDataSource(FileDescriptor fileDescriptor) {
        inputFileDescriptor = fileDescriptor;
    }

    void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    void setControl(Control control) {
        this.control = control;
    }


    void compose(
            final String destPath,
            final Resolution outputResolution,
            final GlFilter filter,
            final GlFilterList filterList,
            final int bitrate,
            final int frameRate,
            final boolean mute,
            final Rotation rotation,
            final Resolution inputResolution,
            final FillMode fillMode,
            final FillModeCustomItem fillModeCustomItem,
            final double timeScale,
            final boolean flipVertical,
            final boolean flipHorizontal,
            final long startTimeMs,
            final long endTimeMs,
            final String audioPath,
            final Mp4Composer.AudioMode audioMode,
            final int audioBitrate
    ) throws IOException, InterruptedException {


        try {
            mediaExtractor = new MediaExtractor();
            mediaExtractor.setDataSource(inputFileDescriptor);
            mediaMuxer = new MediaMuxer(destPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(inputFileDescriptor);
            MediaFormat videoOutputFormat = MediaFormat.createVideoFormat("video/avc", outputResolution.width(), outputResolution.height());

            videoOutputFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
            videoOutputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
            videoOutputFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            videoOutputFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);


            // identify track indices by MIME type instead of assuming 0/1 order
            int detectedVideoTrackIndex = -1;
            int detectedAudioTrackIndex = -1;
            int trackCount = mediaExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                String mime = trackFormat.getString(MediaFormat.KEY_MIME);
                if (mime == null) {
                    continue;
                }
                if (detectedVideoTrackIndex < 0 && mime.startsWith("video/")) {
                    detectedVideoTrackIndex = i;
                } else if (detectedAudioTrackIndex < 0 && mime.startsWith("audio/")) {
                    detectedAudioTrackIndex = i;
                }
            }
            final int videoTrackIndex = detectedVideoTrackIndex;
            final int audioTrackIndex = detectedAudioTrackIndex;
            if (videoTrackIndex < 0) {
                throw new ComposerException(ErrorCode.NO_VIDEO_TRACK, "No video track found in input source.");
            }

            long sourceVideoDurationUs = getTrackDurationUs(mediaExtractor.getTrackFormat(videoTrackIndex));
            if (startTimeMs >= 0 && endTimeMs > startTimeMs) {
                durationUs = (endTimeMs - startTimeMs) * 1000L;
            } else if (sourceVideoDurationUs > 0) {
                durationUs = sourceVideoDurationUs;
            } else {
                try {
                    durationUs = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000L;
                } catch (NumberFormatException e) {
                    durationUs = -1;
                }
            }

            Log.d(TAG, "Duration (us): " + durationUs);

            boolean hasAudio = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) != null
                    && audioTrackIndex >= 0;
            boolean useExternalAudio = audioPath != null && audioPath.length() > 0
                    && audioMode != null
                    && audioMode != Mp4Composer.AudioMode.ORIGINAL;
            boolean audioTrackRequired = !mute && (hasAudio || useExternalAudio);

            MuxRender muxRender = new MuxRender(mediaMuxer, audioTrackRequired);

            // setup video composer
            videoComposer = new VideoComposer(mediaExtractor, videoTrackIndex, videoOutputFormat, muxRender, timeScale);
            videoComposer.setUp(filter, filterList, rotation, outputResolution, inputResolution, fillMode, fillModeCustomItem, flipVertical, flipHorizontal);
            mediaExtractor.selectTrack(videoTrackIndex);

            if (startTimeMs >= 0 && endTimeMs > startTimeMs) {
                videoComposer.setClipRange(startTimeMs, endTimeMs);
            }


            // setup audio if present and not muted

            if (!mute && (hasAudio || useExternalAudio)) {
                if (useExternalAudio && audioMode == Mp4Composer.AudioMode.REPLACE) {
                    audioComposer = new ExternalRemixAudioComposer(audioPath, muxRender, durationUs, audioBitrate);
                } else if (useExternalAudio && audioMode == Mp4Composer.AudioMode.MIX) {
                    if (!hasAudio) {
                        audioComposer = new ExternalRemixAudioComposer(audioPath, muxRender, durationUs, audioBitrate);
                    } else {
                        if (timeScale >= 2) {
                            throw new ComposerException(ErrorCode.UNSUPPORTED_MIX_AUDIO_TIME_SCALE, "Mix audio does not support timeScale >= 2.");
                        }
                        audioComposer = new MixAudioComposer(inputFileDescriptor, audioTrackIndex, audioPath, muxRender, durationUs, startTimeMs, endTimeMs, audioBitrate);
                    }
                } else if (hasAudio) {
                    // original audio
                    if (timeScale < 2) {
                        audioComposer = new AudioComposer(mediaExtractor, audioTrackIndex, muxRender, durationUs);
                        if (startTimeMs >= 0 && endTimeMs > startTimeMs) {
                            ((AudioComposer) audioComposer).setClipRange(startTimeMs, endTimeMs);
                        }
                    } else {
                        audioComposer = new RemixAudioComposer(mediaExtractor, audioTrackIndex, mediaExtractor.getTrackFormat(audioTrackIndex), muxRender, timeScale);
                    }
                }

                if (audioComposer != null) {
                    audioComposer.setup();
                    if (audioComposer instanceof AudioComposer || audioComposer instanceof RemixAudioComposer) {
                        mediaExtractor.selectTrack(audioTrackIndex);
                    }
                    runPipelines();
                } else {
                    runPipelinesNoAudio();
                }
            } else {
                // no audio video
                runPipelinesNoAudio();
            }
            mediaMuxer.stop();
        } finally {
            try {
                if (videoComposer != null) {
                    videoComposer.release();
                    videoComposer = null;
                }
                if (audioComposer != null) {
                    audioComposer.release();
                    audioComposer = null;
                }
                if (mediaExtractor != null) {
                    mediaExtractor.release();
                    mediaExtractor = null;
                }
            } catch (RuntimeException e) {
                // Too fatal to make alive the app, because it may leak native resources.
                //noinspection ThrowFromFinallyBlock
                throw new Error("Could not shutdown mediaExtractor, codecs and mediaMuxer pipeline.", e);
            }
            try {
                if (mediaMuxer != null) {
                    mediaMuxer.release();
                    mediaMuxer = null;
                }
            } catch (RuntimeException e) {
                Log.e(TAG, "Failed to release mediaMuxer.", e);
            }
        }


    }


    private void runPipelines() throws InterruptedException {
        long loopCount = 0;
        if (durationUs <= 0) {
            if (progressCallback != null) {
                progressCallback.onProgress(PROGRESS_UNKNOWN);
            }// unknown
        }
        while (!(videoComposer.isFinished() && audioComposer.isFinished())) {
            checkPausedOrCanceled();
            boolean stepped = videoComposer.stepPipeline()
                    || audioComposer.stepPipeline();
            loopCount++;
            if (durationUs > 0 && loopCount % PROGRESS_INTERVAL_STEPS == 0) {
                double videoProgress = videoComposer.isFinished() ? 1.0 : Math.min(1.0, (double) videoComposer.getWrittenPresentationTimeUs() / durationUs);
                double audioProgress = audioComposer.isFinished() ? 1.0 : Math.min(1.0, (double) audioComposer.getWrittenPresentationTimeUs() / durationUs);
                double progress = (videoProgress + audioProgress) / 2.0;
                if (progressCallback != null) {
                    progressCallback.onProgress(progress);
                }
            }
            if (!stepped) {
                try {
                    Thread.sleep(SLEEP_TO_WAIT_TRACK_TRANSCODERS);
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }
    }

    private void runPipelinesNoAudio() throws InterruptedException {
        long loopCount = 0;
        if (durationUs <= 0) {
            if (progressCallback != null) {
                progressCallback.onProgress(PROGRESS_UNKNOWN);
            } // unknown
        }
        while (!videoComposer.isFinished()) {
            checkPausedOrCanceled();
            boolean stepped = videoComposer.stepPipeline();
            loopCount++;
            if (durationUs > 0 && loopCount % PROGRESS_INTERVAL_STEPS == 0) {
                double videoProgress = videoComposer.isFinished() ? 1.0 : Math.min(1.0, (double) videoComposer.getWrittenPresentationTimeUs() / durationUs);
                if (progressCallback != null) {
                    progressCallback.onProgress(videoProgress);
                }
            }
            if (!stepped) {
                try {
                    Thread.sleep(SLEEP_TO_WAIT_TRACK_TRANSCODERS);
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }


    }

    private void checkPausedOrCanceled() throws InterruptedException {
        if (control == null) {
            return;
        }
        if (control.isCancelRequested()) {
            throw new InterruptedException("compose canceled");
        }
        control.awaitIfPaused();
        if (control.isCancelRequested()) {
            throw new InterruptedException("compose canceled");
        }
    }

    private static long getTrackDurationUs(MediaFormat format) {
        if (format != null && format.containsKey(MediaFormat.KEY_DURATION)) {
            try {
                return format.getLong(MediaFormat.KEY_DURATION);
            } catch (RuntimeException ignore) {
                // fall through
            }
        }
        return -1L;
    }


    interface ProgressCallback {
        /**
         * Called to notify progress. Same thread which initiated transcode is used.
         *
         * @param progress Progress in [0.0, 1.0] range, or negative value if progress is unknown.
         */
        void onProgress(double progress);
    }

    interface Control {
        void awaitIfPaused() throws InterruptedException;

        boolean isCancelRequested();
    }
}
