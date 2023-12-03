package com.vzkz.fitjournal.domain.usecases

import android.util.Log
import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import com.vzkz.fitjournal.domain.model.WorkoutModel
import javax.inject.Inject

class DeleteWorkoutUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val repository: Repository) {
    suspend operator fun invoke(wid: String): UserModel? {
        return try{
            //Get uid from DS
            val uid = dataStoreRepository.getUserUid()
            val nickname = dataStoreRepository.getUserNickname()


            val user = repository.getUserFromRoom(nickname)
            val updatedWots = user.workouts?.filter { it.wid != wid }
            val updatedUser = user.copy(workouts = updatedWots)
            repository.uppadteUserInRoom(updatedUser)

            repository.deleteWorkout(uid = uid, wid = wid)
            updatedUser
        } catch (e: Exception){
            Log.e("Jaime", "Error in DeleteWorkoutUseCase")
            null
        }
    }
}