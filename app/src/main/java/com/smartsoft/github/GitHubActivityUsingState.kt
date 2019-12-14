package com.smartsoft.github

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.setContent


class GitHubActivityUsingState : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitHubAppUsingState()
        }
    }

}

@Composable
fun GitHubAppUsingState() {

    val (users, setUsers) = +state { emptyList<User>() }

    val appCtx = AppCtx()
    val gitHub = appCtx.mkGitHub()

    fun fetchUsersRemoteAsync() {
        gitHub.fetchUsersAsync(setUsers)
    }

    fun fetchUsersDummy() {
        setUsers(GitHub.dummyUsers)
    }

    fun clearUsers() {
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

    GitHubVu(users = users, title = "GitHub with State", dispatch = ::dispatch)
}

