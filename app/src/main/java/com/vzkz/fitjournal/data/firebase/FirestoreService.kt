package com.vzkz.fitjournal.data.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.vzkz.fitjournal.data.firebase.Constants.NICKNAME
import com.vzkz.fitjournal.data.firebase.Constants.USERS_COLLECTION
import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private object  Constants{
    const val USERS_COLLECTION = "users"
    const val NICKNAME = "nickname"
}


class FirestoreService @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun userExists(nickname: String): Boolean {
        val userInfo = firestore.collection(USERS_COLLECTION).whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: UserModel?){
        if (userData == null) throw Exception()
        val user = hashMapOf(
            NICKNAME to userData.nickname
        )
        //At this point, we know for a fact that nickname is unique
        firestore.collection(USERS_COLLECTION).document(userData.uid).set(user).addOnSuccessListener {
            Log.i("Jaime", "Success inserting in database")
        }
        .addOnFailureListener {
            Log.e("Jaime", "Failure ${it.message}")
        }

    }

    suspend fun getUserData(uid: String): String { //returns nickname //TO test
        val documentSnapshot: DocumentSnapshot
        try {
            documentSnapshot = firestore.collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()
            if(documentSnapshot.exists()){
                return documentSnapshot.data?.get(NICKNAME).toString()
            }
            else throw Exception("NU")
        } catch (e: Exception) {
            Log.e("Jaime", "error getting doc. ${e.message}")
            throw Exception("NF")
        }
    }

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel){
        val newUserObject = hashMapOf(
            NICKNAME to newUser.nickname
        )
        if(oldUser.nickname != newUser.nickname){
            val dummy = userExists(newUser.nickname)
            if(dummy) throw Exception()

        }

        firestore.collection(USERS_COLLECTION).document(oldUser.uid).update(newUserObject as Map<String, Any>).addOnSuccessListener {
            Log.i("Jaime", "Data updated succesfully")
        }.addOnFailureListener {
            Log.i("Jaime", "Error updating data")
            throw Exception("NF")
        }

    }

}
