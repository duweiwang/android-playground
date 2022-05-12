package com.duwei.lib_compose.basic

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author 杜伟
 * @date 2022/5/12 2:19 PM
 *
 */
@Composable
fun AnimationScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RotatingSquare()

        ChangeColorAndSize()
    }

}

@Composable
fun RotatingSquare() {
    // rememberInfiniteTransition is used to create a transition that uses infitine
    // child animations. Animations typically get invoked as soon as they enter the
    // composition so don't need to be explicitly started.
    val infiniteTransition = rememberInfiniteTransition()

    // Create a value that is altered by the transition based on the configuration. We use
    // the animated float value the returns and updates a float from the initial value to
    // target value and repeats it (as its called on the infititeTransition).
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween<Float>(
                durationMillis = 3000,
                easing = FastOutLinearInEasing,
            ),
        )
    )
    // We use the Canvas composable that gives you access to a canvas that you can draw
    // into. We also pass it a modifier.

    // You can think of Modifiers as implementations of the decorators pattern that are used
    // to modify the composable that its applied to. In this example, we assign a size
    // of 200dp to the Canvas using Modifier.preferredSize(200.dp).
    Canvas(modifier = Modifier.size(200.dp)) {
        // As the Transition is changing the interpolating the value of the animated float
        // "rotation", you get access to all the values including the intermediate values as
        // its  being updated. The value of "rotation" goes from 0 to 360 and transitions
        // infinitely due to the infiniteRepetable animationSpec used above.
        rotate(rotation) {
            drawRect(color = Color(255, 138, 128))
        }
    }
}

private sealed class BoxState(val color: Color, val size: Dp) {
    operator fun not() = if (this is Small) Large else Small

    object Small : BoxState(Blue, 64.dp)
    object Large : BoxState(Red, 128.dp)
}

@Composable
fun ChangeColorAndSize() {
    var boxState: BoxState by remember {
        mutableStateOf(BoxState.Small)
    }
    val transition = updateTransition(targetState = boxState)

    Column(Modifier.padding(16.dp)) {
        Spacer(Modifier.height(16.dp))

        val color by transition.animateColor {
            it.color
        }
        val size by transition.animateDp(transitionSpec = {
            if (targetState == BoxState.Large) {
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                spring(stiffness = Spring.StiffnessHigh)
            }
        }) {
            it.size
        }

        Button(
            onClick = { boxState = boxState.not() }
        ) {
            Text("Change Color and size")
        }
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .size(size)
                .background(color)
        )
    }
}