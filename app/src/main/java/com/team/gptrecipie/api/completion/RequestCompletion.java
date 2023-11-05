package com.team.gptrecipie.api.completion;

import java.util.List;

public class RequestCompletion {
    private List<ChatMessage> mMessages;
    private String mModel;
    private float mFrequencyPenalty;
    private String mFunctionCall;
    private String mUser;

    public List<ChatMessage> getMessages() {
        return mMessages;
    }

    public void setMessages(List<ChatMessage> messages) {
        mMessages = messages;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public float getFrequencyPenalty() {
        return mFrequencyPenalty;
    }

    public void setFrequencyPenalty(float frequencyPenalty) {
        mFrequencyPenalty = frequencyPenalty;
    }

    public String getFunctionCall() {
        return mFunctionCall;
    }

    public void setFunctionCall(String functionCall) {
        mFunctionCall = functionCall;
    }

    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }
}
