/*
 *abiola 2024
 */

package com.mshdabiola.model

data class NoteItem(val content: String = "", val type: Type = Type.TEXT, val isCheck: Boolean = false)

enum class Type {
    TEXT,
    IMAGE,
    CHECK,
}
