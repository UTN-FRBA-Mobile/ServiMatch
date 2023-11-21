package ar.com.utn.devmobile.servimatch


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.com.utn.devmobile.servimatch.firebase.FCM
import ar.com.utn.devmobile.servimatch.ui.main.BookingScreen
import ar.com.utn.devmobile.servimatch.ui.main.ContactMe
import ar.com.utn.devmobile.servimatch.ui.main.HomeScreen
import ar.com.utn.devmobile.servimatch.ui.main.LoginScreen
import ar.com.utn.devmobile.servimatch.ui.main.ProfileScreen
import ar.com.utn.devmobile.servimatch.ui.main.MapScreen



class MainComposeActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FCM().saveTokenInPreferences()
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        Log.d("INIT_ROUTE", "ENTRO AL onNewIntent")
        super.onNewIntent(intent)
        val destination = intent.getStringExtra("destination")
        if (destination != null) {
            Log.d("INIT_ROUTE", destination)
            navController.navigate(destination)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun App() {
    navController = rememberNavController()
    NavHost(navController = navController as NavHostController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController = navController)
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
            route = "booking/{proveedor}/{precio}/{disponibilidad}",
            arguments = listOf(
                navArgument("proveedor") { type = NavType.StringType },
                navArgument("precio") { type = NavType.StringType },
                navArgument("disponibilidad") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("proveedor")?: ""
            val precio = backStackEntry.arguments?.getString("precio") ?: ""
            val disponibilidadString  = backStackEntry.arguments?.getString("disponibilidad") ?: ""
            // Convierte la cadena de "disponibilidad" en un array de cadenas
            val disponibilidad = disponibilidadString.split(",").map { it.trim() }
            val proovedor = id.toInt()

            BookingScreen(navController = navController , idProveedor = proovedor, precioConsulta = precio, disponibilidad = disponibilidad)
        }

        composable(
            route = "contactMe/{providerId}",
            arguments = listOf(navArgument("providerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("providerId") ?: ""
            Log.d("CONTACT", "ID en string: $id")
            ContactMe(navController = navController, idProvider = id)
        }
    }
}

lateinit var navController: NavController