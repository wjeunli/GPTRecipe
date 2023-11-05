package com.team.gptrecipie.api;

import com.team.gptrecipie.api.completion.RequestCompletion;
import com.team.gptrecipie.api.completion.ResponseCompletion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGptApi {
    @POST("chat/completions")
    @Headers("Content-Type: application/json")
    Call<ResponseCompletion> getChatResponse(@Body() RequestCompletion request);
}
