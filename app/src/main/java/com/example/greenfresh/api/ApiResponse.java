package com.example.greenfresh.api;

import com.example.greenfresh.models.Tanaman;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Tanaman> data;

    public String getMessage() {
        return message;
    }

    public List<Tanaman> getData() {
        return data;
    }
}
