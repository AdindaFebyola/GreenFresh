package com.example.greenfresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.greenfresh.adapters.PlantAdapter;
import com.example.greenfresh.api.ApiClient;
import com.example.greenfresh.api.ApiResponse;
import com.example.greenfresh.models.Tanaman;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlantAdapter plantAdapter;
    private List<Tanaman> plantList;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_plants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- PERUBAHAN 1: Inisialisasi adapter sekali saja di awal dengan list kosong ---
        plantList = new ArrayList<>();
        plantAdapter = new PlantAdapter(this, plantList);
        recyclerView.setAdapter(plantAdapter);
        // --- AKHIR PERUBAHAN 1 ---

        btnAdd = findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddPlantActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tetap panggil fetchPlants() setiap kali activity ini kembali aktif
        fetchPlants();
    }

    private void fetchPlants() {
        Call<ApiResponse> call = ApiClient.getApiService().getAllPlants();

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // --- PERUBAHAN 2: Panggil updateData, jangan buat adapter baru ---
                    List<Tanaman> fetchedPlants = response.body().getData();
                    plantAdapter.updateData(fetchedPlants);
                    // --- AKHIR PERUBAHAN 2 ---

                } else {
                    Toast.makeText(HomeActivity.this, "Gagal mengambil data. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}