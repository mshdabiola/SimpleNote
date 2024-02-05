/*
 *abiola 2024
 */

package com.mshdabiola.model

data class Note(
    val id: Long? = null,
    val title: String = "",
    val contents: List<NoteItem>,
    val createdAt: Long,
    val createdAtStrng: String,

)
