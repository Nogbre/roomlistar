package com.example.roomlistar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<LocationEntity> locationList;
    private Context context;

    // Constructor que acepta la lista de datos
    public LocationAdapter(Context context, List<LocationEntity> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    // Método para actualizar la lista de datos
    public void setLocationList(List<LocationEntity> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista de cada elemento de la lista
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        // Obtener el elemento actual
        LocationEntity location = locationList.get(position);

        // Configurar los datos en las vistas del ViewHolder
        holder.locationName.setText(location.getName());
        holder.latitude.setText("Latitud: " + location.getLatitude());
        holder.longitude.setText("Longitud: " + location.getLongitude());
    }

    @Override
    public int getItemCount() {
        return locationList == null ? 0 : locationList.size();
    }

    // ViewHolder que contiene las vistas para cada elemento de la lista
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView latitude;
        TextView longitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencia a los elementos del layout item_location.xml
            locationName = itemView.findViewById(R.id.location_name);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
        }
    }
}
