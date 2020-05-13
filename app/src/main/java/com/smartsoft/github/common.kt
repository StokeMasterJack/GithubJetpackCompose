package com.smartsoft.github

import android.util.Log
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.padding
import androidx.ui.layout.width
import androidx.ui.material.*
import androidx.ui.unit.dp


const val TAG = "GitHubCompose"

enum class GhAction {
    Fetch,
    Dummy,
    Clear
}

sealed class RAction {
    data class Query(val offset: Int = 0, val limit: Int = 100) : RAction()
    object Clear : RAction()
}

typealias IDispatch<A> = (ev: A) -> Unit

object L {
    fun w(msg: String) {
        Log.w(TAG, msg)
    }
}

@Composable
fun LoadingVu() {
    CircularProgressIndicator()
}

@Composable
fun ErrorVu(e: Exception) {
    val (c, t) = Theme
    Column {
        Text(text = "Error", style = t.subtitle1.copy(color = c.error))
        Text(text = e.message ?: "No message")
        Text(text = e.javaClass.simpleName)
    }
}

object Theme {
    @Composable
    val colors: ColorPalette
        get() = MaterialTheme.colors

    @Composable
    val c: ColorPalette
        get() = colors

    @Composable
    val typography: Typography
        get() = MaterialTheme.typography

    @Composable
    val t: Typography
        get() = typography

    @Composable
    val emphasis: EmphasisLevels
        get() = EmphasisAmbient.current

    @Composable
    val e: EmphasisLevels
        get() = emphasis

    @Composable
    val shapes: Shapes
        get() = MaterialTheme.shapes

    @Composable
    operator fun component1() = colors

    @Composable
    operator fun component2() = typography

    @Composable
    operator fun component3() = emphasis
}

@Composable
fun <A> Btn(text: String, action: A, dispatch: IDispatch<A>) {
    Button(
        text = { Text(text) },
        modifier = Modifier.padding(8.dp).width(100.dp),
        onClick = { dispatch(action) }
    )
}