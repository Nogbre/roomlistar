package com.example.roomlistar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycle extends AppCompatActivity {
    private LocationDao locationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recycle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializa el adaptador con una lista vacía
        LocationAdapter adapter = new LocationAdapter(this, new ArrayList<>());

        // Configura el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Inicializa el DAO (asegúrate de que AppDatabase esté correctamente configurada)
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        locationDao = db.locationDao();

        // Observa los datos de Room y actualiza el adaptador
        locationDao.getAllLocations().observe(this, locations -> {
            adapter.setLocationList(locations);
        });
    }
}