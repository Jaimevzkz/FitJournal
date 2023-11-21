package com.vzkz.fitjournal.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vzkz.fitjournal.data.CONSTANTS.PREFERENCES_NAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.AGE
import com.vzkz.fitjournal.data.CONSTANTS.UserData.EMAIL
import com.vzkz.fitjournal.data.CONSTANTS.UserData.FIRSTNAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.GENDER
import com.vzkz.fitjournal.data.CONSTANTS.UserData.GOAL
import com.vzkz.fitjournal.data.CONSTANTS.UserData.LASTNAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.NICKNAME
import com.vzkz.fitjournal.data.CONSTANTS.UserData.UID
import com.vzkz.fitjournal.data.CONSTANTS.UserData.WEIGHT
import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private object CONSTANTS{
    const val PREFERENCES_NAME = "my_preferences"
    object UserData {
        const val UID = "uid"
        const val NICKNAME = "nickname"
        const val EMAIL = "email"
        const val FIRSTNAME = "firstname"
        const val LASTNAME = "lastname"
        const val WEIGHT = "weight"
        const val AGE = "age"
        const val GENDER = "gender"
        const val GOAL = "GOAL"
    }
}

private const val THEME = "theme"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepositoryImpl @Inject constructor(private val context: Context) :
    DataStoreRepository {
    override suspend fun saveUser(user: UserModel) {
        saveUserField(UID, user.uid)
        saveUserField(NICKNAME, user.nickname)
        saveUserField(EMAIL, user.email ?: "")
        saveUserField(FIRSTNAME, user.firstname)
        saveUserField(LASTNAME, user.lastname)
        saveUserField(WEIGHT, user.weight ?: "")
        saveUserField(AGE, user.age ?: "")
        saveUserField(GENDER, user.gender ?: "")
        saveUserField(GOAL, user.goal ?: "")
    }

    private suspend fun saveUserField(key: String, value: String) {
        if(value != ""){
            context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    override suspend fun getUser(): Flow<UserModel> {
        return context.dataStore.data.map { preferences ->
            UserModel(
                uid = preferences[stringPreferencesKey(UID)] ?: "",
                nickname = preferences[stringPreferencesKey(NICKNAME)] ?: "",
                email  = preferences[stringPreferencesKey(EMAIL)],
                firstname = preferences[stringPreferencesKey(FIRSTNAME)] ?: "",
                lastname  = preferences[stringPreferencesKey(LASTNAME)] ?: "",
                weight  = preferences[stringPreferencesKey(WEIGHT)],
                age = preferences[stringPreferencesKey(AGE)],
                gender = preferences[stringPreferencesKey(GENDER)],
                goal = preferences[stringPreferencesKey(GOAL)],
            )
        }
    }

    override suspend fun cleanUser() {
        deleteStringField(UID)
        deleteStringField(NICKNAME)
        deleteStringField(EMAIL)
        deleteStringField(FIRSTNAME)
        deleteStringField(LASTNAME)
        deleteStringField(WEIGHT)
        deleteStringField(AGE)
        deleteStringField(GENDER)
        deleteStringField(GOAL)
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