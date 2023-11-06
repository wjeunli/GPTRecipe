package com.team.gptrecipie.service;

import android.util.Log;

import com.team.gptrecipie.api.ChatGPTService;
import com.team.gptrecipie.api.completion.ChatMessage;
import com.team.gptrecipie.api.completion.ResponseChoice;
import com.team.gptrecipie.api.completion.ResponseCompletion;
import com.team.gptrecipie.model.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GPTGenerateService {
    private static final String LOG_TAG = "GPTGenerateService";
    private ChatGPTService mChatGPTService = ChatGPTService.getInstance();

    private static final String TEMPLATE = "I have %s ingredients, please give me a delicious recipe restricted to the ingredient";
    public String generateContent(Recipe recipe) {
        // TODO ingredient can be verified and rechecked
        List<ChatMessage> requests = new ArrayList();
        ChatMessage message = new ChatMessage();
        message.setRole("user");
        message.setContent(String.format(TEMPLATE, recipe.getIngredient()));
        requests.add(message);

        try {
            Callback<ResponseCompletion> callback = new Callback<ResponseCompletion>() {
                @Override
                public void onResponse(Call<ResponseCompletion> call, Response<ResponseCompletion> response) {
                    if (response.isSuccessful()) {
                        ResponseCompletion chatResponse = response.body();
                        List<ResponseChoice> choice = chatResponse.getChoices();
                        if (choice!=null && !choice.isEmpty()) {
                            String content = choice.get(0).getMessage().getContent();
                            recipe.setContent(content);
                        }
                    } else {
                        Log.e(LOG_TAG,"ERROR respons un successful");
                    }
                }

                @Override
                public void onFailure(Call<ResponseCompletion> call, Throwable t) {
                    Log.e(LOG_TAG,"Request Failed", t);
                }
            };

            mChatGPTService.defaultChatRequest(requests, callback);
            /*if (rets != null & rets.size() > 0) {
                return rets.get(0).getContent();
            }*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
