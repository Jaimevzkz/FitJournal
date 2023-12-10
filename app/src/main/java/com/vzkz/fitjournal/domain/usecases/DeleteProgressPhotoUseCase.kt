package com.vzkz.fitjournal.domain.usecases

import android.net.Uri
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class DeleteProgressPhotoUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(uri: Uri, updatedUser: UserModel) {
        repository.insertUserInRoom(updatedUser)
        repository.deleteProgressPhoto(uri = uri)
    }
}