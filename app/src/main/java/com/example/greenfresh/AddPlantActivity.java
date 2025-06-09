package com.example.greenfresh;

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

public class AddPlantActivity extends AppCompatActivity {

    private EditText etPlantName, etPrice, etDescription;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Toolbar toolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etPlantName = findViewById(R.id.edit_text_plant_name);
        etPrice = findViewById(R.id.edit_text_price);
        etDescription = findViewById(R.id.edit_text_description);
        btnSubmit = findViewById(R.id.button_submit_add);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlant();
            }
        });
    }

    private void addPlant() {
        String plantName = etPlantName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(plantName) || TextUtils.isEmpty(price) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        Tanaman newPlant = new Tanaman(plantName, description, price);

        ApiClient.getApiService().addPlant(newPlant).enqueue(new Callback<Tanaman>() {
            @Override
            public void onResponse(@NonNull Call<Tanaman> call, @NonNull Response<Tanaman> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddPlantActivity.this, "Tanaman berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddPlantActivity.this, "Gagal menambahkan data. Kode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tanaman> call, @NonNull Throwable t) {
                Toast.makeText(AddPlantActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddPlantActivity", "onFailure: " + t.getMessage());
            }
        });
    }
}