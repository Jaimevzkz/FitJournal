package com.vzkz.fitjournal.domain

import android.net.Uri
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import java.time.LocalDate

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

    fun addWorkout(user: UserModel, workoutModel: WorkoutModel): WorkoutModel

    fun deleteWorkout(uid: String, wid: String)

    fun updateSets(repList: List<SetXrepXweight>, uid: String, wid: String, exid: String)

    fun updateDate(uid: String, wotDates: List<Pair<LocalDate, String>>)

    suspend fun uploadPhoto(uri: Uri, uid: String): Uri

    suspend fun getExercisesByName(name: String): List<ExerciseModel>?

    suspend fun getUserFromRoom(nickname: String): UserModel

    suspend fun insertUserInRoom(userModel: UserModel)

    suspend fun updateUserInRoom(userModel: UserModel)
}