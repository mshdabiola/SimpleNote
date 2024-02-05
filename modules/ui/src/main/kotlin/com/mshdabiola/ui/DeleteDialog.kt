/*
 *abiola 2024
 */

package com.mshdabiola.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDelete) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Delete Note")
        },
        icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "delete") },
        text = {
            Text(text = "Are you sure you want to delete this note?")
        },
    )
}
