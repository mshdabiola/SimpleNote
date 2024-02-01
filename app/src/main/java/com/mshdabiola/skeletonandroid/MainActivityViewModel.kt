/*
 *abiola 2022
 */

package com.mshdabiola.skeletonandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.model.UserData
import com.mshdabiola.skeletonandroid.MainActivityUiState.Loading
import com.mshdabiola.skeletonandroid.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
