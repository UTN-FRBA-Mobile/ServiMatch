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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ar.com.utn.devmobile.servimatch.MyPreferences
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import ar.com.utn.devmobile.servimatch.ui.model.ReservaRequest
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@ExperimentalMaterial3Api
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingScreen(navController: NavController, idProveedor: Int, precioConsulta: String, disponibilidad: List<String>) {
    var turnoSelected by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) } // Variable para controlar la visibilidad del AlertDialog
    val dateState = rememberDatePickerState(
        yearRange = (2023..2024),
        initialSelectedDateMillis = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker,
        initialDisplayedMonthMillis = null
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(navController)
        DatePickerSection(dateState, idProveedor)
        TurnoSection(turnoSelected, disponibilidad, onTurnoSelected = { nuevoTurno ->
            turnoSelected = nuevoTurno
            // Aquí puedes realizar cualquier acción adicional que necesites después de seleccionar un nuevo turno
            // Por ejemplo, imprimir el nuevo turno:
            Log.d("nuevo","Nuevo turno seleccionado: $nuevoTurno")
        })
        ReservarSection(precioConsulta) { showDialog = true }
        if (showDialog) {
            var result = false
            if (turnoSelected != "") {
                val fecha = millisToDate(dateState.selectedDateMillis)
                val cliente = MyPreferences.getInstance().get("username")?:""
                val request = ReservaRequest(turnoSelected, fecha, cliente, precioConsulta)
                Log.d("reserva", "ENVIANDO AL BACK")
                Log.d("Turno reserva", turnoSelected)
                Log.d("Fecha reserva", millisToDate(dateState.selectedDateMillis))
                Log.d("Username reserva", cliente)
                Log.d("Precio consulta reserva", precioConsulta)
                Log.d("Id proveedor reserva", idProveedor.toString())
                result = sendRequest(request, idProveedor)
            }
            ShowAlertDialog(result, turnoSelected) { showDialog = false }
        }
    }
}


@Composable
fun Header(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Turquesa1)
            .zIndex(10f),
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
fun ShowAlertDialog(result: Boolean, turnoSelected: String, toggleDialog: () -> Unit) {
    val text = when {
        turnoSelected.isBlank() -> "Debe seleccionar un turno"
        !result -> "Hubo un error al crear la reserva"
        else -> "Reserva Creada: $turnoSelected"
    }

    AlertDialog(
        onDismissRequest = {
            toggleDialog()
        },
        title = {
            Text(text);
        },
        confirmButton = {
            Button(
                onClick = {
                    toggleDialog()
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerSection(dateState: DatePickerState, idProveedor: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-35).dp)
            .zIndex(-1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomDatePicker(dateState, idProveedor)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservarSection(precioConsulta: String, onConfirm: () -> Unit) {

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
                        onConfirm()
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
fun CustomDatePicker(dateState: DatePickerState, idProveedor: Int){
    var fechas by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        MainScope().launch {
            val response = ApiClient.apiService.getProvidersUnvailableDays(idProveedor)
            if (response.isSuccessful) {
                fechas = response.body()?:emptyList()
                isLoading = false
            } else {
                Log.d("FECHAS", "Error al hacer la peticion de las fechas")
            }
        }
    }

    if (isLoading) {
        Row(
            modifier = Modifier.fillMaxWidth().offset(y = (-35).dp).zIndex(-1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator()
        }
    } else {
        DatePicker(
            state = dateState,
            dateValidator = { timestamp -> validateDays(timestamp, fechas) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun validateDays(timestamp: Long, fechas: List<String>): Boolean {
    val today = LocalDate.now()
    val selectedLocalDate = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    if (fechas.isEmpty()) {
        Log.d("FECHAS", "La lista de fechas esta vacia: $fechas")
        return !(selectedLocalDate.isBefore(today))
    }

    Log.d("FECHAS", "$fechas")
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val fechasFormateadas: List<LocalDate> = fechas.map { LocalDate.parse(it, formatter) }

    return !(selectedLocalDate.isBefore(today) || fechasFormateadas.contains(selectedLocalDate))
}

@RequiresApi(Build.VERSION_CODES.O)
fun millisToDate(millis: Long?): String {
    millis?.let { nonNullMillis ->
        val localDate = Instant.ofEpochMilli(nonNullMillis).atZone(ZoneOffset.UTC).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return localDate.format(formatter)
    } ?: run {
        return "Fecha no seleccionada"
    }
}

@Composable
fun sendRequest(request: ReservaRequest, idProveedor: Int): Boolean {
    var isRequestSuccessful by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.apiService.createReserva(idProveedor, request)
            isRequestSuccessful = response.isSuccessful
        } catch(e: Exception) {
            Log.d("ERROR", "Error al crear reserva: $e")
            isRequestSuccessful = false
        }
    }
    return isRequestSuccessful
}

