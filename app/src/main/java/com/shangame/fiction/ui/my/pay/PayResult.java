package com.shangame.fiction.ui.my.pay;

public class PayResult {
    private boolean isPaySuccess = false;

    private static PayResult instance = new PayResult();

    private PayResult() {
    }

    public static PayResult getInstance() {
        return instance;
    }

    public boolean isPaySuccess() {
        return isPaySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        isPaySuccess = paySuccess;
    }
}
