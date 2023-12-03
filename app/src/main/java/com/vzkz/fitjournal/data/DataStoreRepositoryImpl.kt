package com.vzkz.fitjournal.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vzkz.fitjournal.data.CONSTANTS.PREFERENCES_NAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.NICKNAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.UID
import com.vzkz.fitjournal.domain.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private object CONSTANTS{
    const val PREFERENCES_NAME = "my_preferences"
    object UserData {
        const val NICKNAME = "nickname"
        const val UID = "uid"
    }
}

private const val THEME = "theme"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {
    override suspend fun saveUserNicknameAndUid(nickname: String, uid: String) {
        saveUserField(NICKNAME, nickname)
        saveUserField(UID, uid)
    }

    private suspend fun saveUserField(key: String, value: String) {
        if (value != "") {
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun getUserNickname(): String {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(NICKNAME)] ?: ""
        }.first()
    }

    override suspend fun getUserUid(): String{
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(UID)] ?: ""
        }.first()
    }

    override suspend fun cleanUserFields() {
        deleteStringField(NICKNAME)
        deleteStringField(UID)
    }

    private suspend fun deleteStringField(key: String){
        context.dataStore.edit {preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    override suspend fun switchAppTheme() {
        context.dataStore.edit { preferences ->
            val value = preferences[booleanPreferencesKey(THEME)] ?: false
            preferences[booleanPreferencesKey(THEME)] = !value
        }
    }

    override suspend fun getAppTheme(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(THEME)] ?: false
        }
    }
}