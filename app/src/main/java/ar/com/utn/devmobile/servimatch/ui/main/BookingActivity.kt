package ar.com.utn.devmobile.servimatch.ui.main


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.AzulOscuro
import java.time.LocalDateTime
import java.time.ZoneId



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingScreen(navController: NavController, username: String, precioConsulta: String, disponibilidad: List<String>) {

    var turnoSelected by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // Variable para controlar la visibilidad del AlertDialog

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(navController)
        DatePickerSection()
        TurnoSection(turnoSelected, disponibilidad, onTurnoSelected = { nuevoTurno ->
            turnoSelected = nuevoTurno
            // Aquí puedes realizar cualquier acción adicional que necesites después de seleccionar un nuevo turno
            // Por ejemplo, imprimir el nuevo turno:
            Log.d("nuevo","Nuevo turno seleccionado: $nuevoTurno")
        })
        ReservarSection(turnoSelected, precioConsulta, showDialog) { showDialog = true }
    }

    // AlertDialog que se muestra cuando showDialog es true
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Se llama cuando el usuario toca fuera del AlertDialog
                showDialog = false
            },
            title = {
                Text("Reserva Creada: "+turnoSelected);
            },
            confirmButton = {
                Button(

                    onClick = {
                        // Puedes realizar acciones adicionales aquí si es necesario
                        showDialog = false
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Turquesa3,
                        contentColor = Color.White,
                        disabledContainerColor = Turquesa2,
                        disabledContentColor = Turquesa3
                    )
                ) {
                    Text("Aceptar")
                }
            },
            modifier = Modifier
                .border(width = 2.dp, color = Color.Black) // Agregar un borde negro al AlertDialog

        )


    }
}
@Composable
fun Header(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Turquesa1).zIndex(10f),
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Perfil Proovedor"
            )
        }
        Text(text = "Volver")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerSection() {
    Row(
        modifier = Modifier.fillMaxWidth().offset(y=(-35).dp).zIndex(-1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomDatePicker()
    }
}

@Composable
    fun TurnoSection(turnoSelected: String, disponibilidad: List<String>, onTurnoSelected: (String) -> Unit) {
    var selectedItem by remember { mutableStateOf(turnoSelected) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Button(
            onClick = {
                onTurnoSelected("Mañana")
                selectedItem="manana"
            },
            enabled = disponibilidad.any { it.equals("Mañana", ignoreCase = true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedItem === "manana") Purpura2 else Turquesa3,
                contentColor = Color.White,
                disabledContainerColor = Turquesa2,
                disabledContentColor = Turquesa3
            ),
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
                .width(130.dp)
        ) {
            Text(text = "Mañana")
        }
        Button(
            onClick = {
                onTurnoSelected("Tarde")
                selectedItem="tarde"
            },
            enabled = disponibilidad.any { it.equals("Tarde", ignoreCase = true) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedItem === "tarde") Purpura2 else Turquesa3,
                contentColor = Color.White,
                disabledContainerColor = Turquesa2,
                disabledContentColor = Turquesa3
            ),
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
                .width(130.dp)
        ) {
            Text(text = "Tarde")
        }
    }
}

@Composable
fun ReservarSection(turnoSelected: String, precioConsulta: String, showDialog: Boolean, onConfirm: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .border(width = 1.dp, color = Color.Black),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = paddingH, vertical = paddingV),
                    text = "$" + precioConsulta
                )
                Button(
                    onClick = {
                        // Llamamos a la función proporcionada cuando se presiona el botón
                        onConfirm()
                        Log.d("Muestro reserva",turnoSelected)
                    },
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Turquesa3,
                        contentColor = Color.White,
                        disabledContainerColor = Turquesa2,
                        disabledContentColor = Turquesa3
                    ),
                    modifier = Modifier
                        .padding(horizontal = paddingH, vertical = paddingV)
                ) {
                    Text(text = "Confirmar")
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(){
    val dateTime = LocalDateTime.now()
    val datePickerState = remember {
        DatePickerState(
            yearRange = (2023..2024),
            initialSelectedDateMillis = dateTime.toMillis(),
            initialDisplayMode = DisplayMode.Picker,
            initialDisplayedMonthMillis = null
        )
    }
    DatePicker(state = datePickerState)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()