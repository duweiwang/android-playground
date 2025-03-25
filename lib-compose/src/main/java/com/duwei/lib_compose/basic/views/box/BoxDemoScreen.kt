import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.duwei.lib_compose.R




@Composable
fun BoxScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        BasicBoxExample()
        StackedBoxExample()
        AlignedBoxExample()
        CustomBoxExample()
        ZIndexBoxExample()
        ResponsiveBoxExample()
        ResponsiveBoxExample()
        AdaptiveBoxSize()
        ProportionalBoxes()
        BadgeBoxExample(2)
        ComplexBoxLayout()
        OverlayBoxExample(true)
        GestureHandlingBox()
        SimulatedHoverTooltipExample()
    }

}


@Composable
fun BasicBoxExample() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.LightGray)
    ) {
        Text(
            text = "Hello, Box!",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun StackedBoxExample() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Overlay Text",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}


@Composable
fun AlignedBoxExample() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(Color.Gray)
    ) {
        Text(
            text = "Top Start",
            modifier = Modifier.align(Alignment.TopStart)
        )
        Text(
            text = "Center",
            modifier = Modifier.align(Alignment.Center)
        )
        Text(
            text = "Bottom End",
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}


@Composable
fun CustomBoxExample() {
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.Cyan, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Blue, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Custom Box",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun ZIndexBoxExample() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Red)
                .align(Alignment.Center)
                .zIndex(1f)
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Green)
                .align(Alignment.Center)
                .zIndex(2f)
        )
    }
}


@Composable
fun ResponsiveBoxExample() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        val boxWidth = maxWidth
        val boxHeight = maxHeight

        val dynamicBoxWidth = boxWidth * 0.7f
        val dynamicBoxHeight = boxHeight * 0.3f
        Box(
            modifier = Modifier
                .size(dynamicBoxWidth, dynamicBoxHeight)
                .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                .align(Alignment.Center)
        ) {
            Text(
                text = "70% Width, 30% Height",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun AdaptiveBoxSize() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth

        val sizeCategory = when {
            screenWidth < 400.dp -> "Small"
            screenWidth < 600.dp -> "Medium"
            else -> "Large"
        }
        val boxSize = when (sizeCategory) {
            "Small" -> screenWidth * 0.6f
            "Medium" -> screenWidth * 0.5f
            else -> screenWidth * 0.4f
        }
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(Color.Cyan, shape = RoundedCornerShape(16.dp))
                .align(Alignment.Center)
        ) {
            Text(
                text = "Size: $sizeCategory",
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun ProportionalBoxes() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        Box(
            modifier = Modifier
                .size(screenWidth * 0.4f, screenHeight * 0.4f)
                .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(screenWidth * 0.3f, screenHeight * 0.3f)
                .background(Color.Red, shape = RoundedCornerShape(8.dp))
                .align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .size(screenWidth * 0.2f, screenHeight * 0.2f)
                .background(Color.Green, shape = RoundedCornerShape(8.dp))
                .align(Alignment.BottomEnd)
        )
    }
}


@Composable
fun BadgeBoxExample(badgeCount: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Gray, shape = CircleShape)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.sample_image), // Replace with your icon resource
            contentDescription = "Cart",
            tint = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.Red, shape = CircleShape)
                    .align(Alignment.TopEnd)
                    .border(2.dp, Color.White, shape = CircleShape)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun ComplexBoxLayout() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Row(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Top Left",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Green)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Top Right",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Red)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Center",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Yellow)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Bottom Center",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun OverlayBoxExample(showOverlay: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = "Main Content",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 24.sp
        )

        if (showOverlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun GestureHandlingBox() {
    var color by remember { mutableStateOf(Color.Green) }

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(color)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        color = Color.Blue
                    },
                    onDoubleTap = {
                        color = Color.Red
                    },
                    onLongPress = {
                        color = Color.Yellow
                    }
                )
            }
    ) {
        Text(
            text = "Tap, Double Tap, or Long Press",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun SimulatedHoverTooltipExample() {
    var showTooltip by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showTooltip = true // Show tooltip on long press
                        },
                        onTap = {
                            showTooltip = false // Hide tooltip when tapping elsewhere
                        }
                    )
                }
        ) {
            Text(
                text = "Long Press Me",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            if (showTooltip) {
                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-24).dp)
                ) {
                    Text(
                        text = "I am a tooltip",
                        color = Color.White
                    )
                }
            }
        }
    }
}