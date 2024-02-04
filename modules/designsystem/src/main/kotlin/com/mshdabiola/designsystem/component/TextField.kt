/*
 *abiola 2024
 */

package com.mshdabiola.designsystem.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoteTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    placeholder: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: () -> Unit = {},
    leadingIcon:
    @Composable()
    (() -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    TextField(
        modifier = modifier,
//                    .bringIntoViewRequester(focusRequester2)
//            .focusRequester(focusRequester)

        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder != null) {
                Text(text = placeholder)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = textStyle,

        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions { keyboardAction() },
        leadingIcon = leadingIcon,
        maxLines = 1,
        singleLine = true
    )
}

@Preview
@Composable
private fun SkTextFieldPreview() {
    NoteTextField(value = "Sk Testing", textStyle = MaterialTheme.typography.titleLarge)
}
