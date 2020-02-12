package com.shangame.fiction.ui.reader;

public class ReadParameter {

    private static ReadParameter instance = new ReadParameter();

    private boolean isResume = false;

    private ReadParameter() {
    }

    public static ReadParameter getInstance() {
        return instance;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }
}
