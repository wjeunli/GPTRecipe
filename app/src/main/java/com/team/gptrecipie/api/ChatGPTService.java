package com.team.gptrecipie.api;

import com.team.gptrecipie.api.completion.ChatMessage;
import com.team.gptrecipie.api.completion.RequestCompletion;
import com.team.gptrecipie.api.completion.ResponseChoice;
import com.team.gptrecipie.api.completion.ResponseCompletion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatGPTService {

    private static final String GPT_KEY = "sk-UWjMmHr3qa9if5MN8q4bWeAmUak2gQ3SuwqVLZizI5dAYFRC";
    private static final String GPT_URL = "https://api.chatanywhere.cn/v1/";
    private static ChatGPTService instance;
    private Retrofit mRetrofit;
    private ChatGptApi service;


    public static ChatGPTService getInstance() {
        if (instance == null) {
            instance = new ChatGPTService();
        }

        return instance;
    }
    private static class GPTInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + GPT_KEY)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }
    }

    private OkHttpClient getOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder
                .addInterceptor(new GPTInterceptor())
                .build();
    }

    private ChatGPTService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(GPT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOKHttpClient())
                .build();
        service = mRetrofit.create(ChatGptApi.class);
    }

    public List<ChatMessage> defaultChatRequest(List<ChatMessage> messages) throws IOException {
        RequestCompletion completion = new RequestCompletion();
        completion.setModel("gpt-3.5-turbo");
        completion.setMessages(messages);

        Call<ResponseCompletion> responseCall = service.getChatResponse(completion);

        ResponseCompletion responseCompletion = responseCall.execute().body();
        List<ChatMessage> responses = new ArrayList<>();
        for (ResponseChoice choice : responseCompletion.getChoices()) {
            responses.add(choice.getMessages());
        }

        return responses;
    }

    public ResponseCompletion defaultChatRequest(List<ChatMessage> messages, Callback<ResponseCompletion> callback) throws IOException {
        RequestCompletion completion = new RequestCompletion();
        completion.setModel("gpt-3.5-turbo");
        completion.setMessages(messages);

        Call<ResponseCompletion> responseCall = service.getChatResponse(completion);
        responseCall.enqueue(callback);

        return responseCall.execute().body();
    }
}
