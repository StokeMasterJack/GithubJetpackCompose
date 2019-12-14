package com.smartsoft.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview

enum class GhMainMenuAction {
    State,
    ViewModel
}

typealias GhMainMenuDispatch = (ev: GhMainMenuAction) -> Unit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainMenu(this)
        }
    }
}


@Composable
fun MainMenu(ctx: Context) {

    fun startGitHubActivityUsingState() {
        val intent = Intent(ctx, GitHubActivityUsingState::class.java)
        ctx.startActivity(intent)
    }

    fun startGitHubActivityUsingViewModel() {
        val intent = Intent(ctx, GitHubActivityUsingViewModel::class.java)
        ctx.startActivity(intent)
    }

    fun dispatch(action: GhMainMenuAction) {
        println("dispatch $action")
        when (action) {
            GhMainMenuAction.State -> startGitHubActivityUsingState()
            GhMainMenuAction.ViewModel -> startGitHubActivityUsingViewModel()
        }
    }

    MainMenuVu(::dispatch)

}

@Composable
fun MainMenuVu(dispatch: GhMainMenuDispatch) {
    MaterialTheme {
        Column {
            TopAppBar(
                title = { Text("GitHub Jetpack") }
            )
            Row(modifier = Expanded, arrangement = Arrangement.Center) {
                Column(
                    modifier = Spacing(8.dp).wraps(ExpandedHeight),
                    arrangement = Arrangement.Center
                ) {
                    Button(
                        text = "Using State",
                        modifier = Spacing(8.dp).wraps(Width(200.dp)),
                        onClick = { dispatch(GhMainMenuAction.State) }
                    )
                    Button(
                        text = "Using ViewModel",
                        modifier = Spacing(8.dp).wraps(Width(200.dp)),
                        onClick = { dispatch(GhMainMenuAction.ViewModel) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview2() {
    MainMenuVu {}
}
