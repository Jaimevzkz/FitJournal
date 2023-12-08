package com.vzkz.fitjournal.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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

    suspend fun uploadAndDownloadImage(uri: Uri, uid: String): Uri {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val reference = storage.reference.child("$uid/$PROGRESSPHOTOS/${uri.lastPathSegment}")
            reference.putFile(uri).addOnSuccessListener {
                downloadImage(it, cancellableContinuation)
            }.addOnFailureListener {
                cancellableContinuation.resumeWithException(it)
            }
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

}