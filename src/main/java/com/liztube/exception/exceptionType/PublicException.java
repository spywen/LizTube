package com.liztube.exception.exceptionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class exception which be raised to users
 */
public class PublicException extends InternalException {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //region attributes
    private List<String> messages = new ArrayList<>();
    //endregion

    //region Constructor of the customize base exception
    public PublicException(String log){

        super.log(log);
        addMessage("An unexpected error occured. If the problem persists please contact the administrator.");
        logException();
    }

    public PublicException(String log, List<String> messages) {
        super.log(log);
        this.messages = messages;
        logException();
    }

    public PublicException(String log, String message) {
        super.log(log);
        this.addMessage(message);
        logException();
    }
    //endregion

    //region getter/setter
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    public void addMessage(String message){
        this.messages.add(message);
    }
    //endregion

    private void logException(){
        StringBuilder builder = new StringBuilder();
        for(String message : messages){
            builder.append(message);
        }
        /* With JDK 8
        messages.forEach(builder::append);
         */
        logger.error(super.log() + ", Messages: " + builder.toString(), this);
    }

}
