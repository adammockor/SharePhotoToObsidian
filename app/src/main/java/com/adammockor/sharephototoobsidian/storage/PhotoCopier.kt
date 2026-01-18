package com.adammockor.sharephototoobsidian.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.adammockor.sharephototoobsidian.storage.FileNameBuilder
import java.io.IOException

object PhotoCopier {

    fun copy(
        context: Context,
        sourceUri: Uri,
        destinationTreeUri: Uri
    ): Result<Uri> {

        val destDir = DocumentFile.fromTreeUri(context, destinationTreeUri)
            ?: return Result.failure(IllegalStateException("Invalid destination folder"))

        val filename = FileNameBuilder.build(context, sourceUri)

        val destFile = destDir.createFile("image/jpeg", filename)
            ?: return Result.failure(IOException("Failed to create destination file"))

        context.contentResolver.openInputStream(sourceUri).use { input ->
            context.contentResolver.openOutputStream(destFile.uri).use { output ->
                if (input == null || output == null) {
                    return Result.failure(IOException("Stream open failed"))
                }
                input.copyTo(output)
            }
        }

        return Result.success(destFile.uri)
    }
}