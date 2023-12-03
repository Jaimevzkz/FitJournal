package com.vzkz.fitjournal.domain

import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUserNicknameAndUid(nickname: String, uid: String)

    suspend fun getUserNickname(): String

    suspend fun getUserUid(): String

    suspend fun cleanUserFields()

    suspend fun switchAppTheme()

    suspend fun getAppTheme(): Flow<Boolean>
}