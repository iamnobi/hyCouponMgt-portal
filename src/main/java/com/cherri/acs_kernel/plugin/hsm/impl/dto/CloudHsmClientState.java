package com.cherri.acs_kernel.plugin.hsm.impl.dto;

public class CloudHsmClientState {
    enum State {
        // 登入中
        LOGINING_IN,
        LOGIN_FAILED,
        NORMAL
    }

    private State state = State.LOGINING_IN;

    private synchronized State getState() {
        return state;
    }

    /** check if state is LOGIN_FAILED, and if is LOGIN_FAILED change the state to LOGINING_IN */
    public synchronized boolean checkIsLoginFailedAndTryLoginIn() {
        if (getState() != State.LOGIN_FAILED) {
            return false;
        } else {
            state = State.LOGINING_IN;
            return true;
        }
    }

    public synchronized boolean isLoginFailed() {
        return getState() == State.LOGIN_FAILED;
    }

    public synchronized boolean isLoginingIn() {
        return getState() == State.LOGINING_IN;
    }

    public synchronized boolean isNormal() {
        return getState() == State.NORMAL;
    }

    /** @return 若已經是 LOGINING_IN, 回傳 false, 否則回傳 true */
    public synchronized boolean setStateLoginingIn() {
        if (getState() == State.LOGINING_IN) {
            return false;
        } else {
            state = State.LOGINING_IN;
            return true;
        }
    }

    public synchronized void setStateNormal() {
        state = State.NORMAL;
    }

    public synchronized void setStateLoginFailed() {
        state = State.LOGIN_FAILED;
    }

    @Override
    public String toString() {
        return "CloudHsmClientState{" +
            "state=" + state +
            '}';
    }
}
