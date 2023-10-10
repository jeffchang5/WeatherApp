package me.jeffreychang.weatherapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Sun() {
    Box(modifier = Modifier.size(120.dp)) {
        // Draw the sun with a gradient color
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Yellow,
                radius = size.minDimension / 2,
                center = center
            )
        }

        // Draw the sun rays using a Path
        Canvas(modifier = Modifier.fillMaxSize()) {
            val raysPath = Path()
            val rayStart = Offset(size.width / 2, size.height / 2)
            val rayEnd = Offset(size.width / 2, 0f)
            raysPath.moveTo(rayStart.x, rayStart.y)
            raysPath.lineTo(rayEnd.x, rayEnd.y)
            for (i in 1..7) {
                raysPath.addArc(
                    Rect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height
                    ),
                    -90f + (i * 45f),
                    20f
                )
                raysPath.lineTo(rayStart.x, rayStart.y)
            }
            drawPath(
                path = raysPath,
                color = Color.Magenta,
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}

@Composable
fun RainingCloud() {
    val cloudColor = Color.LightGray
    val rainColor = Color.DarkGray

    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
        // Draw the cloud shape using a combination of shapes
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cloudPath = Path().apply {
                moveTo(size.width / 3, size.height / 5)
                relativeQuadraticBezierTo(-60f, -20f, -60f, -80f)
                relativeQuadraticBezierTo(0f, -50f, 50f, -50f)
                relativeQuadraticBezierTo(20f, 0f, 30f, 10f)
                relativeQuadraticBezierTo(20f, -10f, 40f, -10f)
                relativeQuadraticBezierTo(60f, 0f, 60f, 60f)
                relativeQuadraticBezierTo(0f, 30f, -20f, 40f)
                relativeQuadraticBezierTo(-10f, 10f, -30f, 10f)
                relativeQuadraticBezierTo(-20f, 0f, -40f, -20f)
                relativeQuadraticBezierTo(-20f, 20f, -50f, 20f)
                close()
            }
            val cloudStrokeWidth = 8.dp.toPx()
            val cloudStrokePathEffect = PathEffect.dashPathEffect(
                floatArrayOf(cloudStrokeWidth, cloudStrokeWidth),
                0f
            )
            drawPath(
                path = cloudPath,
                color = cloudColor,
                style = Stroke(
                    width = cloudStrokeWidth,
                    pathEffect = cloudStrokePathEffect
                )
            )
            val cloudShadow = Path().apply {
                addPath(cloudPath)
//                offset(10.dp.toPx(), 10.dp.toPx())
            }
            drawPath(
                path = cloudShadow,
                color = Color.Black.copy(alpha = 0.1f),
                style = Fill
            )
        }

//        // Draw the rain drops using a repeating icon
//        Icon(
//            painter = painterResource(R.drawable.ic_drop),
//            contentDescription = null,
//            tint = rainColor,
//            modifier = Modifier.repeat(10) {
//                val offsetX = (0..(size.width.toInt())).random().toFloat()
//                val offsetY = (0..(size.height.toInt())).random().toFloat()
//                Modifier.offset(x = offsetX, y = offsetY)
//            }
//        )
    }
}