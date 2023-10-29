package ar.com.utn.devmobile.servimatch.ui.main

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.model.ProviderInfo
import ar.com.utn.devmobile.servimatch.ui.theme.PurpleGrey40
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

import androidx.lifecycle.viewmodel.compose.viewModel
import ar.com.utn.devmobile.servimatch.ui.model.ApiService

import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa4
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa5
import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Response

var paddingH = 16.dp
var paddingV = 8.dp
val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(navController: NavController, username: String) {
    //Cargo el viewModel que va a gestionar la lista de proveedores que se muestra.
    val listaDeProveedores = viewModel<ListaDeProveedores>()

    //Cargo las primeras listas, recomendados y general. Pegandole al back.
    listaDeProveedores.getRecomendados()
    listaDeProveedores.getGeneral()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1)
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = paddingH, vertical = paddingV),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            //Renderizo header.
            Header(navController, username, 0.dp, 0.dp)

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre header y filtros

            //Renderizo lista de filtros
            FilterList(listaDeProveedores)

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre filtros y recomendados

            //Renderizo lista de Proveedores. Inicialmente renderiza las listas recomendados y general.
            ProvidersList(navController, listaDeProveedores)

        }
    }
}

@Composable
fun ProvidersList(navController: NavController, listaProveedores: ListaDeProveedores) {
    val context = LocalContext.current
    val busqueda by remember { listaProveedores.busqueda }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(horizontal = 8.dp, vertical = 4.dp),
        content = {
         if (busqueda.isNotEmpty()) {

                item {
                    Text(
                        text = stringResource(id = R.string.resultados) + " (${busqueda.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = PurpleGrey40,
                    )
                }
                items(busqueda) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name,providerInfo.priceSimbol, providerInfo.location, navController,providerInfo.identificador)
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.recomendados) + " (${listaProveedores.recomendados.value.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = Turquesa4,
                    )
                }
                items(listaProveedores.recomendados.value) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name,providerInfo.priceSimbol, providerInfo.location, navController,providerInfo.identificador)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.mas_resultados) + " (${listaProveedores.general.value.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = PurpleGrey40,
                    )
                }
                items(listaProveedores.general.value) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name,providerInfo.priceSimbol, providerInfo.location, navController,providerInfo.identificador)
                }
            }
        }
    )
}

@Composable
fun Provider(imageBitmap: ImageBitmap,
             nombre: String,
             simboloPrecio: String,
             ubicacion: String,
             navController: NavController,
             identificador: Number) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical=paddingV),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Turquesa3, CircleShape)
        )

        // Espacio entre la imagen y la información
        Spacer(modifier = Modifier.width(16.dp))

        // Columna con información de la persona
        Column(
            modifier = Modifier.weight(1f) // Permite que la columna ocupe el espacio disponible
        ) {
            Text(
                text = nombre,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 20.sp,
                color = Turquesa5
            )
            Text(
                //text = "Desde $precio",
                text = simboloPrecio,
                fontSize = 16.sp,
                color = Purpura3
            )
            Text(
                text = "$ubicacion",
                color = Purpura2
            )
        }

        // IconButton con icono
        IconButton(
            onClick = {
                // Acción a realizar cuando se hace clic en el icono
                navController.navigate("profile/$identificador") {
                    // Puedes pasar el ID del proveedor como argumento si es necesario
                    // arguments = bundleOf("idProveedor" to proveedor.id)
                }
            },
            modifier = Modifier
                .size(35.dp) // Ajusta el tamaño del icono
        ) {
            Icon(
                painter = painterResource(id = R.drawable.eye), // Reemplaza con el recurso de tu icono de ojo
                contentDescription = null,
                tint = Turquesa4 // Color del icono
            )
        }
        // Divider negro como separador

    }
    Divider(color = Color.Gray, thickness = 1.dp)
}

@Composable
fun FilterList(listaProveedores: ListaDeProveedores){
    var zones by remember { mutableStateOf(emptyList<String>()) }
    var jobs by remember { mutableStateOf(emptyList<String>()) }
    var rating by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(Unit) {
        zones = apiService.zonas()
        jobs= apiService.profesiones()
        rating= apiService.rating()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Filter(
            modifier = Modifier.weight(0.7f),
            "Zona",
            zones,
            listaProveedores
        ) // Primer conjunto de filtros
        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los filtros
        Filter(
            modifier = Modifier.weight(0.7f),
            "Rubro",
            jobs,
            listaProveedores
        ) // Primer conjunto de filtros
        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los filtros
        Filter(
            modifier = Modifier.weight(0.7f),
            "Valoracion",
            rating,
            listaProveedores
        )
    }
}

@Composable
fun Filter(modifier: Modifier = Modifier,



           categoria:String,
           items:List<String>,
           listaProveedores: ListaDeProveedores) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(categoria) }

    Card(
        modifier = modifier
            .fillMaxWidth(0.25f)
            .clickable { expanded = true }, // Agregar un Modifier.clickable
        shape = RoundedCornerShape(8.dp), // Bordes redondeados más suaves
        border = BorderStroke(1.dp, Color.Black) // Delineado en negro
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color.White,
                    shape = RoundedCornerShape(8.dp) // Bordes redondeados más suaves
                ),
            content = {
                Row(
                    modifier = Modifier
                        .padding(8.dp) // Espaciado más pequeño
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(selectedItem, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
            }
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.wrapContentSize().height(200.dp)
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    listaProveedores.buscarPorFiltro(categoria,item)
                    selectedItem = item
                    expanded = false },
                text = { Text(item, color = Color.Black) }
            )
        }
    }
}

@Composable
fun Header(navController: NavController, username: String, paddingH: Dp, paddingV: Dp) {
    // Convertir el primer carácter a mayúscula y el resto a minúscula
    val formattedUsername = username.lowercase().replaceFirstChar { it.uppercase() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Agregar el botón de retroceso aquí
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Atrás"
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Este Box ocupa la mitad del espacio del Row
                .padding(end = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.welcome)+" "+formattedUsername,
                style = MaterialTheme.typography.headlineSmall,
                color = Turquesa4,
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ){
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null, // Proporciona una descripción adecuada
                modifier = Modifier.size(75.dp) // Tamaño de la imagen
            )
        }
    }
}
