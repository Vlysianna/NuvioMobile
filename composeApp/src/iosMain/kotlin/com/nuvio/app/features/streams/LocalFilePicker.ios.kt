package com.nuvio.app.features.streams

import androidx.compose.runtime.Composable

@Composable
actual fun rememberLocalFilePicker(
    onFilePicked: (String?) -> Unit
): () -> Unit {
    return {}
}
