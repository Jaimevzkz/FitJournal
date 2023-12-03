package com.vzkz.fitjournal.data

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.data.database.dao.UserDao
import com.vzkz.fitjournal.data.firebase.AuthService
import com.vzkz.fitjournal.data.firebase.FirestoreService
import com.vzkz.fitjournal.data.network.ExerciseApiService
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val context: Context,
    private val exerciseApiService: ExerciseApiService,
    private val roomDB: UserDao
) : Repository {

    //Firestore
    override suspend fun login(email: String, password: String): UserModel? {
        val user: FirebaseUser?
        try{
            user = authService.login(email, password)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.wrong_email_or_password))
        }
        if(user != null) {
            val userData: UserModel
            try {
                //TODO eliminar solo pruebas
//                firestoreService.insertUser(USERMODELFORTESTS)

                userData = firestoreService.getUserData(user.uid)
            } catch (e: Exception) {
                when (e.message) {
                    "NF" -> throw Exception(context.getString(R.string.network_failure_while_checking_user_existence))
                    "CF" -> throw Exception(context.getString(R.string.error_converting_firestore_response_to_usermodel))
                    else -> throw Exception(context.getString(R.string.couldn_t_find_the_user))
                }
            }
            return user.toDomain(userData)
        }
        return null
    }

    override suspend fun signUp(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String
    ): UserModel {
        if (firestoreService.userExists(nickname)) {
            throw Exception(context.getString(R.string.username_already_in_use))
        } else {
            val user: UserModel?
            try {
                val signup = authService.signUp(email, password)
                if (signup != null) {
                    user = signup.toDomain(
                        UserModel(
                            uid = signup.uid,
                            nickname = nickname,
                            email = signup.email,
                            firstname = firstname,
                            lastname = lastname
                        )
                    )
                } else {
                    throw Exception()
                }
            } catch (e: Exception){
                throw Exception(context.getString(R.string.account_already_exists))
            }
            try{
                firestoreService.insertUser(user) //At this point, user should never be null
            } catch (e: Exception){
                throw Exception(context.getString(R.string.couldn_t_insert_user_in_database))
            }
            return user
        }
    }

    override suspend fun logout() = authService.logout()

    override fun isUserLogged() = authService.isUserLogged()

    override suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel) {
        try {
            firestoreService.modifyUserData(oldUser = oldUser, newUser = newUser)
        } catch (e: Exception) {
            if (e.message == "NF") throw Exception(context.getString(R.string.error_modifying_user_data_the_user_wasn_t_modified))
            else throw Exception(context.getString(R.string.username_already_in_use_couldn_t_modify_user))
        }
    }

    override fun addWorkout(user: UserModel, workoutModel: WorkoutModel): WorkoutModel{
        return firestoreService.addWorkout(user, workout = workoutModel)
    }

    override fun deleteWorkout(uid: String, wid: String){
        firestoreService.deleteWorkout(uid = uid, wid = wid)
    }

    private fun FirebaseUser.toDomain(userData: UserModel): UserModel {
        return UserModel(
            uid = this.uid,
            nickname = userData.nickname,
            email = this.email,
            firstname = userData.firstname,
            lastname = userData.lastname,
            weight = userData.weight,
            age = userData.age,
            gender = userData.gender,
            goal = userData.goal,
            workouts = userData.workouts
        )
    }

    override fun updateSets(repList: List<SetXrepXweight>, uid: String, wid: String, exid: String){
        firestoreService.updateSets(repList = repList, uid = uid, wid = wid, exid= exid)
    }


    //Retrofit
    override suspend fun getExercisesByName(name: String): List<ExerciseModel>? {
        runCatching {
            exerciseApiService.getExerciseByName(name)
        }
            .onSuccess {
                return it.map { exResponseItem ->
                    exResponseItem.toDomain()
                }
            }
            .onFailure {
                Log.i("Jaime", "An error occurred while using apiService, ${it.message}")
                throw Exception(context.getString(R.string.an_error_occurred_while_using_apiservice))
            }
        return null
    }

    //Room
    override suspend fun getUserFromRoom(nickname: String): UserModel {
        val userFromRoom = roomDB.getUser(nickname)

        return userFromRoom?.toDomain()
            ?: throw Exception("Error getting user from Room DataBase")
    }

    override suspend fun insertUserInRoom(userModel: UserModel) {
        roomDB.insertUser(userModel.toRoomEntity())
    }

    override suspend fun uppadteUserInRoom(userModel: UserModel){
        roomDB.updateUserWorkouts(userModel.nickname, userModel)
    }
}
