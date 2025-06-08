package com.example.greenfresh.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("plant/all")
    Call<ApiResponse> getAllPlants();
}