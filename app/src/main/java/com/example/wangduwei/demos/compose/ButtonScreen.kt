package com.example.wangduwei.demos.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp

/**
 * @author 杜伟
 * @date 2022/4/22 9:04 PM
 *
 */
@Composable
fun ButtonScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleButtonComponent()
        SimpleButtonWithBorderComponent()
        RoundedCornerButtonComponent()
        OutlinedButtonComponent()
        TextButtonComponent()
    }

}

@Composable
fun SimpleButtonComponent() {
    // Button is a pre-defined Material Design implementation of a contained button -
    // https://material.io/design/components/buttons.html#contained-button.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, we assign a padding of
    // 16dp to the Button.
    Button(
        modifier = Modifier.padding(16.dp),
        elevation = ButtonDefaults.elevation(5.dp),
        onClick = {}) {
        // The Button composable allows you to provide child composables that inherit this button
        // functionality.
        // The Text composable is pre-defined by the Compose UI library; you can use this
        // composable to render text on the screen
        Text(text = "Simple button", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun SimpleButtonWithBorderComponent() {
    // Button is a pre-defined Material Design implementation of a contained button -
    // https://material.io/design/components/buttons.html#contained-button.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, we assign a padding of
    // 16dp to the Button.
    Button(
        onClick = {},
        modifier = Modifier.padding(16.dp),
        elevation = ButtonDefaults.elevation(5.dp),
        // Provide a border for this button
        border = BorderStroke(width = 5.dp, brush = SolidColor(Color.Black))
    ) {
        // The Button composable allows you to provide child composables that inherit this button
        // functionality.
        // The Text composable is pre-defined by the Compose UI library; you can use this
        // composable to render text on the screen
        Text(text = "Simple button with border", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun RoundedCornerButtonComponent() {
    // Button is a pre-defined Material Design implementation of a contained button -
    // https://material.io/design/components/buttons.html#contained-button.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, we assign a padding of
    // 16dp to the Button.
    Button(
        onClick = {},
        modifier = Modifier.padding(16.dp),
        // Provide a custom shape for this button. In this example. we specify the button to have
        // round corners of 16dp radius.
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevation(5.dp),
    ) {
        // The Button composable allows you to provide child composables that inherit this button
        // functionality.
        // The Text composable is pre-defined by the Compose UI library; you can use this
        // composable to render text on the screen
        Text(text = "Button with rounded corners", modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun OutlinedButtonComponent() {
    // Button is a pre-defined Material Design implementation of a outlined button -
    // https://material.io/design/components/buttons.html#outlined-button.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, we assign a padding of
    // 16dp to the Button.
    OutlinedButton(
        onClick = {},
        modifier = Modifier.padding(16.dp)
    ) {
        // The Button composable allows you to provide child composables that inherit this button
        // functionality.
        // The Text composable is pre-defined by the Compose UI library; you can use this
        // composable to render text on the screen
        Text(text = "Outlined Button", modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun TextButtonComponent() {
    // Button is a pre-defined Material Design implementation of a text button -
    // https://material.io/design/components/buttons.html#text-button.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In this example, we assign a padding of
    // 16dp to the Button.
    TextButton(
        onClick = {},
        modifier = Modifier.padding(16.dp)
    ) {
        // The Button composable allows you to provide child composables that inherit this button
        // functionality.
        // The Text composable is pre-defined by the Compose UI library; you can use this
        // composable to render text on the screen
        Text(text = "Text Button", modifier = Modifier.padding(16.dp))
    }
}


