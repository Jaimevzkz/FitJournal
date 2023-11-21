package com.vzkz.fitjournal.data.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.vzkz.fitjournal.data.firebase.Constants.USERS_COLLECTION
import com.vzkz.fitjournal.data.firebase.Constants.UserData.AGE
import com.vzkz.fitjournal.data.firebase.Constants.UserData.EMAIL
import com.vzkz.fitjournal.data.firebase.Constants.UserData.FIRSTNAME
import com.vzkz.fitjournal.data.firebase.Constants.UserData.GENDER
import com.vzkz.fitjournal.data.firebase.Constants.UserData.GOAL
import com.vzkz.fitjournal.data.firebase.Constants.UserData.LASTNAME
import com.vzkz.fitjournal.data.firebase.Constants.UserData.NICKNAME
import com.vzkz.fitjournal.data.firebase.Constants.UserData.WEIGHT
import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private object Constants {
    const val USERS_COLLECTION = "users"

    object UserData {
        const val NICKNAME = "nickname"
        const val EMAIL = "email"
        const val FIRSTNAME = "firstname"
        const val LASTNAME = "lastname"
        const val WEIGHT = "weight"
        const val AGE = "age"
        const val GENDER = "gender"
        const val GOAL = "goal"
    }
}


class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun userExists(nickname: String): Boolean {
        val userInfo = firestore.collection(USERS_COLLECTION).whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: UserModel?) {
        if (userData == null) throw Exception()
        var user = hashMapOf(
            NICKNAME to userData.nickname,
            FIRSTNAME to userData.firstname,
            LASTNAME to userData.lastname
        )
        if (userData.email != null) user[EMAIL] = userData.email
        if (userData.weight != null) user[WEIGHT] = userData.weight
        if (userData.age != null) user[AGE] = userData.age
        if (userData.gender != null) user[GENDER] = userData.gender
        if (userData.goal != null) user[GOAL] = userData.goal

        //At this point, we know for a fact that nickname is unique
        firestore.collection(USERS_COLLECTION).document(userData.uid).set(user)
            .addOnSuccessListener {
                Log.i("Jaime", "Success inserting in database")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Failure ${it.message}")
            }

    }

    suspend fun getUserData(uid: String): UserModel { //returns nickname //TO test
        val documentSnapshot: DocumentSnapshot
        try {
            documentSnapshot = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()
            if (documentSnapshot.exists()) {
                val nickname : String = documentSnapshot.data?.get(NICKNAME).toString()
                val firstname : String = documentSnapshot.data?.get(FIRSTNAME).toString()
                val lastname : String = documentSnapshot.data?.get(LASTNAME).toString()
                var weight : String? = documentSnapshot.data?.get(WEIGHT).toString()
                if(weight == "null") weight = null
                var age : String? = documentSnapshot.data?.get(AGE).toString()
                if(age == "null") age = null
                var gender : String? = documentSnapshot.data?.get(GENDER).toString()
                if(gender == "null") gender = null
                var goal : String?= documentSnapshot.data?.get(GOAL).toString()
                if(goal == "null") goal = null
                return UserModel(
                    uid = uid,
                    nickname = nickname,
                    email = null,
                    firstname = firstname,
                    lastname = lastname,
                    weight = weight,
                    age = age,
                    gender = gender,
                    goal = goal
                )
            } else throw Exception("NU")
        } catch (e: Exception) {
            Log.e("Jaime", "error getting doc. ${e.message}")
            throw Exception("NF")
        }
    }

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel) {
        val newUserObject = hashMapOf(
            NICKNAME to newUser.nickname,
            FIRSTNAME to newUser.firstname,
            LASTNAME to newUser.lastname
        )
        if (newUser.email != null) newUserObject[EMAIL] = newUser.email
        if (newUser.weight != null) newUserObject[WEIGHT] = newUser.weight
        if (newUser.age != null) newUserObject[AGE] = newUser.age
        if (newUser.gender != null) newUserObject[GENDER] = newUser.gender
        if (newUser.goal != null) newUserObject[GOAL] = newUser.goal

        if ((oldUser.nickname != newUser.nickname) && (userExists(newUser.nickname))) throw Exception()

        firestore.collection(USERS_COLLECTION).document(oldUser.uid)
            .update(newUserObject as Map<String, String>).addOnSuccessListener {
            Log.i("Jaime", "Data updated succesfully")
        }.addOnFailureListener {
            Log.i("Jaime", "Error updating data")
            throw Exception("NF")
        }

    }

}
