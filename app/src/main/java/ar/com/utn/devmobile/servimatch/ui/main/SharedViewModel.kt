package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/*
* Esta clase es para poder pasar datos entre pantallas
* que no puedan ser enviadas por el path.
* */
class SharedViewModel : ViewModel() {
    private val _username = mutableStateOf("")
    val username: State<String> = _username

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }
}

