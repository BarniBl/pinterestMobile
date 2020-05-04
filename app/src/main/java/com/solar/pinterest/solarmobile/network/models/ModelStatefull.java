package com.solar.pinterest.solarmobile.network.models;

public class ModelStatefull {
    private State mState = State.EMPTY;

    public State getState() {
        return mState;
    }

    public enum State {
        EMPTY,
        IN_PROGRESS,
        SUCCESS,
        FAILED,
    }

}
