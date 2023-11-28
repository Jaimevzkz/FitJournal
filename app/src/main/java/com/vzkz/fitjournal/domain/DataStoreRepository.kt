package com.vzkz.fitjournal.domain

import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUserNickname(nickname: String)

    suspend fun getUserNickname(): String

    suspend fun cleanUserNickname()

    suspend fun switchAppTheme()

    suspend fun getAppTheme(): Flow<Boolean>
}