package com.example.greenfresh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greenfresh.DetailActivity;
import com.example.greenfresh.HomeActivity;
import com.example.greenfresh.R;
import com.example.greenfresh.models.Tanaman;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private Context context;
    private List<Tanaman> plantList;
    private HomeActivity activity; // Referensi ke HomeActivity untuk memanggil method delete

    // Constructor untuk menerima data dan activity
    public PlantAdapter(Context context, List<Tanaman> plantList, HomeActivity activity) {
        this.context = context;
        this.plantList = plantList;
        this.activity = activity;
    }

    // Metode ini dipanggil untuk membuat setiap item (ViewHolder) baru
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new ViewHolder(view);
    }

    // Metode ini dipanggil untuk mengisi data ke dalam setiap item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Mengambil satu objek Tanaman dari daftar berdasarkan posisinya
        Tanaman plant = plantList.get(position);

        // Mengisi data ke komponen UI di dalam item
        holder.tvPlantName.setText(plant.getPlantName());
        holder.tvPlantPrice.setText("Rp " + plant.getPrice());

        // Menggunakan Glide untuk memuat gambar (sementara pakai logo)
        Glide.with(context)
                .load(R.drawable.logo) // Nanti bisa diganti dengan plant.getImageUrl() jika ada
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.ivPlantImage);

        // Memberi aksi klik untuk tombol Hapus
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memanggil method deletePlant yang ada di HomeActivity
                activity.deletePlant(plant.getPlantName());
            }
        });

        // Memberi aksi klik untuk tombol Detail
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat Intent untuk pindah ke DetailActivity
                Intent intent = new Intent(context, DetailActivity.class);
                // Mengirim seluruh objek 'plant' ke DetailActivity
                // Pastikan class Tanaman sudah 'implements Serializable'
                intent.putExtra("EXTRA_PLANT", plant);
                context.startActivity(intent);
            }
        });
    }

    // Metode ini mengembalikan jumlah total item dalam daftar
    @Override
    public int getItemCount() {
        return plantList.size();
    }

    // Inner class ViewHolder untuk menampung referensi komponen UI dari satu item
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlantImage;
        TextView tvPlantName, tvPlantPrice;
        Button btnDelete, btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Menghubungkan variabel dengan ID komponen di 'item_plant.xml'
            ivPlantImage = itemView.findViewById(R.id.image_view_plant);
            tvPlantName = itemView.findViewById(R.id.text_view_plant_name);
            tvPlantPrice = itemView.findViewById(R.id.text_view_plant_price);
            btnDelete = itemView.findViewById(R.id.button_delete);
            btnDetail = itemView.findViewById(R.id.button_detail);
        }
    }

    // Method untuk memperbarui data di adapter
    public void updateData(List<Tanaman> newPlantList) {
        this.plantList.clear();
        this.plantList.addAll(newPlantList);
        notifyDataSetChanged();
    }
}