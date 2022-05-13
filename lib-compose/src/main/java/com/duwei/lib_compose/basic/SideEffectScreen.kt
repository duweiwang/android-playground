package com.duwei.lib_compose.basic

import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * @author 杜伟
 * @date 2022/5/13 1:57 PM
 *
 * https://medium.com/mobile-app-development-publication/jetpack-compose-side-effects-made-easy-a4867f876928
 *
 */


@Composable
fun SideEffectScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchEffectComponent()

        SideEffectComponent()
    }
}

/**
 * LaunchedEffect的使用，只在首次启动的时候执行
 * 生命周期同所在函数
 */
@Composable
fun LaunchEffectComponent() {
    var timer by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Time $timer")
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000)
            timer++
        }
    }
}

/**
 * 每次recomposition的时候触发，但不是协程作用域，不能执行挂起函数
 */
@Composable
fun SideEffectComponent() {
    var timer by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Time $timer")
    }

    SideEffect {
        //这里不能使用delay，因为不在协程的作用域
        Thread.sleep(1000)
        timer++
    }
}

/**
 * 在LaunchEffect中，当使用Key去触发relaunch时，之前执行的逻辑如果没结束，会被取消。
 * 我们可以使用try-catch捕获取消。也可以使用[DisposableEffect]来简化。取消会回调onDispose方法
 */
@Composable
fun DisposableEffectComponent() {
    var timerStartStop by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                timerStartStop = !timerStartStop
            }) {
                Text(if (timerStartStop) "Stop" else "Start")
            }
        }
    }

    val context = LocalContext.current

    DisposableEffect(key1 = timerStartStop) {
        val x = (1..10).random()
        Toast.makeText(context, "Start $x", LENGTH_SHORT).show()

        onDispose {
            Toast.makeText(context, "Stop $x", LENGTH_SHORT).show()
        }
    }
}

/**
 * 在[LaunchedEffect]中，想要走recomposition需要修改在外部声明的状态
 *
 * [produceState]会自己在内部维护状态
 */
@Composable
fun produceStateComponent() {
    var timerStartStop by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val timer by produceState(initialValue = 0, timerStartStop) {
        val x = (1..10).random()
        var job: Job? = null
        Toast.makeText(context, "Start $x", LENGTH_SHORT).show()
        if (timerStartStop) {
            // Use MainScope to ensure awaitDispose is triggered
            job = MainScope().launch {
                while (true) {
                    delay(1000)
                    value++
                }
            }
        }

        awaitDispose {
            Toast.makeText(context, "Done $x", LENGTH_SHORT).show()
            job?.cancel()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Time $timer")
            Button(onClick = {
                timerStartStop = !timerStartStop
            }) {
                Text(if (timerStartStop) "Stop" else "Start")
            }
        }
    }
}

/**
 * 对于[LaunchedEffect]来说，在首次composition时启动。想要手动控制协程，使用
 * [rememberCoroutineScope]
 */
@Composable
fun rememberCoroutineScopeComponent() {
    val scope = rememberCoroutineScope()
    var timer by remember { mutableStateOf(0) }
    var timerStartStop by remember { mutableStateOf(false) }
    var job: Job? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Time $timer")
            Button(onClick = {
                timerStartStop = !timerStartStop

                if (timerStartStop) {
                    job?.cancel()
                    job = scope.launch {
                        while (true) {
                            delay(1000)
                            timer++
                        }
                    }
                } else {
                    job?.cancel()
                }

            }) {
                Text(if (timerStartStop) "Stop" else "Start")
            }
        }
    }
}


/**
 * 注意和 mutableStateOf 的区别
 */
@ExperimentalAnimationApi
@Composable
fun deriveStateOfComponent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(1000) { index ->
                Text(text = "Item: $index")
            }
        }

        val showButtonDerive by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        Log.d("Track", "Recompose")
        Column {
            AnimatedVisibility(showButtonDerive) {
                Button({}) {
                    Text("Row 1 hiding")
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun snapshotFlowComponent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(1000) { index ->
                Text(text = "Item: $index")
            }
        }

        var showButtonSnapshot by remember {
            mutableStateOf(false)
        }


        Log.d("Track", "Recompose")
        Column {
            AnimatedVisibility(showButtonSnapshot) {
                Button({}) {
                    Text("Row 1 and 2 hiding")
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index -> index > 2 }
                .distinctUntilChanged()
                .collect {
                    showButtonSnapshot = it
                }
        }
    }
}

/**
 * To ensure we always get the state variable value, we should use rememberUpdatedState.
 */
@Composable
fun rememberUpdatedStateComponent(timer: Int) {
    Text("Time $timer")
    val myRememberTimer by rememberUpdatedState(timer)
    LaunchedEffect(key1 = Unit) {
        delay(1000)
        Log.d("Track", "$myRememberTimer")
    }
}


