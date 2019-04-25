package com.tany.base.net.exception;


public class CCHttpException {
    public String mCode;
    public String mMessage;

    public CCHttpException(String code, String message) {
        this.mCode = code;
        this.mMessage = message;
    }
}
