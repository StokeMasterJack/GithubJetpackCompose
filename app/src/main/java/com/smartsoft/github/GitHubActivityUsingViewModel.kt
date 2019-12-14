package com.smartsoft.github

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.ui.core.setContent

/*
ViewModels are no longer needed with Jetpack Compose.
But, since Schwab specifically asked how to create a ViewModel in Kotlin, here is the same app using a view model
 */
class GitHubActivityUsingViewModel : AppCompatActivity() {

    private val appCtx: AppCtx = AppCtx()
    private val viewModel: GhViewModel = appCtx.mkGhViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.observe(this) {
            setContent {
                GitHubAppUsingViewModel(gitHub = appCtx.mkGitHub(), viewModel = viewModel)
            }
        }
        viewModel.users.value = emptyList()
    }

}

@Composable
fun GitHubAppUsingViewModel(gitHub: GitHub, viewModel: GhViewModel) {

    fun fetchUsersRemoteAsync() {
        gitHub.fetchUsersAsync(viewModel.users::postValue)
    }

    fun fetchUsersDummy() {
        viewModel.users.value = GitHub.dummyUsers
    }

    fun clearUsers() {
        viewModel.users.value = emptyList()
    }

    fun dispatch(action: GhAction) {
        when (action) {
            GhAction.FetchUsersAsync -> fetchUsersRemoteAsync()
            GhAction.FetchDummyUsers -> fetchUsersDummy()
            GhAction.ClearUsers -> clearUsers()
        }
    }

    val user = viewModel.users.value ?: emptyList()

    GitHubVu(users = user, title = "GitHub with ViewModel", dispatch = ::dispatch)
}

