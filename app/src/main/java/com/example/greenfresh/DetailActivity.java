package com.example.greenfresh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.greenfresh.models.Tanaman;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription;
    private ImageView ivImage;
    private Button btnUpdatePage;
    private Tanaman plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inisialisasi Views
        tvName = findViewById(R.id.text_detail_name);
        tvPrice = findViewById(R.id.text_detail_price);
        tvDescription = findViewById(R.id.text_detail_description);
        ivImage = findViewById(R.id.image_detail);
        btnUpdatePage = findViewById(R.id.button_update_page);

        // Ambil data Tanaman yang dikirim dari adapter
        plant = (Tanaman) getIntent().getSerializableExtra("EXTRA_PLANT");

        // Cek apakah data tidak null
        if (plant != null) {
            // Tampilkan data ke komponen UI
            tvName.setText(plant.getPlantName());
            tvPrice.setText("Rp " + plant.getPrice());
            tvDescription.setText(plant.getDescription());

            ivImage.setImageResource(R.drawable.tanaman);
        } else {
            // Jika data null, tampilkan pesan error dan tutup activity
            Toast.makeText(this, "Data tanaman tidak ditemukan.", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnUpdatePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class);
                // Kirim juga data tanaman ke UpdateActivity
                intent.putExtra("EXTRA_PLANT", plant);
                startActivity(intent);
            }
        });
    }
}