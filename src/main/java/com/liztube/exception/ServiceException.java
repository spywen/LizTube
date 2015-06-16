package com.liztube.exception;

import com.liztube.exception.exceptionType.PublicException;

/**
 * Generic service exception : if any exception is catch on the service layer. A service exception will be raised
 */
public class ServiceException extends PublicException {
    public ServiceException(String log) {
        super(log);
    }
}