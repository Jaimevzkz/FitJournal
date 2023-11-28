package com.vzkz.fitjournal.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.vzkz.fitjournal.domain.model.Constants.DIFFICULTY
import com.vzkz.fitjournal.domain.model.Constants.DURATION
import com.vzkz.fitjournal.domain.model.Constants.EXCOUNT
import com.vzkz.fitjournal.domain.model.Constants.EXERCISES
import com.vzkz.fitjournal.domain.model.Constants.EXNAME
import com.vzkz.fitjournal.domain.model.Constants.EXORDER
import com.vzkz.fitjournal.domain.model.Constants.INSTRUCTIONS
import com.vzkz.fitjournal.domain.model.Constants.MUSCLE
import com.vzkz.fitjournal.domain.model.Constants.NICKNAME
import com.vzkz.fitjournal.domain.model.Constants.REPS
import com.vzkz.fitjournal.domain.model.Constants.REST
import com.vzkz.fitjournal.domain.model.Constants.SETNUM
import com.vzkz.fitjournal.domain.model.Constants.SETXREPXWEIGHT
import com.vzkz.fitjournal.domain.model.Constants.USERS_COLLECTION
import com.vzkz.fitjournal.domain.model.Constants.WEIGHT
import com.vzkz.fitjournal.domain.model.Constants.WORKOUTS
import com.vzkz.fitjournal.domain.model.Constants.WOTNAME
import com.vzkz.fitjournal.domain.model.Constants.WOTORDER
import com.vzkz.fitjournal.domain.model.ExerciseModel
import com.vzkz.fitjournal.domain.model.Exercises
import com.vzkz.fitjournal.domain.model.SetXrepXweight
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirestoreService @Inject constructor(firestore: FirebaseFirestore) {

    private val usersCollection = firestore.collection(USERS_COLLECTION)
    suspend fun userExists(nickname: String): Boolean {
        val userInfo = usersCollection.whereEqualTo(NICKNAME, nickname).get().await()
        return !userInfo.isEmpty
    }

    fun insertUser(userData: UserModel?) {
        if (userData == null) throw Exception()
        val userDocument = usersCollection.document(userData.uid)
        val user = userData.toMap()

        if (userData.workouts != null) {
            val workoutDocumentRef = userDocument.collection(WORKOUTS)
            for (workout in userData.workouts!!) {
                val docWorkoutDocumentRef = workoutDocumentRef.document()
                docWorkoutDocumentRef.set(workout.toMap())

                val exerciseListRef = docWorkoutDocumentRef.collection(EXERCISES)
                for (exercise in workout.exercises) {
                    val docExListRef = exerciseListRef.document()
                    docExListRef.set(exercise.exData.toMap(), SetOptions.merge())
                    docExListRef.set(exercise.toMap(), SetOptions.merge())

                    val indExerciseRef = docExListRef.collection(
                        SETXREPXWEIGHT
                    )
                    for (indExercise in exercise.setXrepXweight) {
                        indExerciseRef.document(indExercise.exNum).set(indExercise.toMap())
                    }
                }

            }

        }

        // We use SetOptions.merge() to add the user without overriding other potential fields
        userDocument.set(user, SetOptions.merge())
            .addOnSuccessListener {
                Log.i("Jaime", "Success inserting in database")
            }
            .addOnFailureListener {
                Log.e("Jaime", "Failure ${it.message}")
            }

    }

    suspend fun getUserData(uid: String): UserModel {
        return try {
            val userDocumentRef = usersCollection.document(uid)

            val userModel: UserModel =
                userDocumentRef.get().await().toObject(UserModel::class.java)
                    ?: throw Exception("CF")

            val workoutsCollectionRef = userDocumentRef.collection(WORKOUTS)
            val workoutDocuments = workoutsCollectionRef.get().await()

            val workoutsList = workoutDocuments.map { workoutDocument ->
                val exercisesCollectionRef =
                    workoutDocument.reference.collection(EXERCISES)

                val exerciseDocuments = exercisesCollectionRef.get().await()

                val exerciseList = exerciseDocuments.map { exerciseDocument ->
                    val repCollectionRef =
                        exerciseDocument.reference.collection(SETXREPXWEIGHT)

                    val repDocuments = repCollectionRef.get().await()

                    val repList = repDocuments.map { repDocument ->
                        SetXrepXweight(
                            exNum = repDocument.id,
                            reps = repDocument.getLong(REPS)?.toInt() ?: -1,
                            weight = repDocument.getLong(WEIGHT)?.toInt() ?: -1
                        )
                    }

                    val exData = ExerciseModel(
                        exName = exerciseDocument.getString(EXNAME) ?: "error",
                        muscle = exerciseDocument.getString(MUSCLE) ?: "error",
                        difficulty = exerciseDocument.getString(DIFFICULTY) ?: "error",
                        instructions = exerciseDocument.getString(INSTRUCTIONS) ?: "error",
                    )

                    Exercises(
                        rest = exerciseDocument.getLong(REST)?.toInt() ?: -1,
                        exData = exData,
                        setNum = exerciseDocument.getLong(SETNUM)?.toInt() ?: -1,
                        setXrepXweight = repList,
                        exOrder = exerciseDocument.getLong(EXORDER)?.toInt() ?: -1,
                    )
                }

                WorkoutModel(
                    wotName = workoutDocument.getString(WOTNAME) ?: "error",
                    duration = workoutDocument.getLong(DURATION)?.toInt() ?: -1,
                    exCount = workoutDocument.getLong(EXCOUNT)?.toInt() ?: -1,
                    wotOrder = workoutDocument.getLong(WOTORDER)?.toInt() ?: -1,
                    exercises = exerciseList.sortedBy { it.exOrder }
                )
            }

            userModel.workouts = workoutsList.sortedBy { it.wotOrder }
            userModel
        } catch (e: Exception) {
            Log.e("Jaime", "error getting doc. ${e.message}")
            when (e.message) {
                "CF" -> throw Exception("CF")
                "NU" -> throw Exception("NU")
                else -> throw Exception("NF")
            }
        }
    }

    suspend fun modifyUserData(oldUser: UserModel, newUser: UserModel) {
        if ((oldUser.nickname != newUser.nickname) && (userExists(newUser.nickname))) throw Exception()

        // Gets the reference to the user document
        val userDocumentReference = usersCollection.document(oldUser.uid)

        // Casts the new UserModel to map in order to update only the needed fields
        val newUserMap = newUser.toMap()

        // Updates firestore data
        userDocumentReference.update(newUserMap).addOnSuccessListener {
            Log.i("Jaime", "Data updated successfully")
        }.addOnFailureListener {
            Log.i("Jaime", "Error updating data")
            throw Exception("NF")
        }

    }

}
