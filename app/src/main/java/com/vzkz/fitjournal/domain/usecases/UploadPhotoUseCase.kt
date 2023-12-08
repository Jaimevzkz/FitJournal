package com.vzkz.fitjournal.domain.usecases

import android.net.Uri
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(uri: Uri, user: UserModel): Uri {
        val storageUri =  repository.uploadPhoto(uri, user.uid)

        val updatedPhotosList = user.progressPhotos.toMutableList()
        updatedPhotosList.add(storageUri)
        repository.insertUserInRoom(user.copy(progressPhotos = updatedPhotosList))

        return storageUri
    }

}