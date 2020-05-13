package com.smartsoft.github

import androidx.compose.Composable
import androidx.compose.onActive
import androidx.ui.core.Modifier
import androidx.ui.foundation.AdapterList
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.livedata.observeAsState
import androidx.ui.material.ListItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.TopAppBar
import androidx.ui.res.vectorResource
import androidx.ui.unit.dp
import ss.ssutil.UiState

@Composable
fun JobsPage(model: JobFlow) {

    val state = model.jobsLiveData.observeAsState()
    val jobs: UiState<RPage> = state.value ?: UiState.Success(RPage())
    val dispatch: IDispatch<RAction> = { model.update(it) }

    onActive {
        L.w("JobsPage.onActive")
        dispatch(RAction.Query())
        onDispose {
            L.w("JobsPage.onDispose")
        }
    }

    RJobsVu(page = jobs, title = "Jobs", dispatch = dispatch)
}


@Composable
fun RJobsVu(
    page: UiState<RPage> = UiState.Success(RPage()),
    title: String = "Jobs",
    dispatch: IDispatch<RAction> = {}
) {
    MaterialTheme {
        val colors = MaterialTheme.colors
        Surface(color = colors.background) {
            Column() {
                TopAppBar(
                    title = { Text(title) }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                        JobList(page)
                    }
                }
            }
        }
    }
}


@Composable
fun JobList(page: UiState<RPage>) {
    when (page) {
        is UiState.NotStarted -> JobsScrollVu(RPage())
        is UiState.Error -> ErrorVu(page.exception)
        is UiState.Loading -> LoadingVu()
        is UiState.Success -> JobsScrollVu1(page.data)
    }
}

@Composable
fun JobsScrollVu(page: RPage) {
    val jobs = page.rows
    VerticalScroller(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            jobs.forEach {
                Row {
                    ListItem(
                        text = { Text(it.customerName) },
                        icon = { Icon(vectorResource(R.drawable.ic_check)) },
                        secondaryText = { Text(it.openDate) }
                    )
                }
            }
        }
    }
}

@Composable
fun JobsScrollVu1(page: RPage) {
    val jobs = page.rows
    AdapterList(
        modifier = Modifier.fillMaxSize(),
        data = jobs,
        itemCallback = { it ->
            ListItem(
                text = { Text(it.customerName) },
                icon = { Icon(vectorResource(R.drawable.ic_check)) },
                secondaryText = { Text(it.openDate) }
            )
        }
    )
}

