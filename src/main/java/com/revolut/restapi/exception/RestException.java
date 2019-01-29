package com.revolut.restapi.exception;

/**
 * Created by ayomide on 1/28/2019.
 */
public class RestException extends Throwable{

    private int code;
    public RestException(String message) {
        this(500);
    }
    public RestException(int code) {
        this(code, "Error while processing the request", null);
    }
    public RestException(int code, String message) {
        this(code, message, null);
    }
    public RestException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}
