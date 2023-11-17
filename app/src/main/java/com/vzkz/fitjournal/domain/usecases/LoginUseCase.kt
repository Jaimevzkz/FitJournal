package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String): UserModel? {
        return repository.login(email, password)
    }
}