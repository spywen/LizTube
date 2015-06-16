package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

import java.util.List;

/**
 * Video exception
 */
public class VideoException extends PublicException {
    public VideoException(String log) {
        super(log);
    }

    public VideoException(String log, List<String> messages) {
        super(log, messages);
    }

    public VideoException(String log, String message) {
        super(log, message);
    }
}
