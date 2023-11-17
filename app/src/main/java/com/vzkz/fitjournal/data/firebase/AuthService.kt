package com.vzkz.fitjournal.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthService @Inject constructor(private val firebaseAuth: FirebaseAuth) { //TO Test

    suspend fun login(email: String, password: String): FirebaseUser? {

        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
                    Log.e("Jaime", "${it.message}")
                    cancellableContinuation.resumeWithException(it)
                }

        }
    }


    suspend fun signUp(email: String, password: String): FirebaseUser? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user)
                }
                .addOnFailureListener {
                    Log.e("Jaime", "${it.message}")
                    cancellableContinuation.resumeWithException(it)
                }
        }
    }

    fun isUserLogged(): Boolean = getCurrentUser() != null

    fun logout() = firebaseAuth.signOut()

    private fun getCurrentUser() = firebaseAuth.currentUser

}