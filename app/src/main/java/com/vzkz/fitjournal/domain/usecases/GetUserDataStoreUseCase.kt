package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataStoreUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke(): Flow<UserModel> {
        return dataStoreRepository.getUser()
    }
}