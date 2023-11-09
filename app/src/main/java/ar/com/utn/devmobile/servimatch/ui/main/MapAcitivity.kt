package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap()
    }
}