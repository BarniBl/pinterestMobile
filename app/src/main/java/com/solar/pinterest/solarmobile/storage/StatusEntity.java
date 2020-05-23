package com.solar.pinterest.solarmobile.storage;

public class StatusEntity{
    private String mMessage;
    private Status mState;

    public StatusEntity(Status status, String message) {
        mMessage = message;
        mState = status;
    }

    public StatusEntity(Status status) {
        mMessage = "";
        mState = status;
    }

    public StatusEntity() {
        mMessage = "";
        mState = Status.EMPTY;
    }

    public Status getStatus() {
        return mState;
    }

    public void setStatus(Status state) {
        mState = state;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public enum Status {
        EMPTY,
        IN_PROGRESS,
        SUCCESS,
        FAILED,
        TIMEOUT,
        EXISTS,
        NOT_EXISTS,
        NO_PERMISSION,
    }
}
