package com.example.roomlistar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationDao locationDao;
    private EditText inputName;
    private Button btnSaveMarker;
    private Marker selectedMarker;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Button btnRecycle,btnListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        inputName = view.findViewById(R.id.edit_text_name);
        btnSaveMarker = view.findViewById(R.id.save_button);
        btnRecycle=view.findViewById(R.id.btnRecycle);
        btnListView=view.findViewById(R.id.btnListView);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationDao = AppDatabase.getInstance(requireContext()).locationDao();

        // Configurar el SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Guardar marcador en la base de datos al presionar el botón
        btnSaveMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMarkerToDatabase();
            }
        });
        btnRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Recycle.class));

            }
        });
        btnListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ListActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        // Pedir permisos de ubicación si no están concedidos
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        // Cargar todos los marcadores guardados en la base de datos
        loadSavedMarkers();

        // Listener para añadir marcador en la posición seleccionada
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }
                selectedMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Nuevo Marcador")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        });
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        });
    }

    private void saveMarkerToDatabase() {
        if (selectedMarker != null) {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Ingrese un nombre para el marcador", Toast.LENGTH_SHORT).show();
                return;
            }
            LatLng position = selectedMarker.getPosition();
            LocationEntity location = new LocationEntity(0, name, position.latitude, position.longitude);

            // Insertar ubicación en la base de datos
            executorService.execute(() -> {
                locationDao.insert(location);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Marcador guardado en la base de datos", Toast.LENGTH_SHORT).show();
                    inputName.setText("");
                    selectedMarker.remove();
                });
            });
        } else {
            Toast.makeText(requireContext(), "No hay un marcador seleccionado para guardar", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSavedMarkers() {
        LiveData<List<LocationEntity>> allLocations = locationDao.getAllLocations();
        allLocations.observe(getViewLifecycleOwner(), new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(List<LocationEntity> locations) {
                googleMap.clear();
                for (LocationEntity location : locations) {
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(position).title(location.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
        } else {
            Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
        }
    }
}