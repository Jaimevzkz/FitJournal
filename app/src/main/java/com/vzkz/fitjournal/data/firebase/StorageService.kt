package com.vzkz.fitjournal.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.vzkz.fitjournal.data.firebase.STORAGECONSTANTS.PROFILEPHOTO
import com.vzkz.fitjournal.data.firebase.STORAGECONSTANTS.PROGRESSPHOTOS
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private object STORAGECONSTANTS{
    const val PROGRESSPHOTOS = "progressPhotos"
    const val PROFILEPHOTO = "profilePhoto"
}

class StorageService @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun uploadAndDownloadProgressPhoto(uri: Uri, uid: String, profileSrc: Boolean, oldProfileUri: Uri?): Uri {
        if(profileSrc && oldProfileUri != null)
            deletePhoto(oldProfileUri)
        return suspendCancellableCoroutine { cancellableContinuation ->
            val photoSource = if(profileSrc) PROFILEPHOTO else PROGRESSPHOTOS
            val reference = storage.reference.child("$uid/$photoSource/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
        }
    }

    fun deletePhoto(uri: Uri){
        val reference = storage.reference.child("${uri.lastPathSegment}")
        reference.delete().addOnSuccessListener {
            Log.i("Jaime","Photo deleted correctly")
        }.addOnFailureListener {
            Log.e("Jaime","Error deleting photo")
        }
    }


    private fun downloadImage(
        uploadTask: UploadTask.TaskSnapshot, cancellableContinuation: CancellableContinuation<Uri>
    ) {
        uploadTask.storage.downloadUrl
            .addOnSuccessListener { uri -> cancellableContinuation.resume(uri) }
            .addOnFailureListener { cancellableContinuation.resumeWithException(it) }
    }

    suspend fun getAllProgressPhotos(uid: String):List<Uri>{
        val reference = storage.reference.child("$uid/$PROGRESSPHOTOS")

        return reference.listAll().await().items.map { it.downloadUrl.await() }
    }

    suspend fun getProfilePhoto(uid: String): Uri?{
        val reference = storage.reference.child("$uid/$PROFILEPHOTO")
        return reference.listAll().await().items.firstOrNull()?.downloadUrl?.await()
    }

}