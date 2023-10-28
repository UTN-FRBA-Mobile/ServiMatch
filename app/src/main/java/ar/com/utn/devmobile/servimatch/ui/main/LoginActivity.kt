package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa5
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen( navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var paddingH = 16.dp
    var paddingV = 8.dp
    val painterIcon = painterResource(id = R.drawable.servimatch)
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var errorIcon by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Nuevo estado para la animación de carga
    val viewModelScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Box(modifier = Modifier.padding(horizontal = 0.dp, vertical = 32.dp)) {
            Image(painter = painterIcon, contentDescription = null)
        }
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                isButtonEnabled = it.isNotBlank() && password.isNotBlank()
            },
            isError = isError,
            label = { Text("Usuario") },
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Turquesa3, // Color del borde cuando el campo está enfocado
                unfocusedBorderColor = Turquesa5, // Color del borde cuando el campo no está enfocado
                focusedLabelColor = Turquesa3,
                textColor = Turquesa5
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isButtonEnabled = it.isNotBlank() && username.isNotBlank()
            },
            isError = isError,
            label = { Text("Contraseña") },
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            supportingText={ Text(errorMessage) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Turquesa3, // Color del borde cuando el campo está enfocado
                unfocusedBorderColor = Turquesa5, // Color del borde cuando el campo no está enfocado
                focusedLabelColor = Turquesa3,
                textColor = Turquesa5,
                errorSupportingTextColor = Color.Red
            )
        )
        //Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (!isLoading) { // Evita hacer otra solicitud mientras se está cargando
                    isLoading = true
                    isError = false
                    errorMessage = ""
                    // Simula una carga de 3 segundos
                    viewModelScope.launch {
                        //ACA TENGO QUE PEGARLE AL BACK.
                        delay(3000)
                        if (username == "Pablo25" && password == "pablo") {
                            // Navegar a la pantalla de inicio y pasar el usuario como argumento
                            navController.navigate(
                                route = "home/${username}"
                            )
                        } else {
                            isError = true
                            errorMessage = "Usuario y/o Contraseña incorrectos"
                        }
                        isLoading = false
                    }
                }
            },
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Turquesa3,
                contentColor = Color.White,
                disabledContainerColor = Turquesa2,
                disabledContentColor = Turquesa3
            ),
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
                .fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator() // Muestra la animación de carga
            } else {
                Text(text = "Iniciar sesión")
            }
        }
        Text(
            text = "Olvidaste la contraseña?",
            style = MaterialTheme.typography.bodySmall,
            color = Turquesa5,
            modifier = Modifier
                .padding(horizontal = paddingH, vertical = paddingV)
        )
    }

}

