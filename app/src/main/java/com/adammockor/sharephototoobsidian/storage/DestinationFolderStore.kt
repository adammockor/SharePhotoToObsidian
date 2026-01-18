package com.adammockor.sharephototoobsidian.storage

import android.content.Context
import android.net.Uri
import androidx.core.content.edit

object DestinationFolderStore {

    private const val PREFS = "share_photo_to_obsidian_prefs"
    private const val KEY_TREE_URI = "dest_tree_uri"

    fun get(context: Context): Uri? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_TREE_URI, null)
            ?.let(Uri::parse)

    fun set(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
            putString(KEY_TREE_URI, uri.toString())
        }
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
            remove(KEY_TREE_URI)
        }
    }
}