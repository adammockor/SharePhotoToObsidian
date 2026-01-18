package com.adammockor.sharephototoobsidian.storage

import android.content.Context
import android.net.Uri

object DestinationFolderStore {
    private const val PREFS = "share_photo_to_obsidian_prefs"
    private const val KEY_TREE_URI = "dest_tree_uri"

    fun get(context: Context): Uri? {
        val s = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_TREE_URI, null)
        return s?.let(Uri::parse)
    }

    fun set(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TREE_URI, uri.toString())
            .apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_TREE_URI)
            .apply()
    }
}