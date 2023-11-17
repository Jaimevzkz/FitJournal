package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import javax.inject.Inject

class LogoutUseCase@Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        return repository.logout()
    }
}