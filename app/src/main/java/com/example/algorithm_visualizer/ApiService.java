package com.example.algorithm_visualizer;

import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @POST
    Call<JsonObject> sendRequest(@Url String url,
                                 @Header("Authorization") String authorization,
                                 @Header("Content-Type") String contentType,
                                 @Body JsonObject requestBody);
}
