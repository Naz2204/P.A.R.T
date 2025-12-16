package ua.ipze.kpi.part.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class PrefManager(context: Context) {
    private val LANGUAGE_KEY = stringPreferencesKey("language_preference")
    val DEFAULT_LANGUAGE = "en"
    private val dataStore = context.dataStore

    fun getLanguagePreference(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_KEY] ?: DEFAULT_LANGUAGE
            }
    }

    suspend fun setLanguagePreference(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }
}