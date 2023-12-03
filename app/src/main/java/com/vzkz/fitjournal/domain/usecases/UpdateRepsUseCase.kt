package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class UpdateRepsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(
        userModel: UserModel,
        uid: String,
        wid: String,
        exid: String,
        workoutIndex: Int,
        exIndex: Int
    ) {
        repository.uppadteUserInRoom(userModel)
        repository.updateSets(
            repList = userModel.workouts?.get(workoutIndex)?.exercises?.get(exIndex)?.setXrepXweight
                ?: emptyList(),
            uid = uid,
            wid = wid,
            exid = exid
        )
    }

}