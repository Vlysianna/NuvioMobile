package com.nuvio.app.features.streams

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocalFilePicker(
    onFilePicked: (String?) -> Unit
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        onFilePicked(uri?.toString())
    }
    return {
        launcher.launch(arrayOf("video/mp4", "video/x-matroska"))
    }
}
