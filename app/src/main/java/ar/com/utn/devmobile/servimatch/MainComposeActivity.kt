package ar.com.utn.devmobile.servimatch


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.com.utn.devmobile.servimatch.ui.main.HomeScreen
import ar.com.utn.devmobile.servimatch.ui.main.LoginScreen
import ar.com.utn.devmobile.servimatch.ui.main.ProfileScreen
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}



@Composable
private fun App() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile/5") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(navController = navController,username = username)
        }
        composable(
            route = "profile/{idProveedor}",
            arguments = listOf(navArgument("idProveedor") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("idProveedor") ?: 1
            ProfileScreen(navController = navController, idProveedor = id)
        }
    }
}