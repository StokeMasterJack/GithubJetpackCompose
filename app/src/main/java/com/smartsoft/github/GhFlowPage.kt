@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.smartsoft.github

import androidx.compose.Composable
import androidx.compose.onActive
import androidx.ui.livedata.observeAsState
import ss.ssutil.UiState


@Composable
fun GhFlowPage(model: GhFlow) {

    val state = model.usersLiveData.observeAsState()
    val users: UiState<List<User>> = state.value ?: UiState.Success(emptyList())
    val dispatch: IDispatch<GhAction> = { model.update(it) }

    onActive {
        L.w("GhFlowPage.onActive")
        dispatch(GhAction.Fetch)
        onDispose {
            L.w("GhFlowPage.onDispose")
        }
    }
    GitHubVu(users = users, title = "GitHub with State", dispatch = dispatch)
}

