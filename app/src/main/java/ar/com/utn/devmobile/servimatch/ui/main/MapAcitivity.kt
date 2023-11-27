package ar.com.utn.devmobile.servimatch.ui.main

import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.com.utn.devmobile.servimatch.MyPreferences
import ar.com.utn.devmobile.servimatch.navController
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import ar.com.utn.devmobile.servimatch.ui.model.ProviderInfo
import ar.com.utn.devmobile.servimatch.ui.model.UserInfo
import ar.com.utn.devmobile.servimatch.ui.theme.Purple40
import ar.com.utn.devmobile.servimatch.ui.theme.Purple80
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura1
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MapScreen(navController: NavController, username: String) {
    //val username = "admin" //TODO BORRAR, ES PARA PRUEBAS
    var user: UserInfo by remember { mutableStateOf(UserInfo("admin", "", 0.0, 0.0)) }
    var direccion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var colorSearchBar by remember { mutableStateOf(Color.Black) }
    var providers: List<ProviderInfo> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        val userResponse = ApiClient.apiService.getUser(username)
        val providersResponse = ApiClient.apiService.getProviders()
        userResponse.body()?.let { repsonseUser ->
            user = repsonseUser
            direccion = user.direccion
        }
        providers = providersResponse.body() ?: emptyList()
        isLoading = false
    }

    if(isLoading){
        CircularProgressIndicator(modifier = Modifier
            .fillMaxSize(),
            color = Turquesa1
        )
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight()
        ) {
            TopBar(
                navController = navController,
                colorSearchBar = colorSearchBar,
                onCancel = { color -> colorSearchBar = color },
                onChange = { value -> direccion = value }
            )
            MyGoogleMap(
                navController = navController,
                user = user,
                direccion = direccion,
                providers = providers,
                changeColor = { color -> colorSearchBar = color }
            )
        }

    }
}


