package com.vzkz.fitjournal.domain.usecases.datapersistence

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class GetUserPersistenceUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val repository: Repository) {
    suspend operator fun invoke(): UserModel {
        //Get user nickname from DS
        val nickname = dataStoreRepository.getUserNickname()
        //get user form room
        return repository.getUserFromRoom(nickname)
    }
}