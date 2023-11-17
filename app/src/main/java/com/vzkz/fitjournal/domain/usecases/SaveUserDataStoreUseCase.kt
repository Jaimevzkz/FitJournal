package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class SaveUserDataStoreUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(user: UserModel) {
        dataStoreRepository.saveUser(user)
    }
}