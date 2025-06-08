package com.example.greenfresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

    // 1. Deklarasi variabel untuk RecyclerView, Adapter, dan daftar tanaman
    private RecyclerView recyclerView;
    private PlantAdapter plantAdapter;
    private List<Tanaman> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        // 2. Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recycler_view_plants);
        // Mengatur bagaimana item akan ditampilkan (misalnya, sebagai daftar linear vertikal)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Panggil method untuk mengambil data dari API
        fetchPlants();
    }

    private void fetchPlants() {
        // 4. Menggunakan ApiClient yang sudah kita buat untuk mendapatkan service API
        Call<ApiResponse> call = ApiClient.getApiService().getAllPlants();

        // 5. Menjalankan panggilan API secara asynchronous (di background thread)
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                // 6. Cek apakah panggilan berhasil dan mendapatkan respons
                if (response.isSuccessful() && response.body() != null) {
                    // Ambil daftar tanaman dari body respons
                    plantList = response.body().getData();
                    // Inisialisasi adapter dengan data yang didapat
                    plantAdapter = new PlantAdapter(HomeActivity.this, plantList);
                    // Set adapter ke RecyclerView
                    recyclerView.setAdapter(plantAdapter);
                } else {
                    // Jika gagal, tampilkan pesan error
                    Toast.makeText(HomeActivity.this, "Gagal mengambil data. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                // 7. Jika terjadi error koneksi atau lainnya
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}