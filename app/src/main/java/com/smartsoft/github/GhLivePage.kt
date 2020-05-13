package com.smartsoft.github

import androidx.compose.Composable
import androidx.compose.onActive
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.TopAppBar
import androidx.ui.unit.dp
import ss.ssutil.UiState

@Composable
fun GhLivePage(model: GhLive) {

    val state = model.users.observeAsState()
    val users: UiState<List<User>> = state.value ?: UiState.Success(emptyList())
    val dispatch: IDispatch<GhAction> = { model.update(it) }

    onActive {
        L.w("GhLivePage.onActive")
        dispatch(GhAction.Fetch)
        onDispose {
            L.w("GhLivePage.onDispose")
        }
    }

    GitHubVu(users = users, title = "GitHub with ViewModel", dispatch = dispatch)
}

@Composable
fun GitHubVu(
    users: UiState<List<User>> = UiState.Success(emptyList()),
    title: String = "GitHub JetPack",
    dispatch: IDispatch<GhAction> = {}
) {
    MaterialTheme {
        val colors = MaterialTheme.colors
        Surface(color = colors.background) {
            Column() {
                TopAppBar(
                    title = { Text(title) }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
//                    Column {
//                        GhButtonsVu(dispatch)
//                    }
                    Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                        UsersVu(users)
                    }
                }
            }
        }
    }
}


@Composable
fun UsersVu(users: UiState<List<User>>) {
    when (users) {
        is UiState.NotStarted -> UsersScrollVu(emptyList())
        is UiState.Error -> ErrorVu(users.exception)
        is UiState.Loading -> LoadingVu()
        is UiState.Success -> UsersScrollVu(users.data)
    }
}

@Composable
fun UsersScrollVu(users: List<User>) {
    VerticalScroller(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            users.forEach {
                Text(it.login)
            }
        }
    }
}


@Composable
fun GhButtonsVu(dispatch: IDispatch<GhAction>) {
    val colors = MaterialTheme.colors
    Surface(color = colors.background) {
        Column() {
            Btn(text = "Fetch", action = GhAction.Fetch, dispatch = dispatch)
            Btn(text = "Dummy", action = GhAction.Dummy, dispatch = dispatch)
            Btn(text = "Clear", action = GhAction.Clear, dispatch = dispatch)
        }
    }
}


