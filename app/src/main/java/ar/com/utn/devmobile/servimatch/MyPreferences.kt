package ar.com.utn.devmobile.servimatch

import android.util.Log
import com.google.android.gms.maps.model.LatLng

class MyPreferences {
    companion object {
        private var instance: MyPreferences? = null
        fun getInstance(): MyPreferences {
            if (instance == null) {
                instance = MyPreferences()
            }
            return instance!!
        }
    }

    private val preferencesMap: MutableMap<String, Any?> = mutableMapOf()

    fun set(key: String, value: Any?) {
        Log.d("PREFERENCES", "SET -> KEY: $key, value: $value")
        preferencesMap[key] = value
    }

    fun get(key: String): Any? {
        Log.d("PREFERENCES", "GET -> KEY: $key")
        return preferencesMap[key]
    }
/*
    var token: String? = null
    var username: String? = null
    var userLatLong: LatLng? = null

    fun set(key: String, value: String) {
        when (key) {
            "token" -> token = value
            "username" -> username = value
            else -> throw IllegalArgumentException("Invalid key: $key")
        }
    }
    fun get(key: String): String? {
        return when (key) {
            "token" -> token
            "username" -> username
            else -> null
        }
    }*/
}