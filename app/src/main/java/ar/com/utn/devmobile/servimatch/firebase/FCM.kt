package ar.com.utn.devmobile.servimatch.ui.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import ar.com.utn.devmobile.servimatch.MainComposeActivity
import ar.com.utn.devmobile.servimatch.R
import ar.com.utn.devmobile.servimatch.ui.main.SharedViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCM : FirebaseMessagingService() {
    fun saveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result?.token
                Log.d("FCM", "EL TOKEN DE 'saveToken' es $token")
                SharedViewModel().updateToken(token)
            } else {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Llega un mensaje")
        if (remoteMessage.data.isNotEmpty()) {
            val idProveedor = remoteMessage.data["provider_id"]
            Log.d("FCM", "ID del Proovedor: $idProveedor")
        }
        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            Log.d("FCM", "Titulo: $title, Cuerpo: $body")
            showNotification(it)
        }
    }

    override fun onNewToken(newToken: String) {
        Log.d("FCM", newToken)
        SharedViewModel().updateToken(newToken)
        super.onNewToken(newToken)
    }

    private fun showNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainComposeActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

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
    }

    companion object {
        private val CHANNEL_ID = "CHANNEL_1"
    }
}