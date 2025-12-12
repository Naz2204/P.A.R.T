package ua.ipze.kpi.part.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class PasswordStore(context: Context) {

    private val PASSWORD_KEY = "local_app_password"
    private val PREFERENCE_FILE_NAME = "local_secure_data"
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        PREFERENCE_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun savePassword(password: String) {
        sharedPrefs.edit {
            putString(PASSWORD_KEY, password)
        }
    }

    fun getPassword(): String? {
        return sharedPrefs.getString(PASSWORD_KEY, null)
    }

    fun clearPassword() {
        sharedPrefs.edit {
            remove(PASSWORD_KEY)
        }
    }
}