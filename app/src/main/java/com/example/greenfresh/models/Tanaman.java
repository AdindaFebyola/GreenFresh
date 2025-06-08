package com.example.greenfresh.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Tanaman implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("plant_name")
    private String plantName;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private String price;

    public int getId() {
        return id;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public Tanaman(String plantName, String description, String price) {
        this.plantName = plantName;
        this.description = description;
        this.price = price;
    }
}
