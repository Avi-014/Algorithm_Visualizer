package com.example.algorithm_visualizer.Models;

public class BotMessageModelClass {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT = "bot";

    public BotMessageModelClass() {
    }

    public BotMessageModelClass(String message, String sentBy) {
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
