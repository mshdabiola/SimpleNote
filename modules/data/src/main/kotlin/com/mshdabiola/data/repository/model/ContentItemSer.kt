/*
 *abiola 2024
 */

package com.mshdabiola.data.repository.model

import com.mshdabiola.model.NoteItem
import com.mshdabiola.model.Type
import kotlinx.serialization.Serializable

@Serializable
internal data class ContentItemSer(val content: String, val type: Type = Type.TEXT, val isCheck: Boolean)

internal fun ContentItemSer.asContents() = NoteItem(content, type, isCheck)

internal fun NoteItem.asContentSer() = ContentItemSer(content, type, isCheck)
