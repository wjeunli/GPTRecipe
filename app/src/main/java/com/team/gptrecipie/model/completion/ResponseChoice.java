package com.team.gptrecipie.model.completion;

public class ResponseChoice {
    private int mIndex;
    private String finishReason;
    private ChatMessage mMessages;

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public ChatMessage getMessages() {
        return mMessages;
    }

    public void setMessages(ChatMessage messages) {
        mMessages = messages;
    }
}
