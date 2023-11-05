package com.team.gptrecipie.service;

import com.team.gptrecipie.api.ChatGPTService;
import com.team.gptrecipie.api.completion.ChatMessage;
import com.team.gptrecipie.model.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPTGenerateService {
    private ChatGPTService mChatGPTService = ChatGPTService.getInstance();

    private static final String TEMPLATE = "I have %s ingredients, please give me a delicious recipe restricted to the ingredient";
    public String generateContent(Recipe recipe) {
        // TODO ingredient can be verified and rechecked
        List<ChatMessage> requests = new ArrayList();
        ChatMessage message = new ChatMessage();
        message.setRole("cook");
        message.setContent(String.format(TEMPLATE, recipe.getIngredient()));
        requests.add(message);

        try {
            List<ChatMessage> rets = mChatGPTService.defaultChatRequest(requests);
            if (rets != null & rets.size() > 0) {
                return rets.get(0).getContent();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
