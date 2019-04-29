package com.mhr.housekeeping.utils;

public class MyException extends RuntimeException {
    MyException(String message) {
        super(message);
    }
    public String toString(){
        return "异常[$message]";
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
