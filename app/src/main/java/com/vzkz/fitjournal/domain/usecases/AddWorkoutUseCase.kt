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
            var workoutCopy = workout
            if(user.workouts == null){ //first workout added
                //room
                user.workouts = listOf(workout.copy(wotOrder = 1))
                //firestore
                workoutCopy =workout.copy(wotOrder = 1)
            } else {
                //room
                val userWots = mutableListOf<WorkoutModel>()
                userWots.addAll(user.workouts!!)
                userWots.add(workout.copy(wotOrder = userWots.size + 1))
                user.workouts = userWots.toList()
                //firestore
                workoutCopy = workout.copy(wotOrder = user.workouts!!.size + 1)
            }

            repository.insertUserInRoom(user)
            repository.addWorkout(user, workoutCopy)
            true
        } catch (e: Exception){
            false
        }
    }
}