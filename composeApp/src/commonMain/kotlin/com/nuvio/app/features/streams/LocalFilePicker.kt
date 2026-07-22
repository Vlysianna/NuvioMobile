package com.nuvio.app.features.streams

import androidx.compose.runtime.Composable

@Composable
expect fun rememberLocalFilePicker(
    mimeTypes: List<String> = listOf("video/mp4", "video/x-matroska"),
    onFilePicked: (String?) -> Unit
): () -> Unit
