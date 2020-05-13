package com.smartsoft.github

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.Scaffold
import androidx.ui.res.vectorResource

enum class TabKey {
    Jobs, GhFlow, GhLive
}

@Composable
fun App(viewModel: AppViewModel) {

    val (tab, setTab) = state { TabKey.Jobs }

    Scaffold(
        bottomAppBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(vectorResource(R.drawable.ic_home)) },
                    selected = tab == TabKey.Jobs, onSelected = { setTab(TabKey.Jobs) },
                    text = { Text("Jobs") }
                )
                BottomNavigationItem(
                    icon = { Icon(vectorResource(R.drawable.ic_home)) },
                    selected = tab == TabKey.GhFlow, onSelected = { setTab(TabKey.GhFlow) },
                    text = { Text("GhFlow") }
                )
                BottomNavigationItem(
                    icon = { Icon(vectorResource(R.drawable.ic_home)) },
                    selected = tab == TabKey.GhLive, onSelected = { setTab(TabKey.GhLive) },
                    text = { Text("GhLive") }
                )
            }
        },
        bodyContent = {
            when (tab) {
                TabKey.Jobs -> JobsPage(viewModel.jobFlow)
                TabKey.GhFlow -> GhFlowPage(viewModel.ghFlow)
                TabKey.GhLive -> GhLivePage(viewModel.ghLive)

            }

        }

    )


}
