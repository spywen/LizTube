package com.liztube.exception.exceptionType;

/**
 * Internal exception which be not raised to users
 */
public class InternalException extends Exception {

    //region attributes
    private String log;
    //endregion

    //region constructor
    public InternalException(){
        super();
    }
    /**
     * An internal exception expects a string log for log files
     * @param log
     */
    public InternalException(String log){
        super();
        this.log = log;
    }
    //endregion

    //region getter/setter
    public String log() {
        return log;
    }

    public void log(String log) {
        this.log = log;
    }
    //endregion

}
