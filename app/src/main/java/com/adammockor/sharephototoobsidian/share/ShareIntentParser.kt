package com.adammockor.sharephototoobsidian.share

import android.content.Intent
import android.net.Uri

object ShareIntentParser {

    fun extractUris(intent: Intent): List<Uri> {
        val result = mutableListOf<Uri>()

        when (intent.action) {
            Intent.ACTION_SEND -> {
                intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    ?.let { result += it }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                    ?.let { result += it }
            }
        }

        intent.clipData?.let { clip ->
            for (i in 0 until clip.itemCount) {
                clip.getItemAt(i).uri?.let { result += it }
            }
        }

        return result.distinct()
    }
}