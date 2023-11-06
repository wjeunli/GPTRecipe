package com.team.gptrecipie.api;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.team.gptrecipie.api.completion.ChatMessage;
import com.team.gptrecipie.api.completion.RequestCompletion;
import com.team.gptrecipie.api.completion.ResponseChoice;
import com.team.gptrecipie.api.completion.ResponseCompletion;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class ChatGPTServiceTest {
    private static final String GPT_KEY = "sk-UWjMmHr3qa9if5MN8q4bWeAmUak2gQ3SuwqVLZizI5dAYFRC";
    private static final String GPT_URL = "https://api.chatanywhere.cn/v1/chat/";
    private ChatGPTService service = ChatGPTService.getInstance();
    private static final String TEMPLATE = "I have apple,banana ingredients, please give me a delicious recipe restricted to the ingredient";

    @Test
    public void testURL() {
        List<ChatMessage> requests = new ArrayList();
        ChatMessage preRequire = new ChatMessage();
        preRequire.setRole("system");
        preRequire.setContent("You are a professional cook");
        ChatMessage message = new ChatMessage();
        message.setRole("user");
        message.setContent(TEMPLATE);
        requests.add(message);

        Callback<ResponseCompletion> callback = new Callback<ResponseCompletion>() {
            @Override
            public void onResponse(Call<ResponseCompletion> call, Response<ResponseCompletion> response) {
                if (response.isSuccessful()) {
                    ResponseCompletion chatResponse = response.body();
                    List<ResponseChoice> choice = chatResponse.getChoices();
                    if (choice != null && !choice.isEmpty()) {
                        String content = choice.get(0).getMessage().getContent();
                    }
                } else {
                    Log.e("ChatGPTServiceTest", "ERROR response un successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseCompletion> call, Throwable t) {
                Log.e("ChatGPTServiceTest", "Request Failed", t);
            }
        };

        try {
            service.defaultChatRequest(requests, callback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class GPTInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + GPT_KEY)
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }
    }

    interface ChatGptApi {
        @POST("completions")
        @Headers("Content-Type: application/json")
        Call<ResponseCompletion> getChatResponse(@Body() JsonObject request);

        @POST("completions")
        @Headers("Content-Type: application/json")
        Call<ResponseCompletion> getChatResponse(@Body() RequestCompletion request);

    }

    @Test
    public void testApi() throws IOException {
        ChatGptApi api = new Retrofit.Builder()
                .baseUrl(GPT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOKHttpClient())
                .build()
                .create(ChatGptApi.class);

        JsonObject request = fakeJSONObject();
        RequestCompletion requestCompletion = fakeObjectRequest();
        /*Gson gson = new Gson();
        String res1 = gson.toJson(requestCompletion);*/


        Call<ResponseCompletion> call = api.getChatResponse(requestCompletion);

        Response<ResponseCompletion> response = call.execute();

        if (response.isSuccessful()) {
            System.out.println(response);
        }
    }

    private JsonObject fakeJSONObject() {
        JsonObject request = new JsonObject();
        request.addProperty("model", "gpt-3.5-turbo");
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "Compose a poem that explains the concept of recursion in programming.");
        messages.add(message);
        request.add("messages", messages);

        return request;
    }

    private RequestCompletion fakeObjectRequest() {
        RequestCompletion request = new RequestCompletion();
        request.setModel("gpt-3.5-turbo");
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage message = new ChatMessage();
        message.setRole("user");
        message.setContent("Compose a poem that explains the concept of recursion in programming.");
        list.add(message);
        request.setMessages(list);

        return request;
    }

    private OkHttpClient getOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder
                .addInterceptor(new GPTInterceptor())
                .connectTimeout(100, TimeUnit.SECONDS)
                .callTimeout(100, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
