package ar.com.utn.devmobile.servimatch.ui.main

import android.location.Address
import android.location.Geocoder
import android.media.effect.Effect
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
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
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp), color = Turquesa1)
        } else {
            TopBar(navController = navController, direccion = direccion, onChange = { value -> direccion = value })
            Log.d("MAPS", "La dirección que se envia desde inicio es: $direccion")
            MyGoogleMap(direccion)
        }
    }
}

@Composable
fun TopBar(navController: NavController, direccion: String, onChange: (String) -> Unit) {
    var isClearVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

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
            value = searchText,
            onValueChange = { it -> searchText = it },
            maxLines = 1,
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
                    Row() {
                        IconButton(onClick = { onChange(searchText) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = {
                            searchText = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyGoogleMap(direccion: String) {
    Log.d("MAPS", "Direccion en MyGoogleMaps: $direccion")
    var lat by remember { mutableDoubleStateOf(0.0) }
    var long by remember { mutableDoubleStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val geocoder = Geocoder(context)

    LaunchedEffect(direccion) {
        isLoading = true
        getLatLong(
            geocoder = geocoder,
            direccion = direccion,
            latChange = { _lat -> lat = _lat },
            longChange = { _long -> long = _long },
            onReady = { isLoading = false }
        )
    }

    if (isLoading) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .size(50.dp),
                color = Turquesa1
            )
        }
    } else {
        val userLocation = LatLng(lat, long)
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
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getLatLong(geocoder: Geocoder, direccion: String, latChange: (Double) -> Unit, longChange: (Double) -> Unit, onReady: () -> Unit): Unit {
    if (Geocoder.isPresent()) {
        val listener = Geocoder.GeocodeListener { it ->
            Log.d("MAPS", "'it': $it")
            if (it.isEmpty()) {
                Log.e("MAPS", "No se encontró latitud ni longitud")
            } else {
                latChange(it[0].latitude)
                longChange(it[0].longitude)
                Log.e("MAPS", "HAY LAT: ${it[0].latitude} Y LONG: ${it[0].longitude}")
            }
            onReady()
        }
        geocoder.getFromLocationName(direccion, 1, listener)
    } else {
        Log.e("MAPS", "Problemas con GEOCODER")
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun getDireccion(latLng: LatLng): String {
    val geocoder = Geocoder(LocalContext.current)
    var direcciones = emptyList<Address>()
    try {
        if (Geocoder.isPresent()) {
            val listener = Geocoder.GeocodeListener { it ->
                Log.d("MAPS", "en getDirecciones 'it' es: $it")
                direcciones = it
            }
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1, listener)
            if (direcciones.isNotEmpty()) {
                val address = direcciones[0]
                Log.d("MAPS", "Dirección: ${address.getAddressLine(0)}")
                return address.getAddressLine(0)
            }
            Log.e("MAPS", "Error, no se encontró ninguna dirección: $direcciones")
            return "Dirección desconocida"
        } else {
            Log.e("MAPS", "Problemas con GEOCODER")
            return "Dirección desconocida"
        }
    } catch (e: Exception) {
        Log.e("MAPS", "Error al obtener la dirección:", e)
        return "Dirección desconocida"
    }
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowMapScreen() {
    val navController = rememberNavController()
    MapScreen(navController = navController)
}
