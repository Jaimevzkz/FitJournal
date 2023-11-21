package com.vzkz.fitjournal.domain

import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveUser(user: UserModel)

    suspend fun getUser(): Flow<UserModel>

    suspend fun cleanUser()

    suspend fun switchAppTheme()

    suspend fun getAppTheme(): Flow<Boolean>
}