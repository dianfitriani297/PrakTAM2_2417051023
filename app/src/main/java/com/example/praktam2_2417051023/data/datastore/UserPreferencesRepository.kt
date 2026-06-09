package com.example.praktam2_2417051023.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "zoopedia_prefs"
)

class UserPreferencesRepository(
    private val context: Context
) {
    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_NAMA_LENGKAP = stringPreferencesKey("nama_lengkap")
        private val KEY_NAMA_PANGGILAN = stringPreferencesKey("nama_panggilan")
        private val KEY_PASSWORD = stringPreferencesKey("password")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        private fun formatKey(value: String): String {
            return value
                .trim()
                .lowercase()
                .replace(Regex("[^a-z0-9]+"), "_")
                .trim('_')
                .ifBlank { "pengguna" }
        }

        private fun keyUserNamaLengkap(userId: String) =
            stringPreferencesKey("user_${userId}_nama_lengkap")

        private fun keyUserNamaPanggilan(userId: String) =
            stringPreferencesKey("user_${userId}_nama_panggilan")

        private fun keyUserPassword(userId: String) =
            stringPreferencesKey("user_${userId}_password")

        private fun keyFavorit(userId: String, namaHewan: String) =
            booleanPreferencesKey("${userId}_favorit_${formatKey(namaHewan)}")

        private fun keySkor(userId: String, namaHewan: String) =
            intPreferencesKey("${userId}_skor_${formatKey(namaHewan)}")

        private fun keySelesai(userId: String, namaHewan: String) =
            booleanPreferencesKey("${userId}_selesai_${formatKey(namaHewan)}")
    }

    private suspend fun getUserId(): String {
        val prefs = context.dataStore.data.first()
        val userId = prefs[KEY_USER_ID]

        if (!userId.isNullOrBlank()) {
            return userId
        }

        val namaPanggilan = prefs[KEY_NAMA_PANGGILAN] ?: ""
        val namaLengkap = prefs[KEY_NAMA_LENGKAP] ?: ""
        val userIdBaru = formatKey(namaPanggilan.ifBlank { namaLengkap })

        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userIdBaru
        }

        return userIdBaru
    }

    suspend fun registerUser(
        namaLengkap: String,
        namaPanggilan: String,
        password: String
    ): Boolean {
        val userId = formatKey(namaPanggilan)
        val prefs = context.dataStore.data.first()

        if (prefs[keyUserNamaPanggilan(userId)] != null) {
            return false
        }

        context.dataStore.edit { preferences ->
            preferences[keyUserNamaLengkap(userId)] = namaLengkap
            preferences[keyUserNamaPanggilan(userId)] = namaPanggilan
            preferences[keyUserPassword(userId)] = password
            preferences[KEY_USER_ID] = userId
            preferences[KEY_NAMA_LENGKAP] = namaLengkap
            preferences[KEY_NAMA_PANGGILAN] = namaPanggilan
            preferences[KEY_PASSWORD] = password
            preferences[KEY_IS_LOGGED_IN] = false
        }

        return true
    }

    suspend fun loginUser(
        namaPanggilan: String,
        password: String
    ): Boolean {
        val userId = formatKey(namaPanggilan)
        val prefs = context.dataStore.data.first()
        val savedPassword = prefs[keyUserPassword(userId)]

        if (savedPassword == null || savedPassword != password) {
            return false
        }

        val namaLengkap = prefs[keyUserNamaLengkap(userId)] ?: ""
        val panggilan = prefs[keyUserNamaPanggilan(userId)] ?: namaPanggilan

        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_NAMA_LENGKAP] = namaLengkap
            preferences[KEY_NAMA_PANGGILAN] = panggilan
            preferences[KEY_PASSWORD] = savedPassword
            preferences[KEY_IS_LOGGED_IN] = true
        }

        return true
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data
            .map { prefs ->
                prefs[KEY_IS_LOGGED_IN] ?: false
            }
            .first()
    }

    suspend fun getNamaLengkap(): String {
        return context.dataStore.data
            .map { prefs ->
                prefs[KEY_NAMA_LENGKAP] ?: ""
            }
            .first()
    }

    suspend fun getNamaPanggilan(): String {
        return context.dataStore.data
            .map { prefs ->
                prefs[KEY_NAMA_PANGGILAN] ?: ""
            }
            .first()
    }

    suspend fun getPassword(): String {
        return context.dataStore.data
            .map { prefs ->
                prefs[KEY_PASSWORD] ?: ""
            }
            .first()
    }

    suspend fun updateNamaLengkap(nama: String) {
        val userId = getUserId()

        context.dataStore.edit { prefs ->
            prefs[KEY_NAMA_LENGKAP] = nama
            prefs[keyUserNamaLengkap(userId)] = nama
        }
    }

    suspend fun updateNamaPanggilan(nama: String) {
        val userIdLama = getUserId()
        val userIdBaru = formatKey(nama)
        val prefs = context.dataStore.data.first()
        val namaLengkap = prefs[KEY_NAMA_LENGKAP] ?: ""
        val password = prefs[KEY_PASSWORD] ?: ""

        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userIdBaru
            preferences[KEY_NAMA_PANGGILAN] = nama
            preferences[keyUserNamaLengkap(userIdBaru)] = namaLengkap
            preferences[keyUserNamaPanggilan(userIdBaru)] = nama
            preferences[keyUserPassword(userIdBaru)] = password

            preferences.remove(keyUserNamaLengkap(userIdLama))
            preferences.remove(keyUserNamaPanggilan(userIdLama))
            preferences.remove(keyUserPassword(userIdLama))
        }
    }

    suspend fun updatePassword(password: String) {
        val userId = getUserId()

        context.dataStore.edit { prefs ->
            prefs[KEY_PASSWORD] = password
            prefs[keyUserPassword(userId)] = password
        }
    }

    suspend fun setFavorit(
        namaHewan: String,
        isFavorit: Boolean
    ) {
        val userId = getUserId()

        context.dataStore.edit { prefs ->
            prefs[keyFavorit(userId, namaHewan)] = isFavorit
        }
    }

    suspend fun getFavorit(namaHewan: String): Boolean {
        val userId = getUserId()

        return context.dataStore.data
            .map { prefs ->
                prefs[keyFavorit(userId, namaHewan)] ?: false
            }
            .first()
    }

    suspend fun updateSkor(
        namaHewan: String,
        skorBaru: Int
    ) {
        val userId = getUserId()
        val skorLama = getSkor(namaHewan)

        if (skorBaru > skorLama) {
            context.dataStore.edit { prefs ->
                prefs[keySkor(userId, namaHewan)] = skorBaru
            }
        }
    }

    suspend fun getSkor(namaHewan: String): Int {
        val userId = getUserId()

        return context.dataStore.data
            .map { prefs ->
                prefs[keySkor(userId, namaHewan)] ?: 0
            }
            .first()
    }

    suspend fun setSelesai(namaHewan: String) {
        val userId = getUserId()

        context.dataStore.edit { prefs ->
            prefs[keySelesai(userId, namaHewan)] = true
        }
    }

    suspend fun isSelesai(namaHewan: String): Boolean {
        val userId = getUserId()

        return context.dataStore.data
            .map { prefs ->
                prefs[keySelesai(userId, namaHewan)] ?: false
            }
            .first()
    }
}