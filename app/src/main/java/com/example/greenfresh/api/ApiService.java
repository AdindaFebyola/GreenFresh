package com.example.greenfresh.api;

import com.example.greenfresh.models.Tanaman;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("plant/all")
    Call<ApiResponse> getAllPlants();

    @POST("plant/new")
    Call<Tanaman> addPlant(@Body Tanaman tanaman);
}