package com.team.gptrecipie.model.completion;

public class ChatMessage {
    private String mRole;
    private String mContent;

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
