package com.smartsoft.github

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.tooling.preview.Preview

enum class GhAction {
    FetchUsersAsync,
    FetchDummyUsers,
    ClearUsers,
}

typealias GhDispatch = (ev: GhAction) -> Unit

class MainActivity : AppCompatActivity() {

    private val appCtx: AppCtx = AppCtx()
//    private lateinit var viewModel: GhViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = appCtx.mkGhViewModel()
//        val users: List<User> = viewModel.users.value ?: emptyList()
        setContent {
            GitHubApp()
        }

//        viewModel.observe(theme)


    }


}


@Preview
@Composable
fun DefaultPreview() {
    GitHubPreview()
}

@Composable
fun GitHubApp() {

    val (users, setUsers) = +state { emptyList<User>() }

    val appCtx = AppCtx()
    val gitHub = appCtx.mkGitHub()


    fun fetchUsersRemoteAsync() {
        println("fetchUsersRemoteAsync")
        gitHub.fetchUsersAsync(setUsers)
    }

    fun fetchUsersDummy() {
        println("fetchUsersDummy")
        setUsers(GitHub.dummyUsers)
    }

    fun clearUsers() {
        println("clearUsers")
        setUsers(emptyList())
    }

    fun dispatch(action: GhAction) {
        println("dispatch $action")
        when (action) {
            GhAction.FetchUsersAsync -> fetchUsersRemoteAsync()
            GhAction.FetchDummyUsers -> fetchUsersDummy()
            GhAction.ClearUsers -> clearUsers()
        }
    }
    MaterialTheme {
        GitHubVu(users = users, dispatch = ::dispatch)
    }
}

@Composable
fun GitHubPreview() {
    MaterialTheme {
        GitHubVu(GitHub.dummyUsers) {}
    }
}

@Composable
fun GitHubVu(users: List<User>, dispatch: GhDispatch) {
    Surface(color = Color.Magenta) {
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


@Composable
fun UsersVu(users: List<User>) {
    Surface(color = Color.Cyan) {
        VerticalScroller(modifier = Expanded) {
            Column(modifier = ExpandedWidth) {
                users.forEach {
                    Text(it.login)
                }
            }
        }
    }
}

@Composable
fun ButtonsVu(dispatch: GhDispatch) {
    Surface(color = Color.Yellow) {
        Column() {
            Btn(text = "Async", action = GhAction.FetchUsersAsync, dispatch = dispatch)
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


