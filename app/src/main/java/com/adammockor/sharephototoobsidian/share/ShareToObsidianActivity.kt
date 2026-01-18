package com.adammockor.sharephototoobsidian.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.adammockor.sharephototoobsidian.storage.DestinationFolderStore
import com.adammockor.sharephototoobsidian.MainActivity
import com.adammockor.sharephototoobsidian.storage.PhotoCopier
import com.adammockor.sharephototoobsidian.notify.ShareNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.forEach

class ShareToObsidianActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val folderUri = DestinationFolderStore.get(this)
        if (folderUri == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val uris = ShareIntentParser.extractUris(intent)

        lifecycleScope.launch {
            val (ok, fail) = withContext(Dispatchers.IO) {
                var successCount = 0
                var failCount = 0

                uris.forEach { uri ->
                    val result = PhotoCopier.copy(this@ShareToObsidianActivity, uri, folderUri)
                    if (result.isSuccess) successCount++ else failCount++
                }

                successCount to failCount
            }

            ShareNotification.show(this@ShareToObsidianActivity, ok, fail)
            finish()
        }
    }
}