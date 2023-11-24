package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen(navController: NavController) {
    var direccion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Turquesa1)
    ) {
        Column {

            TopBar(navController = navController, direccion = direccion, onChange = { value -> direccion = value })

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Turquesa1)
                    .fillMaxSize()
                    .fillMaxWidth()
                    .padding(horizontal = paddingH, vertical = paddingV)
            ){

                GoogleMap {

                }
            }
        }
    }

}

@Composable
fun TopBar(navController: NavController, direccion: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Turquesa1)
            .zIndex(10f),
        verticalAlignment = Alignment.CenterVertically

    ) {
        IconButton(
            onClick = { navController.navigateUp() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Perfil Proovedor"
            )
        }
        OutlinedTextField (
            value = direccion,
            onValueChange = { onChange(it) },
            modifier = Modifier
                .padding(10.dp)
                .background(Turquesa1),
            leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "") }
        )
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowMapScreen() {
    val navController = rememberNavController()
    MapScreen(navController = navController)
}
