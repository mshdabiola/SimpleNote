/*
 *abiola 2024
 */

@file:OptIn(ExperimentalMaterial3Api::class)

package com.mshdabiola.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.designsystem.theme.LocalBackgroundTheme

@Composable
fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    mainText: String = "Notepad Pro",
    isDarkMode: Boolean = true,
    toggleDarkMode: () -> Unit = {},
    onSearch: () -> Unit = {},
) {
    val bg= LocalBackgroundTheme.current

    MediumTopAppBar(
        colors = TopAppBarDefaults
            .mediumTopAppBarColors(
                containerColor = bg.color,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = mainText,
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.testTag("main:topbar:search"),
                onClick = onSearch,
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                )
            }
            IconButton(
                modifier = Modifier.testTag("main:topbar:darkmode"),
                onClick = toggleDarkMode,
            ) {
                if (isDarkMode) {
                    Icon(
                        imageVector = Icons.Default.LightMode,
                        contentDescription = "light mode",
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = "dark mode",
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onChangeCheckClick: () -> Unit = {},
    onAddImageClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier.testTag("detail:topbar"),
        colors = TopAppBarDefaults
            .topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                modifier = modifier.testTag("detail:topbar:back"),

                onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                )
            }
        },
        title = {
        },
        actions = {
            IconButton(
                modifier = modifier.testTag("detail:topbar:addimage"),

                onClick = onAddImageClick,
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = "add image",
                )
            }
            IconButton(
                modifier = modifier.testTag("detail:topbar:changecheck"),

                onClick = onChangeCheckClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = "add checkList",
                )
            }
            IconButton(
                modifier = modifier.testTag("detail:topbar:delete"),

                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun MainTopAppBarPreview() {
    MainTopAppBar(scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior())
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun DetailTopAppBarPreview() {
    DetailTopAppBar(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    )
}
