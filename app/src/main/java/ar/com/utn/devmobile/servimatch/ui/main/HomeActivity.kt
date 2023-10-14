package ar.com.utn.devmobile.servimatch.ui.main

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura1
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa4
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa5
import ar.com.utn.devmobile.servimatch.ui.theme.VerdePrecio

var paddingH = 16.dp
var paddingV = 8.dp
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(navController: NavController, username: String) {
    var searchList by remember { mutableStateOf<MutableList<ProviderInfo>>(mutableListOf()) }

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
            Header(navController, username, 0.dp, 0.dp)
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre header y filtros
            FilterList(searchList)
            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre filtros y recomendados
            ProvidersList(navController, searchList)
        }
    }
}

@Composable
fun ProvidersList(navController: NavController, searchList: MutableList<ProviderInfo>) {
    val context = LocalContext.current

    val recommendedList = listOf(
        ProviderInfo(R.drawable.hombre1, "Joaquin Benitez", "$2500", "Palermo - Plomero", "plomero"),
        ProviderInfo(R.drawable.hombre2, "Eduardo Alarcón", "$1800", "Recoleta - Cerrajero", "cerrajero"),
        ProviderInfo(R.drawable.mujer1, "Maria Esperanza", "$3000", "Belgrano - Niñera", "niñera")
        // Agrega más proveedores si es necesario
    )

    val generalList = listOf(
        ProviderInfo(R.drawable.hombre3, "Lucas Sainz", "$3000", "Flores - Reparación Aire Acondicionado", "reparacion aire acondicionado"),
        ProviderInfo(R.drawable.mujer2, "Eugenia Romano", "$2500", "Caballito - Limpieza Hogar ", "limpieza hogar"),
        // Agrega más proveedores si es necesario
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(horizontal = 8.dp, vertical = 4.dp),
        content = {
            if (searchList.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.resultados) + " (${searchList.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = PurpleGrey40,
                    )
                }
                items(searchList) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name, providerInfo.price, providerInfo.location, navController)
                }
            } else {
                item {
                    Text(
                        text = stringResource(id = R.string.recomendados) + " (${recommendedList.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = Turquesa4,
                    )
                }
                items(recommendedList) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name, providerInfo.price, providerInfo.location, navController)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.mas_resultados) + " (${generalList.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = PurpleGrey40,
                    )
                }
                items(generalList) { providerInfo ->
                    val imageBitmap: ImageBitmap =
                        ImageBitmap.Companion.imageResource(context.resources, providerInfo.imageResource)
                    Provider(imageBitmap, providerInfo.name, providerInfo.price, providerInfo.location, navController)
                }
            }
        }
    )
}

@Composable
fun Provider(imageBitmap: ImageBitmap,
             nombre: String,
             precio: String,
             ubicacion: String,
             navController: NavController) {
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
                text = "$$$",
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
                navController.navigate("profile/123") {
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
fun FilterList(searchList: MutableList<ProviderInfo>){
    val zones = listOf("Almagro", "Chacarita", "Flores")
    val jobs = listOf("Plomero", "Cerrajero", "Electricista")
    val prices = listOf("De Mayor a Menor", "De Menor a Mayor")

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Filter(
            modifier = Modifier.weight(0.7f),
            "Zona",
            zones,
            searchList
        ) // Primer conjunto de filtros
        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los filtros
        Filter(
            modifier = Modifier.weight(0.7f),
            "Rubro",
            jobs,
            searchList
        ) // Primer conjunto de filtros
        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los filtros
        Filter(
            modifier = Modifier.weight(0.7f),
            "Precio",
            prices,
            searchList
        )
    }
}

@Composable
fun Filter(modifier: Modifier = Modifier,
           categoria:String,
           items:List<String>,
           searchList: MutableList<ProviderInfo>) {

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
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    selectedItem = item
                    expanded = false
                    searchList.add(  ProviderInfo(R.drawable.mujer2, "Eugenia Romano", "$2500", "Caballito - Limpieza Hogar ","limpieza hogar"),)
                },
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
