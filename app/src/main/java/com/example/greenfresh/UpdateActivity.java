package com.example.greenfresh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.greenfresh.api.ApiClient;
import com.example.greenfresh.models.Tanaman;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private EditText etPlantName, etPrice, etDescription;
    private Button btnSubmitUpdate;
    private Tanaman currentPlant; // Untuk menyimpan data tanaman yang akan diupdate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Toolbar toolbar = findViewById(R.id.toolbar_add); // ID toolbar sama dengan layout add
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Hubungkan variabel dengan komponen UI
        etPlantName = findViewById(R.id.edit_text_plant_name);
        etPrice = findViewById(R.id.edit_text_price);
        etDescription = findViewById(R.id.edit_text_description);
        btnSubmitUpdate = findViewById(R.id.button_submit_update);

        // Ambil data tanaman dari Intent
        currentPlant = (Tanaman) getIntent().getSerializableExtra("EXTRA_PLANT");

        // Isi form dengan data yang ada
        if (currentPlant != null) {
            etPlantName.setText(currentPlant.getPlantName());
            etPrice.setText(currentPlant.getPrice());
            etDescription.setText(currentPlant.getDescription());
        }

        // Beri aksi pada tombol simpan perubahan
        btnSubmitUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlantData();
            }
        });
    }

    private void updatePlantData() {
        // Ambil data baru dari form
        String newName = etPlantName.getText().toString().trim();
        String newPrice = etPrice.getText().toString().trim();
        String newDescription = etDescription.getText().toString().trim();

        // Validasi
        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPrice) || TextUtils.isEmpty(newDescription)) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat objek Tanaman dengan data baru
        Tanaman updatedPlant = new Tanaman(newName, newDescription, newPrice);

        // Panggil API untuk update
        // Kita butuh nama original untuk path URL, dan data baru untuk body
        ApiClient.getApiService().updatePlant(currentPlant.getPlantName(), updatedPlant).enqueue(new Callback<Tanaman>() {
            @Override
            public void onResponse(@NonNull Call<Tanaman> call, @NonNull Response<Tanaman> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateActivity.this, "Data berhasil diupdate!", Toast.LENGTH_SHORT).show();
                    // Tutup semua activity di atas HomeActivity dan kembali ke sana
                    Intent intent = new Intent(UpdateActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Gagal mengupdate data. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tanaman> call, @NonNull Throwable t) {
                Toast.makeText(UpdateActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}