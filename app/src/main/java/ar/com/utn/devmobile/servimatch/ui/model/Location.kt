package ar.com.utn.devmobile.servimatch.ui.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices



class Location {
    @Composable
    fun Get() {
        val context = LocalContext.current
        val fusedLocationClient = remember {
            LocationServices.getFusedLocationProviderClient(context)
        }

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            Log.d("GetLocation", "Ubicación del usuario: ${location.latitude}, ${location.longitude}")
                        } else {
                            Log.d("GetLocation", "Ubicación del usuario no disponible")
                        }
                    }
                    .addOnFailureListener { exception: Exception ->
                        Log.d("GetLocation", "Error al obtener la ubicación del usuario: $exception")
                    }
            }
        }

        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d("GetLocation", "Ubicación del usuario: ${location.latitude}, ${location.longitude}")
                    } else {
                        Log.d("GetLocation", "Ubicación del usuario no disponible")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("GetLocation", "Error al obtener la ubicación del usuario: $exception")
                }
        } else {
            //Se pide el permiso
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}