package com.team.gptrecipie.api.completion;

import java.util.List;

public class RequestCompletion {
    private List<ChatMessage> messages;
    private String model;
    private String functionCall;
    private String user;

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFunctionCall() {
        return functionCall;
    }

    public void setFunctionCall(String functionCall) {
        this.functionCall = functionCall;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
