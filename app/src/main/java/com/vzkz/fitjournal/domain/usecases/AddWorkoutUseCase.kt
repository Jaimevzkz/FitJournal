package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.WorkoutModel
import javax.inject.Inject

class AddWorkoutUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val repository: Repository) {
    suspend operator fun invoke(workout: WorkoutModel): Boolean {
        return try{
            //Get user nickname from DS
            val nickname = dataStoreRepository.getUserNickname()
            //get user form room
            val user = repository.getUserFromRoom(nickname)
            var workoutCopy: WorkoutModel
            val wotOrder = user.workouts?.size?.plus(1) ?: 1

            //firestore
            workoutCopy = workout.copy(wotOrder = wotOrder)
            workoutCopy = repository.addWorkout(user, workoutCopy) //returns the wid assigned
            //room
            if(user.workouts == null)
                user.workouts = listOf(workoutCopy)
            else{
                val userWots = mutableListOf<WorkoutModel>()
                userWots.addAll(user.workouts!!)
                userWots.add(workoutCopy)
                user.workouts = userWots.toList()
            }

            repository.insertUserInRoom(user)
            true
        } catch (e: Exception){
            false
        }
    }
}