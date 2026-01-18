package com.adammockor.sharephototoobsidian.storage

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.exifinterface.media.ExifInterface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileNameBuilder {

    private val outputFormat =
        SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)

    fun build(context: Context, uri: Uri): String {
        val original = queryDisplayName(context, uri)
            ?.substringBeforeLast(".")
            ?: "photo"

        val extension = queryDisplayName(context, uri)
            ?.substringAfterLast(".", "jpg")
            ?: "jpg"

        val timestamp = extractTimestamp(context, uri)
        val formatted = outputFormat.format(timestamp)

        return "${formatted}_${original}.$extension"
    }

    private fun parseExifDate(value: String): Date? {
        return try {
            // EXIF format: "yyyy:MM:dd HH:mm:ss"
            SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US)
                .parse(value)
        } catch (_: Exception) {
            null
        }
    }

    private fun extractTimestamp(context: Context, uri: Uri): Date {
        return try {
            context.contentResolver.openInputStream(uri).use { input ->
                if (input == null) return@use null

                val exif = ExifInterface(input)

                val raw =
                    exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                        ?: exif.getAttribute(ExifInterface.TAG_DATETIME)

                raw?.let { parseExifDate(it) }
            }
        } catch (_: Exception) {
            null
        } ?: Date()
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0) cursor.getString(idx) else null
            } else null
        } finally {
            cursor?.close()
        }
    }
}