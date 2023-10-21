package com.team.gptrecipie.api;

import com.team.gptrecipie.model.completion.RequestCompletion;
import com.team.gptrecipie.model.completion.ResponseCompletion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ChatGptApi {
    @GET("/chat/completions")
    @Headers("Content-Type: application/json")
    Call<ResponseCompletion> getChatResponse(@Body() RequestCompletion request);
}
