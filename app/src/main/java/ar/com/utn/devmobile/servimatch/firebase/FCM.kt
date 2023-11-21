package ar.com.utn.devmobile.servimatch.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import ar.com.utn.devmobile.servimatch.MainComposeActivity
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.MyPreferences
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCM : FirebaseMessagingService() {

    fun saveTokenInPreferences() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                if (token != null) {
                    MyPreferences.getInstance().set("token", token)
                } else {
                    Log.w("FCM", "El token es nulo")
                }
            } else {
                Log.w("FCM", "Error al obtener el token", task.exception)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var idProvider = ""
        Log.d("FCM", "Llega un mensaje")
        if (remoteMessage.data.isNotEmpty()) {
            idProvider = remoteMessage.data["id_proveedor"]?:""
            Log.d("FCM", "ID_PROVEEDOR $idProvider")
        }
        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            Log.d("FCM", "Titulo: $title, Cuerpo: $body")
            showNotification(it, idProvider)
        }
    }

    override fun onNewToken(newToken: String) {
        Log.d("FCM", newToken)
        MyPreferences.getInstance().set("token", newToken)
        super.onNewToken(newToken)
    }

    private fun showNotification(notification: RemoteMessage.Notification, idProvider: String) {
        val intent = Intent(this, MainComposeActivity::class.java).apply {
            putExtra("destination", "contactMe/${idProvider}")
        }
        val requestCode = idProvider.toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true) .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    /*private fun showNotification(notification: RemoteMessage.Notification, idProvider: String) {
        val intent = Intent(this, MainComposeActivity::class.java).apply {
            putExtra("destination", "contactMe/${idProvider}")
        }
        Log.d("INITIAL_ROUTE", "En ShowNotification: $idProvider")
        /*if (route != "") {
            intent.putExtra("destination", route)
        }*/
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.img)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }*/

    companion object {
        private val CHANNEL_ID = "CHANNEL_1"
    }
}
