package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class ModifyUserDataUseCase @Inject constructor(private val repository: Repository){
    suspend operator fun invoke(oldUser: UserModel, newUser: UserModel) {
        return repository.modifyUserData(oldUser = oldUser, newUser = newUser)
    }
}