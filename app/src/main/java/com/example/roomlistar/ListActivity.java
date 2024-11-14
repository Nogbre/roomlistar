// ListActivity.java
package com.example.roomlistar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private AppDatabase db;
    private ArrayAdapter<String> adapter;
    private List<String> locationNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_view);

        // Inicializa la base de datos y la lista de nombres
        db = AppDatabase.getInstance(getApplicationContext());
        locationNames = new ArrayList<>();

        // Configura el adaptador para el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationNames);
        listView.setAdapter(adapter);

        // Observa los datos en Room y actualiza el ListView
        db.locationDao().getAllLocations().observe(this, locations -> {
            locationNames.clear();
            for (LocationEntity location : locations) {
                locationNames.add(location.getName() + " (" + location.getLatitude() + ", " + location.getLongitude() + ")");
            }
            adapter.notifyDataSetChanged();
        });
    }
}
