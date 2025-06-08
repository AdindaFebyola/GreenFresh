package com.example.greenfresh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.greenfresh.R;
import com.example.greenfresh.models.Tanaman;
import com.example.greenfresh.HomeActivity;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private Context context;
    private List<Tanaman> plantList;

    private HomeActivity activity;

    public PlantAdapter(Context context, List<Tanaman> plantList, HomeActivity activity) {
        this.context = context;
        this.plantList = plantList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tanaman plant = plantList.get(position);

        holder.tvPlantName.setText(plant.getPlantName());
        holder.tvPlantPrice.setText("Rp " + plant.getPrice());

        Glide.with(context)
                .load(R.drawable.logo) // Ganti dengan plant.getImageUrl() jika sudah ada
                .placeholder(R.drawable.logo)
                .into(holder.ivPlantImage);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deletePlant(plant.getPlantName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void updateData(List<Tanaman> newPlantList) {
        this.plantList.clear(); // Kosongkan list yang lama
        this.plantList.addAll(newPlantList); // Isi dengan data yang baru
        notifyDataSetChanged(); // Perintahkan RecyclerView untuk me-refresh dirinya
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlantImage;
        TextView tvPlantName, tvPlantPrice;
        Button btnDelete, btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlantImage = itemView.findViewById(R.id.image_view_plant);
            tvPlantName = itemView.findViewById(R.id.text_view_plant_name);
            tvPlantPrice = itemView.findViewById(R.id.text_view_plant_price);
            btnDelete = itemView.findViewById(R.id.button_delete);
            btnDetail = itemView.findViewById(R.id.button_detail);
        }
    }
}