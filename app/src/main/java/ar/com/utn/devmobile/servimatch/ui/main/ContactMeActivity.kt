package ar.com.utn.devmobile.servimatch.ui.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.com.utn.devmobile.servimatch.ui.model.Contacts
import ar.com.utn.devmobile.servimatch.ui.model.ProviderContacts
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import coil.compose.AsyncImage
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import ar.com.utn.devmobile.servimatch.ui.theme.Naranja
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura1

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactMe(navController: NavController, idProvider: String) {
    var providerContacts by remember { mutableStateOf<ProviderContacts?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        idProvider.let { providerId ->
            try {
                Log.d("CONTACT", "ID antes de ser inteado: $idProvider")
                val intedID = Integer.parseInt(providerId)
                Log.d("CONTACT", "ID luego de ser inteado: $intedID")
                val response = ApiClient.apiService.getProviderContacts(intedID)
                if (response.isSuccessful) {
                    providerContacts = response.body()
                    Log.d("CONTACT", "Response: $providerContacts")
                    isLoading = false
                } else {
                    Log.d("ERROR", "No se pudo obtener el proveedor con id $providerId")
                    providerContacts = CONTACTOS_MOCKEADOS
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Error al obtener el proveedor con id $providerId", e)
                providerContacts = CONTACTOS_MOCKEADOS
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Header(navController)
            providerContacts?.let { Body(it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(provider : ProviderContacts) {
    Banner(provider)
    DetallesContacto(provider)
}

@Composable
fun Banner(provider : ProviderContacts) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.height(250.dp)
    ){
        AsyncImage(
            model = provider.image,
            contentDescription = "Banner",
            modifier = Modifier
                .blur(15.dp, 15.dp)
                .background(color = Turquesa2, RectangleShape)
                .fillMaxWidth()
        )

        AsyncImage(
            model = provider.image,
            contentDescription = "Foto de perfil del ofertante",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(color = Turquesa3, CircleShape)
                .border(5.dp, Turquesa3, CircleShape),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun DetallesContacto(provider: ProviderContacts) {
    Text(
        text = "Detalles de contacto",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Purpura2,
        modifier = Modifier
            .padding(top = 20.dp)
    )
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        ContactDetailItem(Icons.Outlined.Person, "Nombre", "${provider.nombre} ${provider.apellido}")
        ContactDetailItem(Icons.Outlined.PinDrop, "Zonas", provider.area.joinToString(", "))
        ContactDetailItem(Icons.Outlined.Phone, "Telefono", provider.contactos.telefono)
        ContactDetailItem(Icons.Outlined.Email, "Email", provider.contactos.email)
    }
}

@Composable
fun ContactDetailItem(icon: ImageVector, dato: String, texto: String) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .scale(2f)
                .padding(horizontal = 15.dp)
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(horizontal = 50.dp)
        ) {
            Text(
                text = dato,
                fontWeight = FontWeight.Light,
                fontSize = 17.sp,
            )
            Text(
                text = texto,
                fontSize = 17.sp,
            )
        }
    }
    Divider()
}

//--------------------------------------------------------------//

val CONTACTOS_MOCKEADOS = ProviderContacts(
    nombre = "Melisa",
    apellido = "Perez",
    image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF5TcVFjPc_Z0ZdLUAA2Df6uTrJL1C5Al4-w&usqp=CAU",
    contactos = Contacts(
        email = "mperez@example.com",
        telefono = "+1234567890"
    ),
    area = listOf("Flores", "Caballito")
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewContactMe() {
    val navController = rememberNavController()
    ContactMe(navController = navController, idProvider = "2")
}