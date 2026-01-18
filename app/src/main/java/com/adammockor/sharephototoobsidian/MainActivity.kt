package com.adammockor.sharephototoobsidian

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adammockor.sharephototoobsidian.storage.DestinationFolderStore

class MainActivity : ComponentActivity() {

    private val pickFolderLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { treeUri: Uri? ->
            if (treeUri == null) return@registerForActivityResult

            // Persist permission so it keeps working after restart
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(treeUri, flags)

            DestinationFolderStore.set(this, treeUri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var current by remember { mutableStateOf(DestinationFolderStore.get(this)) }

            // Refresh state after picker returns
            LaunchedEffect(Unit) {
                current = DestinationFolderStore.get(this@MainActivity)
            }

            Column(Modifier.padding(16.dp)) {
                Text("Destination folder:")
                Text(current?.toString() ?: "Not set")

                Button(
                    onClick = {
                        pickFolderLauncher.launch(null) // user picks your DailyPhoto folder
                    },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Pick destination folder")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // keep UI updated if coming back from picker
        // (simple approach without extra state management)
    }
}