package com.pab.locationbasedservice;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pab.locationbasedservice.databinding.ActivityMapsBinding;


import android.Manifest;
import android.content.pm.PackageManager;
import com.google.android.gms.tasks.OnSuccessListener;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button currentLocationButton;
    private static final int REQUEST_CODE = 101; // Variabel untuk menentukan kode permintaan izin lokasi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mendapatkan SupportMapFragment dan memberitahu saat peta siap digunakan
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Inisialisasi tombol lokasi terkini
        currentLocationButton = findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(v -> getCurrentLocation());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng place = new LatLng(-6.975882418579812, 108.47741382095178);
        float zoomLevel = 17.0f;

        // Menyiapkan peta dengan menambahkan marker pada lokasi yang ditentukan
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(place)
                .title("Universitas Kuningan Kampus 2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel));
    }

    private void getCurrentLocation() {
        // Mendapatkan lokasi terkini
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin lokasi tidak diberikan, minta izin
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        // Dapatkan lokasi terkini dan tampilkan pada peta
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Dapatkan latitude dan longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // Tampilkan marker pada peta
                    LatLng currentLocation = new LatLng(latitude, longitude);
//                    mMap.clear(); // Hapus semua marker sebelumnya
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Urang keur didieu yeuh"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));
                } else {
                    // Lokasi tidak tersedia
                    Toast.makeText(MapsActivity.this, "Lokasi tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
