package com.example.algorithm_visualizer;

public class MessageModelClass {
    String mText;
    boolean isSentByUser;

    public MessageModelClass(String mText, boolean isSentByUser) {
        this.mText = mText;
        this.isSentByUser = isSentByUser;
    }

    public String getmText() {
        return mText;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }


}
