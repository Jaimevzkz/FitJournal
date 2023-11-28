package com.vzkz.fitjournal.domain.usecases.datapersistence

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class SaveUserPersistenceUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository, private val repository: Repository) {
    suspend operator fun invoke(user: UserModel) {
        //Save nickname in DS
        dataStoreRepository.saveUserNickname(user.nickname)
        //Save user in room
        repository.insertUserInRoom(user)
    }
}