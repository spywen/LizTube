package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

import java.util.List;

/**
 * Created by laurent on 10/06/15.
 */
public class PathException extends PublicException {

    public PathException(String log) {
        super(log);
    }

    public PathException(String log, List<String> messages) {
        super(log, messages);
    }

    public PathException(String log, String message) {
        super(log, message);
    }
}
