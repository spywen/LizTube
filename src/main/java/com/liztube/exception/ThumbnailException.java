package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

import java.util.List;

/**
 * Thumbnail exception
 */
public class ThumbnailException extends PublicException {
    public ThumbnailException(String log) {
        super(log);
    }

    public ThumbnailException(String log, List<String> messages) {
        super(log, messages);
    }

    public ThumbnailException(String log, String message) {
        super(log, message);
    }
}
