package com.nuvio.app.features.streams

import androidx.compose.runtime.Composable

@Composable
expect fun rememberLocalFilePicker(
    onFilePicked: (String?) -> Unit
): () -> Unit
