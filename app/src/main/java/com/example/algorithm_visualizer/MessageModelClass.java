package com.example.algorithm_visualizer;

public class MessageModelClass {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    public MessageModelClass() {
    }

    public MessageModelClass(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    String message;
    String sentBy;

}
