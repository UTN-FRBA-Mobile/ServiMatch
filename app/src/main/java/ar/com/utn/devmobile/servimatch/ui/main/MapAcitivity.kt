package ar.com.utn.devmobile.servimatch.ui.main

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.com.utn.devmobile.servimatch.MyPreferences
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(navController: NavController) {
    var direccion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val user = MyPreferences.getInstance().username
        val response = ApiClient.apiService.getUser(user?:"admin")

        direccion = response.body()?.ubicacion ?: "Av. Juan Bautista Alberdi 2428"
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(isLoading) {
            CircularProgressIndicator()
        } else {
            TopBar(navController = navController, direccion = direccion, onChange = { value -> direccion = value })
            MyGoogleMap(direccion)
        }
    }

}

@Composable
fun TopBar(navController: NavController, direccion: String, onChange: (String) -> Unit) {
    var isClearVisible by remember { mutableStateOf(false) }

    DisposableEffect(direccion) {
        isClearVisible = direccion.isNotEmpty()
        onDispose { }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(10f),
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Perfil Proovedor"
            )
        }
        OutlinedTextField (
            value = direccion,
            onValueChange = { onChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .alpha(1f)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationOn,
                    contentDescription = "")
            },
            trailingIcon = {
                if (isClearVisible) {
                    IconButton(onClick = {
                        onChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyGoogleMap(direccion: String) {
    val mockDireccion = "Av. Juan Bautista Alberdi 2428" //TODO: BORRAR
    val userLocation = getLatLong(direccion = mockDireccion)
    val centerLocation = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userLocation, 10f) }
    val userMarker = rememberMarkerState( key = "user", position = userLocation)

    val randomLocation = LatLng(-34.608728, -58.427796)

    GoogleMap (
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = centerLocation
    ) {
        Marker(
            state = userMarker,
            icon = BitmapDescriptorFactory.defaultMarker()
        )

        Circle(
            center = randomLocation,
            radius = 5000.0
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun getLatLong(direccion: String): LatLng {
    val defaultLocation = LatLng(0.0, 0.0)
    val geocoder = Geocoder(LocalContext.current)
    var direcciones = emptyList<Address>()
    try {
        val listener = Geocoder.GeocodeListener { it ->
            direcciones = it
        }
        geocoder.getFromLocationName(direccion, 1, listener)
        return if (direcciones.isNotEmpty()) {
            val address = direcciones[0]
            Log.d("MAPS", "Latitud: ${address.latitude}, Longitud: ${address.longitude}")
            LatLng(address.latitude, address.longitude)
        } else {
            Log.e("MAPS", "Error, no se encontro ningun LatLong: $direcciones")
            defaultLocation
        }
    } catch (e: Exception) {
        Log.e("MAPS", "Error al obtener la LatLong:", e)
        return defaultLocation
    }
}


/*
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun getLatLong(direccion: String): LatLng {
    val defaultLocation = LatLng(0.0, 0.0)
    val geocoder = Geocoder(LocalContext.current)
    var direcciones = emptyList<Address>()
    try {
        val listener = Geocoder.GeocodeListener { it ->
            direcciones = it
        }
        geocoder.getFromLocationName(direccion, 1, listener)
        if (direcciones.isNotEmpty()) {
            val address = direcciones[0]
            Log.d("MAPS", "Latitud: ${address.latitude}, Longitud: ${address.longitude}")
            return LatLng(address.latitude, address.longitude)
        }
        Log.e("MAPS", "Error, no se encontro ningun LatLong: $direcciones")
        return defaultLocation
    } catch (e: Exception) {
        Log.e("MAPS", "Error al obtener la LatLong:", e)
        return defaultLocation
    }
}*/

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun getDireccion(lat: Double, long: Double): String {
    val geocoder = Geocoder(LocalContext.current)
    var direccion = ""
    try {
        val listener = Geocoder.GeocodeListener {it ->
            direccion = it[0].getAddressLine(0)
        }
        geocoder.getFromLocation(lat, long, 1, listener)

        if (direccion != "") {
            Log.d("MAPS", "Direccion: $direccion")
            return direccion
        }
        return ""
    } catch (e: Exception) {
        Log.e("MAPS", "Error al obtener direccion:", e)
        return ""
    }
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowMapScreen() {
    val navController = rememberNavController()
    MapScreen(navController = navController)
}
