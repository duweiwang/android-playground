package com.example.lib_gles.video_filter.composer;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class ErrorCode {
    public static final int UNKNOWN = -1;
    public static final int INVALID_INPUT = 1001;
    public static final int OUTPUT_PATH_INVALID = 1002;
    public static final int FILE_NOT_FOUND = 1003;
    public static final int IO = 1004;
    public static final int NO_VIDEO_TRACK = 1005;
    public static final int NO_AUDIO_TRACK = 1006;
    public static final int INVALID_AUDIO_MIME = 1007;
    public static final int UNSUPPORTED_AUDIO_FORMAT = 1008;
    public static final int CODEC_INIT = 1009;
    public static final int OUTPUT_FORMAT = 1010;
    public static final int MUXER = 1011;
    public static final int UNSUPPORTED_MIX_AUDIO_TIME_SCALE = 1012;

    private ErrorCode() {
    }

    public static int fromException(Throwable throwable) {
        if (throwable instanceof ComposerException) {
            return ((ComposerException) throwable).getErrorCode();
        }
        if (throwable instanceof FileNotFoundException) {
            return FILE_NOT_FOUND;
        }
        if (throwable instanceof IOException) {
            return IO;
        }
        if (throwable instanceof UnsupportedOperationException) {
            return UNSUPPORTED_AUDIO_FORMAT;
        }
        if (throwable instanceof IllegalArgumentException) {
            String message = throwable.getMessage();
            if (message == null) {
                return INVALID_INPUT;
            }
            if (message.contains("No video track")) {
                return NO_VIDEO_TRACK;
            }
            if (message.contains("No audio track")) {
                return NO_AUDIO_TRACK;
            }
            if (message.contains("Invalid") && message.contains("audio mime")) {
                return INVALID_AUDIO_MIME;
            }
            if (message.contains("timeScale >= 2")) {
                return UNSUPPORTED_MIX_AUDIO_TIME_SCALE;
            }
            return INVALID_INPUT;
        }
        return UNKNOWN;
    }
}
