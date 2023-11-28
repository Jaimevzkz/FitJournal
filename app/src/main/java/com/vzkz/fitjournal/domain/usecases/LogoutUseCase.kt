package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.DataStoreRepository
import com.vzkz.fitjournal.domain.Repository
import javax.inject.Inject

class LogoutUseCase@Inject constructor(private val repository: Repository, private val dsRepository: DataStoreRepository) {
    suspend operator fun invoke() {
        repository.logout()
        dsRepository.cleanUserNickname()
    }
}