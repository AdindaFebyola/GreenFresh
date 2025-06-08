package com.example.greenfresh.api;

import com.example.greenfresh.models.Tanaman;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("plant/all")
    Call<ApiResponse> getAllPlants();

    @POST("plant/new")
    Call<Tanaman> addPlant(@Body Tanaman tanaman);

    @DELETE("plant/{name}")
    Call<Void> deletePlant(@Path("name") String plantName);

    @PUT("plant/{name}")
    Call<Tanaman> updatePlant(@Path("name") String plantName, @Body Tanaman plantData);
}