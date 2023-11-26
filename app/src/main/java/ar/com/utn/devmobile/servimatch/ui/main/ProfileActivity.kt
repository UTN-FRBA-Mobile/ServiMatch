package ar.com.utn.devmobile.servimatch.ui.main

import android.service.autofill.OnClickAction
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa3
import ar.com.utn.devmobile.servimatch.ui.theme.Naranja
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import ar.com.utn.devmobile.servimatch.ui.model.ApiClient
import ar.com.utn.devmobile.servimatch.ui.model.Comentario
import ar.com.utn.devmobile.servimatch.ui.model.ProviderInfo
import ar.com.utn.devmobile.servimatch.ui.model.ProviderProfile
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa1
import ar.com.utn.devmobile.servimatch.ui.theme.Purpura2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa2
import ar.com.utn.devmobile.servimatch.ui.theme.Turquesa5

@Composable
fun ProfileScreen(navController: NavController) {
    var providerProfile by remember { mutableStateOf<ProviderProfile?>(PERSONAMOCKEADA) }
    val arguments = navController.currentBackStackEntry?.arguments
    val idProveedor = arguments?.getString("idProveedor")
    val id = idProveedor?.toInt()
    LaunchedEffect(Unit) {
        val response = id?.let { ApiClient.apiService.getProvider(it) }

        if (response != null) {
            providerProfile = if (response.isSuccessful) {
                response.body()
            } else {
                Log.d("ERROR", "No se pudo obtener el proveedor con id $idProveedor")
                PERSONAMOCKEADA
            }
        }
    }

        Column {
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

            providerProfile?.let { ProviderProfileContent(navController, it) }
        }
    }



@Composable
fun ProviderProfileContent(navController: NavController, profile: ProviderProfile) {
    val promedioPuntajes = (profile.comentarios.map {it.puntaje}.average() * 10).toInt() / 10.0
    val cantComentarios = profile.comentarios.size

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Turquesa1)
            .fillMaxSize()
            .fillMaxWidth()
            .padding(horizontal= paddingH,vertical=paddingV)
    ){

        PersonalInfo(profile.imagePath, profile.nombre, profile.apellido, profile.profesion, profile.ubicaciones,navController)
        Puntajes(profile.serviciosCompletados, promedioPuntajes, cantComentarios)
        BotonesAcciones(navController, profile)
        Reseñas(profile.comentarios)
    }
}

@Composable
fun PersonalInfo(foto: String, nombre: String, apellido: String, profesion: String, ubicaciones: List<String>, navController: NavController) {
    Column {
            Row(
                modifier = Modifier.padding(top = 4.dp)
            ) {
                AsyncImage(
                    model = foto,
                    contentDescription = "Foto de perfil del ofertante",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(color = Turquesa3, CircleShape)
                        .border(5.dp, Turquesa3, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "$nombre $apellido",
                        color = Turquesa3,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = profesion,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Place, contentDescription = "")
                        Text(text = ubicaciones.joinToString(", "))
                    }
                }
            }
        }
}




@Composable
fun Puntajes(servicios_completados: Int, puntaje: Number, comentarios: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingH, vertical= paddingV),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        PuntajeItem(servicios_completados, "Servicios completados",  modifier = Modifier.weight(0.5f))
        PuntajeItem(puntaje, "Puntaje",  modifier = Modifier.weight(0.5f))
        PuntajeItem(comentarios, "Comentarios",  modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun PuntajeItem(
    puntaje: Number,
    texto: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = puntaje.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Turquesa3,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Center // Alinea el texto al centro
        )
        Text(
            text = texto,
            modifier = Modifier
                .align(CenterHorizontally),
            textAlign = TextAlign.Center // Alinea el texto al centro
        )
    }
}



@Composable
fun BotonesAcciones(navController : NavController, persona: ProviderProfile) {
    val disponibilidad = persona.disponibilidad
    //laburo necesario que hago para pasar la lista y que no se reciba anidada dentro de otra lista.
    val disponibilidadStr = disponibilidad.joinToString(",")

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Boton(texto = "Reservar", onClick = {navController.navigate(route = "booking/${persona.id}/${persona.precio_visita}/$disponibilidadStr")})
        Spacer(modifier = Modifier.width(20.dp))
        Boton(texto = "Contactame", onClick = {navController.navigate(route = "contactMe/${persona.id}")})
    }
}

@Composable
fun Boton(texto: String, onClick: ()->Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Turquesa3,
            contentColor = Color.White,
        )
    ) {
        Text(
            text = texto
        )
    }
}

@Composable
fun Reseñas(comentarios: List<Comentario>) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
        .padding(vertical= paddingV),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Reseñas",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        Divider()
        LazyColumn() {
            itemsIndexed(comentarios) { index, comentario ->
                ReseñaItem(
                    url = comentario.foto,
                    nombre = comentario.nombreUsuario,
                    comentario = comentario.comentario,
                    fecha = comentario.fecha,
                    estrellas = comentario.puntaje
                )
            }
        }
    }
}

@Composable
fun ReseñaItem(url: String, nombre: String, comentario: String, fecha: String, estrellas: Double) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ){
        Box(modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color = Turquesa3, CircleShape)
            .border(1.dp, Turquesa3, CircleShape)
        ) {
            AsyncImage(
                model = url,
                contentDescription = "Comentario de $nombre",
                modifier = Modifier
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = nombre,
                color = Purpura2,
                fontSize = 15.sp,
                //fontWeight = FontWeight.SemiBold
            )
            Text(
                text = comentario,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = fecha
            )
        }
        RatingBar(rating = estrellas)
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = Naranja,
) {
    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))
    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = null,
                tint = starsColor
            )
        }
        if (halfStar) {
            Icon(
                imageVector = Icons.Outlined.StarHalf,
                contentDescription = null,
                tint = starsColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = starsColor
            )
        }
    }
}

/*ESTO ES PARA PROBAR, DUMMY DATA*/
val comentariosCuidadoraMascotas = listOf(
    Comentario("https://picsum.photos/id/237/300/300", "Firulais", "guau guau!!", "03/10/2023", 4.0),
    Comentario("https://picsum.photos/id/251/300/300", "Roberto", "ok, todo bien", "04/10/2023", 4.5),

)

val PERSONAMOCKEADA = ProviderProfile(
    id=5,
    imagePath = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF5TcVFjPc_Z0ZdLUAA2Df6uTrJL1C5Al4-w&usqp=CAU",
    nombre="Melisa",
    apellido= "Perez",
    profesion="Cuidadora de Mascotas",
    cantSignosPesos= 3,
    ubicaciones=listOf("Flores", "Caballito"),
    disponibilidad=listOf("mañana"),
    serviciosCompletados = 30,
    comentarios= comentariosCuidadoraMascotas,
    precio_visita="3870"
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowProfilePreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}
