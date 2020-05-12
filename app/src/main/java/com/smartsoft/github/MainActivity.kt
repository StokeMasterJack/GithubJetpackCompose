package com.smartsoft.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp

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
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalGravity = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalGravity = Alignment.CenterHorizontally
                ) {
                    Button(
                        text = {Text("Using State")},
                        modifier = Modifier.padding(8.dp).width(200.dp),
                        onClick = { dispatch(GhMainMenuAction.State) }
                    )
                    Button(
                        text = {Text("Using ViewModel")},
                        modifier = Modifier.padding(8.dp).width(200.dp),
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
