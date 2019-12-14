package com.smartsoft.github

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview

@Composable
fun GitHubPreview() {
    GitHubVu(GitHub.dummyUsers, "GitHub Jetpack") {}
}

@Composable
fun GitHubVu(users: List<User>, title: String, dispatch: GhDispatch) {
    MaterialTheme {
        val colors = +MaterialTheme.colors()
        Surface(color = colors.background) {
            Column() {
                TopAppBar(
                    title = { Text(title) }
                )
                Row(modifier = ExpandedWidth) {
                    Column {
                        ButtonsVu(dispatch)
                    }
                    Column(modifier = Flexible(1f).wraps(Spacing(8.dp))) {
                        UsersVu(users)
                    }
                }
            }
        }
    }
}


@Composable
fun UsersVu(users: List<User>) {
//    Surface(color = Color.Cyan) {
    VerticalScroller(modifier = Expanded) {
        Column(modifier = ExpandedWidth) {
            users.forEach {
                Text(it.login)
            }
        }
    }
//    }
}

@Composable
fun ButtonsVu(dispatch: GhDispatch) {
    val colors = +MaterialTheme.colors()
    Surface(color = colors.background) {
        Column() {
            Btn(text = "Fetch", action = GhAction.FetchUsersAsync, dispatch = dispatch)
            Btn(text = "Dummy", action = GhAction.FetchDummyUsers, dispatch = dispatch)
            Btn(text = "Clear", action = GhAction.ClearUsers, dispatch = dispatch)
        }
    }
}

@Composable
fun Btn(text: String, action: GhAction, dispatch: GhDispatch) {
    Button(
        text = text,
        modifier = Spacing(8.dp).wraps(Width(100.dp)),
        onClick = { dispatch(action) }
    )
}

@Preview
@Composable
fun DefaultPreview() {
    GitHubPreview()
}