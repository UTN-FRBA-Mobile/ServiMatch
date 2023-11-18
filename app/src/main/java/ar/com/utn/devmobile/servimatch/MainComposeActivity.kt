package ar.com.utn.devmobile.servimatch


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.com.utn.devmobile.servimatch.ui.main.BookingScreen
import ar.com.utn.devmobile.servimatch.ui.main.ContactMe
import ar.com.utn.devmobile.servimatch.ui.main.HomeScreen
import ar.com.utn.devmobile.servimatch.ui.main.LoginScreen
import ar.com.utn.devmobile.servimatch.ui.main.ProfileScreen
import ar.com.utn.devmobile.servimatch.ui.main.MapScreen
import ar.com.utn.devmobile.servimatch.ui.main.SharedViewModel

class MainComposeActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(sharedViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun App(sharedViewModel: SharedViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(
            route = "map"
        ) {
            MapScreen(navController = navController)
        }

        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(navController = navController,username = username)
        }

        composable(
            route = "profile/{idProveedor}"
        ) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = "booking/{username}/{precio}/{disponibilidad}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType },
                navArgument("disponibilidad") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val precio = backStackEntry.arguments?.getString("precio") ?: ""
            val disponibilidadString  = backStackEntry.arguments?.getString("disponibilidad") ?: ""
            // Convierte la cadena de "disponibilidad" en un array de cadenas
            val disponibilidad = disponibilidadString.split(",").map { it.trim() }

            BookingScreen(navController = navController, sharedViewModel = sharedViewModel , username = username, precioConsulta = precio, disponibilidad = disponibilidad)
        }

        composable(
            route = "contactMe/{providerId}",
        ) {
            ContactMe(navController = navController)
        }
    }
}