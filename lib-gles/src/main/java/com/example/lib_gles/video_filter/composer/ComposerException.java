package com.example.lib_gles.video_filter.composer;

class ComposerException extends RuntimeException {
    private final int errorCode;

    ComposerException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    ComposerException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    ComposerException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    int getErrorCode() {
        return errorCode;
    }
}
