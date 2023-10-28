package ar.com.utn.devmobile.servimatch.ui.main


import android.util.Log
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
import ar.com.utn.devmobile.servimatch.ui.model.ApiService
import ar.com.utn.devmobile.servimatch.ui.model.LoginRequest
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa5
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_URL = "http://localhost:5000" // Reemplaza con la URL de tu backend

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen( navController: NavController) {
    val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)

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
                if (!isLoading) {
                    isLoading = true
                    isError = false
                    errorMessage = ""

                    val loginRequest = LoginRequest(username, password)

                    viewModelScope.launch {
                        try {
                            val response = apiService.login(loginRequest)

                            if (response.isSuccessful) {
                                // La solicitud fue exitosa, puedes procesar la respuesta aquí
                                val result = response.body() // Esto es tu modelo de respuesta
                                // Procesa el resultado como desees
                            } else {
                                isError = true
                                errorMessage = "Usuario y/o Contraseña incorrectos"
                            }
                        } catch (e: Exception) {
                            isError = true
                            errorMessage = "Error en la solicitud"
                            Log.d("ERROR", e.toString());

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

