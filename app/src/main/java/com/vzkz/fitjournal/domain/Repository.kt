package com.vzkz.fitjournal.domain

import com.vzkz.fitjournal.domain.model.UserModel

interface Repository {

    suspend fun login(email: String, password: String): UserModel?

    suspend fun signUp(email: String, password: String, nickname: String): UserModel?

    suspend fun logout()

    fun isUserLogged(): Boolean

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel)

}