@Composable
fun TopBar(
    navController: NavController,
    colorSearchBar: Color,
    onCancel: (Color) -> Unit,
    onChange: (String) -> Unit
) {
    var isClearVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    DisposableEffect(searchText) {
        isClearVisible = searchText.isNotEmpty()
        onDispose {  }
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
                contentDescription = "Login"
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
                .border(2.dp, colorSearchBar, shape = RoundedCornerShape(8.dp)),
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationOn,
                    contentDescription = "")
            },
            trailingIcon = {
                if (isClearVisible) {
                    Row() {
                        IconButton(onClick = { 
                            onChange(searchText) 
                            Log.d("MAPS", "VALOR: $searchText")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                        IconButton(onClick = {
                            searchText = ""
                            isClearVisible = false
                            onCancel(Color.Black)
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
fun MyGoogleMap(
    navController: NavController,
    user: UserInfo,
    direccion: String,
    providers: List<ProviderInfo>,
    changeColor: (Color) -> Unit,
) {
    val userLatLong = LatLng(user.latitud, user.longitud)
    Log.d("MAPS", "Direccion en MyGoogleMaps: $direccion")
    var isLoading by remember { mutableStateOf(true) }
    var selectedProvider by remember { mutableStateOf<ProviderInfo?>(null) }
    val userMarker = rememberMarkerState( key = "user", position = LatLng(user.latitud, user.longitud))

    val context = LocalContext.current
    val geocoder = Geocoder(context)

    LaunchedEffect(direccion) {
        isLoading = true
        getLatLong(
            geocoder = geocoder,
            direccion = direccion,
            markerChange = { newMarker -> userMarker.position = newMarker },
            onReady = { isLoading = false },
            changeColor = changeColor
        )
    }

    if (isLoading) {
        Row (verticalAlignment = Alignment.CenterVertically){
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .size(50.dp),
                color = Turquesa1
            )
        }
    } else {
        val centerLocation = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(userLatLong, 12f) }
        Box (
            modifier = Modifier
                .fillMaxSize()
        ){
            GoogleMap (
                cameraPositionState = centerLocation,
                onMapLongClick = { it -> userMarker.position = it }
            ) {
                Marker(
                    state = userMarker,
                    icon = BitmapDescriptorFactory.defaultMarker()
                )
                if (selectedProvider != null) {
                    ProviderInfoDialog(
                        navController = navController,
                        provider = selectedProvider!!,
                        onClose = { selectedProvider = null }
                    )
                }
                //MarcarRangosProviders(providers = providers, onProviderClick = { provider ->
                //    selectedProvider = provider
                //})
            }
            GoHomeSection(navController, providers, userMarker.position, user.username)
        }
    }

}

@Composable
fun MarcarRangosProviders(providers: List<ProviderInfo>, onProviderClick: (ProviderInfo) -> Unit) {
    for (provider in providers) {
        Circle(
            center = LatLng(provider.latitud, provider.longitud),
            radius = provider.rangoMax,
            clickable = true,
            strokeColor = Purpura2,
            onClick = { onProviderClick(provider) },
            fillColor = Purpura2.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun GoHomeSection(
    navController: NavController,
    providers: List<ProviderInfo>,
    userPosition: LatLng,
    username: String
) {
    var proveedoresCercanos: List<ProviderInfo> by remember { mutableStateOf(emptyList()) }
    var hayProveedores by remember { mutableStateOf(true) }

    DisposableEffect(userPosition) {
        proveedoresCercanos = calcularProveedoresCercanos(providers, userPosition)
        hayProveedores = proveedoresCercanos.isNotEmpty()
        onDispose {} // Cleanup
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            modifier = Modifier.padding(horizontal = 20.dp),
            onClick = {
                MyPreferences.getInstance().set("latlong_user", userPosition)
                navController.navigate("home/$username")
            },
            shape = RoundedCornerShape(10.dp),
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "home page"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getLatLong(geocoder: Geocoder, direccion: String, markerChange: (LatLng) -> Unit , onReady: () -> Unit, changeColor: (Color) -> Unit): Unit {
    if (Geocoder.isPresent()) {
        val listener = Geocoder.GeocodeListener { it ->
            Log.d("MAPS", "'it': $it")
            if (it.isEmpty()) {
                Log.e("MAPS", "No se encontró latitud ni longitud")
                changeColor(Color.Red)
            } else {
                val newPosition = LatLng(it[0].latitude, it[0].longitude)
                markerChange(newPosition)
                Log.d("MAPS", "HAY LAT: ${it[0].latitude} Y LONG: ${it[0].longitude}")
                changeColor(Color.Black)
            }
            onReady()
        }
        geocoder.getFromLocationName(direccion, 1, listener)
    } else {
        Log.e("MAPS", "Problemas con GEOCODER")
    }
}

/*
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun getDirection(geocoder: Geocoder, LatLng: String, markerChange: (MarkerState) -> Unit , onReady: () -> Unit, changeColor: (Color) -> Unit): Unit {
    if (Geocoder.isPresent()) {
        val listener = Geocoder.GeocodeListener { it ->
            Log.d("MAPS", "'it': $it")
            if (it.isEmpty()) {
                Log.e("MAPS", "No se encontró latitud ni longitud")
                changeColor(Color.Red)
            } else {
                val newPosition = LatLng(it[0].latitude, it[0].longitude)
                markerChange(MarkerState(newPosition))
                Log.d("MAPS", "HAY LAT: ${it[0].latitude} Y LONG: ${it[0].longitude}")
                changeColor(Color.Black)
            }
            onReady()
        }
        geocoder.getFromLocationName(direccion, 1, listener)
    } else {
        Log.e("MAPS", "Problemas con GEOCODER")
    }
}*/

@Composable
fun ProviderInfoDialog(navController: NavController, provider: ProviderInfo, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = Purple80,
        title = { Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Información del proveedor",
            color = Color.Black,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )},
        text = {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AsyncImage(
                    model = provider.imageResource,
                    contentDescription = "Foto de perfil del ofertante",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(color = Turquesa3, CircleShape)
                        .border(1.dp, Purpura3, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = "${provider.name} ${provider.apellido}",
                        color = Purpura3,
                        fontSize = 24.sp
                    )
                    Text(
                        text = provider.rol,
                        fontSize = 20.sp
                    )
                }
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = onClose,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purpura2.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Cerrar",
                    fontSize = 16.sp
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = { navController.navigate("profile/${provider.identificador}") },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Ver",
                    fontSize = 16.sp
                )
            }
        }
    )
}

fun calcularProveedoresCercanos(proveedores: List<ProviderInfo>, user: LatLng): List<ProviderInfo> {
    val proveedoresCercanos = mutableListOf<ProviderInfo>()
    for (proveedor in proveedores) {
        val proveedorLocation = LatLng(proveedor.latitud, proveedor.longitud)
        val distancia = calcularDistanciaEntrePuntos(user, proveedorLocation)
        if (distancia <= proveedor.rangoMax) {
            proveedoresCercanos.add(proveedor)
        }
    }
    return proveedoresCercanos
}

fun calcularDistanciaEntrePuntos(user: LatLng, provider: LatLng): Float {
    val location1 = Location("User")
    location1.latitude = user.latitude
    location1.longitude = user.longitude

    val location2 = Location("Provider")
    location2.latitude = provider.latitude
    location2.longitude = provider.longitude

    return location1.distanceTo(location2)
}
