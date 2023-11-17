package com.vzkz.fitjournal.domain.usecases

import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(email: String, password: String, nickname: String): UserModel? {
        return repository.signUp(email, password, nickname)
    }
}