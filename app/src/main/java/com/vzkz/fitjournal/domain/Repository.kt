package com.vzkz.fitjournal.domain

import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel

interface Repository {

    suspend fun login(email: String, password: String): UserModel?

    suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String
    ): UserModel

    suspend fun logout()

    fun isUserLogged(): Boolean

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel)

    fun addWorkout(user: UserModel, workoutModel: WorkoutModel)

    suspend fun getExercisesByName(name: String): List<ExerciseModel>?

    suspend fun getUserFromRoom(nickname: String): UserModel

    suspend fun insertUserInRoom(userModel: UserModel)
}