package me.jeffreychang.weatherapp.util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

public abstract class GpsFragment extends Fragment implements LocationListener {
    private ActivityResultLauncher<String> permission;

    public abstract void onLocationPermissionGranted(Location location);

    public abstract void onLocationPermissionDeclined();

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
            if (granted) {
                getLocation();
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                onLocationPermissionDeclined();
            } else {
                Toast.makeText(requireContext(), "Device needs to support GPS location to continue!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void askForLocation() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(requireContext(), "Need permission", Toast.LENGTH_SHORT).show();
        } else {
            permission.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        onLocationPermissionGranted(location);
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void getLocation() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, this);
    }
}
