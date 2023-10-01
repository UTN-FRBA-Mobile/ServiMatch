package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.TurquesaTituloHome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( navController: NavController,username: String) {
    var paddingH = 12.dp
    var paddingV = 15.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1)
            .padding(horizontal = paddingH, vertical = paddingV)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
             Header(username, paddingH, paddingV)
             Spacer(modifier = Modifier.height(25.dp)) // Espacio entre los filtros
             Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Filters(modifier = Modifier.weight(0.7f)) // Primer conjunto de filtros
                Spacer(modifier = Modifier.width(10.dp)) // Espacio entre los filtros
                Filters(modifier = Modifier.weight(0.7f)) // Segundo conjunto de filtros
                Spacer(modifier = Modifier.width(10.dp)) // Espacio entre los filtros
                Filters(modifier = Modifier.weight(0.7f)) // Tercer conjunto de filtros

            }


        }
    }
}

@Composable
fun Filters(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedZone by remember { mutableStateOf("Zona") }

    val zones = listOf("Barrio 1", "Barrio 2", "Barrio 3")

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
                    Text(selectedZone, fontWeight = FontWeight.Bold)
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
        zones.forEach { zone ->
            DropdownMenuItem(
                onClick = {
                    selectedZone = zone
                    expanded = false
                },
                text = { Text(zone, color = Color.Black) }
            )
        }
    }
}


@Composable
fun Header(username: String, paddingH: Dp, paddingV: Dp) {
    // Convertir el primer carácter a mayúscula y el resto a minúscula
    val formattedUsername = username.lowercase().replaceFirstChar { it.uppercase() }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Este Box ocupa la mitad del espacio del Row
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Bienvenido de nuevo, $formattedUsername",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TurquesaTituloHome,
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


