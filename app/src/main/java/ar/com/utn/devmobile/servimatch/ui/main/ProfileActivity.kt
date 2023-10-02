package ar.com.utn.devmobile.servimatch.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.outlined.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun ProfileScreen( navController: NavController, idPersona: String) {
    val persona = personaCuidadoraMascotas //no uso la ID por ahora
    val promedioPuntajes = (comentariosCuidadoraMascotas.map { it.puntaje }.average() * 10).toInt() / 10.0
    val cantComentarios = persona.comentarios.size

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        PersonalInfo(persona.foto, persona.nombre, persona.profesion, persona.ubicaciones)
        Puntajes(persona.servicios_completados, promedioPuntajes, cantComentarios)
        BotonesAcciones()
        Reseñas(persona.comentarios)
    }
}

@Composable
fun PersonalInfo(foto: String, nombre: String, profesion: String, ubicaciones: List<String>) {
    Column () {
        AsyncImage(
            model = foto,
            contentDescription = "Foto de perfil del ofertante",
            modifier = Modifier
                .align(CenterHorizontally)
                .size(140.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = nombre,
            color = Turquesa3,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally)
        )
        Text(
            text = profesion,
            fontSize = 20.sp,
            modifier = Modifier
                .align(CenterHorizontally)
        )
        Row(verticalAlignment = CenterVertically){
            Icon(imageVector = Icons.Default.Place, contentDescription = "")
            Text(
                text = ubicaciones.joinToString(", ")
            )
        }
    }
}

@Composable
fun Puntajes(servicios_completados: Int, puntaje: Number, comentarios: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        PuntajeItem(servicios_completados, "Servicios completados")
        PuntajeItem(puntaje, "Puntaje")
        PuntajeItem(comentarios, "Comentarios")
    }
}

@Composable
fun PuntajeItem(puntaje: Number, texto: String) {
    Column {
        Text(
            text = puntaje.toString(),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Turquesa3,
            modifier = Modifier
                .align(CenterHorizontally),
        )
        Text(
            text = texto
        )
    }
}

@Composable
fun BotonesAcciones() {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Boton("Reservar")
        Boton("Contactame")
    }
}

@Composable
fun Boton(texto: String) {
    Button(
        onClick = { /*TODO*/ },
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
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Reseñas",
            fontWeight = FontWeight.SemiBold
        )
        Divider()
        LazyColumn() {
            itemsIndexed(comentarios) { index, comentario ->
                ReseñaItem(
                    url = comentario.foto,
                    nombre = comentario.nombre,
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ){
        AsyncImage(
            model = url,
            contentDescription = "Comentario de $nombre",
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = nombre,
                color = Color.LightGray,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = comentario,
                fontSize = 20.sp,
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
    starsColor: Color = Color.Yellow,
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
data class Persona(
    val id: Int,
    val foto: String,
    val nombre: String,
    val profesion: String,
    val ubicaciones: List<String>,
    val servicios_completados: Int,
    val comentarios: List<Comentario>
)

data class Comentario(
    val foto: String,
    val nombre: String,
    val comentario: String,
    val fecha: String,
    val puntaje: Double
)

val comentariosCuidadoraMascotas = listOf(
    Comentario("https://picsum.photos/id/250/100/100", "Firulais", "guau guau!!", "03/10/2023", 4.0),
    Comentario("https://picsum.photos/id/251/100/100", "Roberto", "ok, todo bien", "04/10/2023", 4.5),
    Comentario("https://picsum.photos/id/252/100/100", "Fluffy", "Me cobro caro, esperaba algo mas economico debido a que solamente lo cuido por unos minutos", "05/10/2023", 3.0),
    Comentario("https://picsum.photos/id/253/100/100", "Luna", "Perfecto", "06/10/2023", 4.0),
    Comentario("https://picsum.photos/id/254/100/100", "Maximiliano", "Excelente servicio", "07/10/2023", 5.0),
    Comentario("https://picsum.photos/id/255/100/100", "Oliver", "Volvio peinado, perfecto", "08/10/2023", 3.5),
    Comentario("https://picsum.photos/id/256/100/100", "Roxana", "El perro vino lleno de tierra, un desastre, nunca mas!", "09/10/2023", 2.0)
)

val personaCuidadoraMascotas = Persona(
    id=5,
    foto = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQF5TcVFjPc_Z0ZdLUAA2Df6uTrJL1C5Al4-w&usqp=CAU",
    nombre="Melisa Perez",
    profesion="Cuidadora de Mascotas",
    ubicaciones=listOf("Flores", "Caballito"),
    servicios_completados = 30,
    comentarios= comentariosCuidadoraMascotas
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShowProfilePreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController, idPersona = "5")
}
