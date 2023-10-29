package ar.com.utn.devmobile.servimatch.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa4

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContactMe(navController: NavController, username: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Header(navController)
        Body(username)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(username : String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Este Box ocupa la mitad del espacio del Row
                .padding(end = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Detalles de contacto",
                style = MaterialTheme.typography.headlineSmall,
                color = Turquesa4,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Datos de contacto
        ContactDetailItem("Nombre", username)
        ContactDetailItem("Correo Electrónico", "mperez@example.com")
        ContactDetailItem("Teléfono", "+1234567890")
        ContactDetailItem("Zona de trabajo", "Flores, Caballito")
    }
}

@Composable
fun ContactDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
        }
        Column {
            Text(text = value)
        }
    }
}