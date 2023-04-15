package `in`.jadu.anju.kvstorage

import android.content.Context
import android.content.SharedPreferences

class KvStorage(context:Context) {
    private val storage: SharedPreferences

    companion object{
        const val KV_PREF = "in.jadu.anju.kv.PREFERENCE_FILE_KEY"
    }

    fun storageSetString(key: String, value: String) {
        val editor = storage.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun storageGetString(key: String): String? {
        return storage.getString(key, "")
    }

    fun storageSetBoolean(s: String, b: Boolean) {
        val editor = storage.edit()
        editor.putBoolean(s, b)
        editor.apply()
    }

    fun storageGetBoolean(s: String): Boolean {
        return storage.getBoolean(s, false)
    }


    init {
        storage = context.getSharedPreferences(KV_PREF, Context.MODE_PRIVATE)
    }

}