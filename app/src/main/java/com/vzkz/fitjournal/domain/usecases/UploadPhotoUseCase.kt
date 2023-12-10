package com.vzkz.fitjournal.domain.usecases

import android.net.Uri
import com.vzkz.fitjournal.domain.Repository
import com.vzkz.fitjournal.domain.model.UserModel
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(uri: Uri, user: UserModel, profilePhoto: Boolean): Uri {
        val oldUri = user.profilePhoto
        val storageUri =  repository.uploadAndDownloadProgressPhoto(uri = uri, uid = user.uid, profileSrc = profilePhoto, oldProfileUri = oldUri)

        if(profilePhoto){
            repository.insertUserInRoom(user.copy(profilePhoto = storageUri))
        } else{
            val updatedPhotosList = user.progressPhotos.toMutableList()
            updatedPhotosList.add(storageUri)
            repository.insertUserInRoom(user.copy(progressPhotos = updatedPhotosList))
        }

        return storageUri

    }

}