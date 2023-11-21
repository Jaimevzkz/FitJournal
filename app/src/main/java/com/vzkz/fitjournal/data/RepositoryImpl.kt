package com.vzkz.fitjournal.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.vzkz.fitjournal.R
import com.vzkz.fitjournal.data.firebase.AuthService
import com.vzkz.fitjournal.data.firebase.FirestoreService
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
    private val context: Context
) : Repository {

    override suspend fun login(email: String, password: String): UserModel? {
        val user: FirebaseUser?
        try{
            user = authService.login(email, password)
        } catch (e: Exception){
            throw Exception(context.getString(R.string.wrong_email_or_password))
        }
        if(user != null){
            val userData: UserModel
            try{
                userData = firestoreService.getUserData(user.uid)
            } catch (e: Exception){
                if(e.message == "NF") throw Exception(context.getString(R.string.network_failure_while_checking_user_existence))
                else throw Exception(context.getString(R.string.couldn_t_find_the_user))
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
    ): UserModel? {
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
            goal = userData.goal
        )
    }

}