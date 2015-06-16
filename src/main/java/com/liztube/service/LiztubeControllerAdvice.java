package com.liztube.service;

import com.liztube.exception.*;
import com.liztube.exception.exceptionType.ExceptionForAdvice;
import com.liztube.exception.exceptionType.PublicException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice. In case of an exception occured : a clean message will be send through the api.
 */
@ControllerAdvice
public class LiztubeControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ExceptionForAdvice userNotFoundException(UserNotFoundException userNotFoundException){
        return new ExceptionForAdvice(userNotFoundException);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SigninException.class)
    @ResponseBody
    public ExceptionForAdvice signInException(SigninException signinException){
        return new ExceptionForAdvice(signinException);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VideoException.class)
    @ResponseBody
    public ExceptionForAdvice videoException(VideoException videoException){
        return new ExceptionForAdvice(videoException);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ThumbnailException.class)
    @ResponseBody
    public ExceptionForAdvice thumbnailException(ThumbnailException thumbnailException){
        return new ExceptionForAdvice(thumbnailException);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ExceptionForAdvice userException(UserException userException){
        return new ExceptionForAdvice(userException);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ExceptionForAdvice serviceException(ServiceException serviceException){
        return new ExceptionForAdvice(serviceException);
    }

}
