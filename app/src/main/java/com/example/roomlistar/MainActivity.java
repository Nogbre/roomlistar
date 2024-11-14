package com.example.roomlistar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roomlistar.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.roomlistar.R.layout.activity_main);
        // Verifica que el fragmento se cargue solo si `savedInstanceState` es nulo
        if (savedInstanceState == null) {
            // Llama al fragmento
            loadFragment(new MapFragment());
        }
    }

    // Método para cargar el fragmento
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}