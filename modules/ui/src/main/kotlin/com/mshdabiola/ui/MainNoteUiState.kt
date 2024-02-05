/*
 *abiola 2024
 */

package com.mshdabiola.ui

data class MainNoteUiState(
    val id: Long,
    val title: String,
    val content: String? = null,
    val checkFraction: String? = null,
    val path: String? = null,
    val createAt: String = "Today",
)
