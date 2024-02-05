/*
 *abiola 2024
 */

package com.mshdabiola.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewer(
    path: String? = null,
    onDismiss: () -> Unit,
) {
    if (path != null) {
        AlertDialog(onDismissRequest = onDismiss) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZoomableAsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    model = path,
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                )

                FilledTonalIconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = onDismiss,
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
            }
        }
    }
}
