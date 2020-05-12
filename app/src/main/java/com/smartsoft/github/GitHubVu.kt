package com.smartsoft.github

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.ModifierInfo
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp

@Composable
fun GitHubPreview() {
    GitHubVu(GitHub.dummyUsers, "GitHub Jetpack") {}
}

@Composable
fun GitHubVu(users: List<User>, title: String, dispatch: GhDispatch) {
    MaterialTheme {
        val colors = MaterialTheme.colors
        Surface(color = colors.background) {
            Column() {
                TopAppBar(
                    title = { Text(title) }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        ButtonsVu(dispatch)
                    }
//                    Column(modifier = Flexible(1f).wraps(Spacing(8.dp))) {
//                        UsersVu(users)
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
fun UsersVu(users: List<User>) {
//    Surface(color = Color.Cyan) {
    VerticalScroller(modifier = Modifier.fillMaxSize()) {
        Column(modifier =  Modifier.fillMaxWidth()) {
            users.forEach {
                Text(it.login)
            }
        }
    }
//    }
}

@Composable
fun ButtonsVu(dispatch: GhDispatch) {
    val colors = MaterialTheme.colors
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
        text = { Text(text) },
        modifier = Modifier.padding(8.dp).width(100.dp),
        onClick = { dispatch(action) }
    )
}

@Preview
@Composable
fun DefaultPreview() {
    GitHubPreview()
}