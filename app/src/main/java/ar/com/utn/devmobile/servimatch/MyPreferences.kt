package ar.com.utn.devmobile.servimatch

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
    var token: String? = null
    var username: String? = null

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
    }
}