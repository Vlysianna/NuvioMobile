package com.nuvio.app.features.streams

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocalFilePicker(
    mimeTypes: List<String>,
    onFilePicked: (String?) -> Unit
): () -> Unit {
    return {}
}
