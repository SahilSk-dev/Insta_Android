package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShieldHeartIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    size: Dp = 28.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = 2.dp.toPx()

        // Draw Shield outline
        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            cubicTo(w * 0.75f, h * 0.12f, w * 0.88f, h * 0.16f, w * 0.88f, h * 0.35f)
            cubicTo(w * 0.88f, h * 0.65f, w * 0.65f, h * 0.82f, w * 0.5f, h * 0.88f)
            cubicTo(w * 0.35f, h * 0.82f, w * 0.12f, h * 0.65f, w * 0.12f, h * 0.35f)
            cubicTo(w * 0.12f, h * 0.16f, w * 0.25f, h * 0.12f, w * 0.5f, h * 0.12f)
            close()
        }
        drawPath(
            path = shieldPath,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Draw small heart inside shield
        val heartPath = Path().apply {
            val hw = w * 0.5f
            val hh = h * 0.5f
            val s = w * 0.14f
            moveTo(hw, hh + s * 0.8f)
            cubicTo(hw - s * 1.2f, hh + s * 0.2f, hw - s * 1.2f, hh - s * 0.8f, hw - s * 0.5f, hh - s * 0.8f)
            cubicTo(hw - s * 0.1f, hh - s * 0.8f, hw, hh - s * 0.3f, hw, hh - s * 0.3f)
            cubicTo(hw, hh - s * 0.3f, hw + s * 0.1f, hh - s * 0.8f, hw + s * 0.5f, hh - s * 0.8f)
            cubicTo(hw + s * 1.2f, hh - s * 0.8f, hw + s * 1.2f, hh + s * 0.2f, hw, hh + s * 0.8f)
            close()
        }
        drawPath(
            path = heartPath,
            color = tint,
            style = Stroke(width = strokeWidth * 0.85f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun BlockSlashIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    size: Dp = 28.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = 2.dp.toPx()
        val radius = (w.coerceAtMost(h) * 0.38f)

        // Draw circle
        drawCircle(
            color = tint,
            radius = radius,
            center = Offset(w / 2f, h / 2f),
            style = Stroke(width = strokeWidth)
        )

        // Draw diagonal slash across circle (45 degrees)
        val offset = radius * 0.7071f
        drawLine(
            color = tint,
            start = Offset(w / 2f - offset, h / 2f - offset),
            end = Offset(w / 2f + offset, h / 2f + offset),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun InstagramTagIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    size: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = 1.8.dp.toPx()

        // Tag / Label shape with angled cut and hole
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.35f)
            lineTo(w * 0.55f, h * 0.15f)
            cubicTo(w * 0.6f, h * 0.12f, w * 0.65f, h * 0.12f, w * 0.7f, h * 0.15f)
            lineTo(w * 0.88f, h * 0.33f)
            cubicTo(w * 0.92f, h * 0.37f, w * 0.92f, h * 0.43f, w * 0.88f, h * 0.47f)
            lineTo(w * 0.48f, h * 0.87f)
            cubicTo(w * 0.44f, h * 0.91f, w * 0.38f, h * 0.91f, w * 0.34f, h * 0.87f)
            lineTo(w * 0.15f, h * 0.68f)
            cubicTo(w * 0.11f, h * 0.64f, w * 0.11f, h * 0.58f, w * 0.15f, h * 0.54f)
            close()
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Circle dot inside tag
        drawCircle(
            color = tint,
            radius = w * 0.05f,
            center = Offset(w * 0.68f, h * 0.32f)
        )
    }
}

@Composable
fun InstagramSmileyBubbleIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    size: Dp = 24.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = 1.8.dp.toPx()

        // Speech bubble with round corners and tail
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            cubicTo(w * 0.85f, h * 0.12f, w * 0.88f, h * 0.35f, w * 0.88f, h * 0.5f)
            cubicTo(w * 0.88f, h * 0.75f, w * 0.65f, h * 0.85f, w * 0.45f, h * 0.85f)
            lineTo(w * 0.28f, h * 0.92f)
            cubicTo(w * 0.23f, h * 0.94f, w * 0.18f, h * 0.9f, w * 0.2f, h * 0.85f)
            lineTo(w * 0.22f, h * 0.78f)
            cubicTo(w * 0.12f, h * 0.72f, w * 0.12f, h * 0.6f, w * 0.12f, h * 0.5f)
            cubicTo(w * 0.12f, h * 0.25f, w * 0.3f, h * 0.12f, w * 0.5f, h * 0.12f)
            close()
        }
        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Two smiley eyes
        drawCircle(
            color = tint,
            radius = w * 0.04f,
            center = Offset(w * 0.38f, h * 0.45f)
        )
        drawCircle(
            color = tint,
            radius = w * 0.04f,
            center = Offset(w * 0.62f, h * 0.45f)
        )

        // Smile arc
        val smilePath = Path().apply {
            moveTo(w * 0.36f, h * 0.58f)
            quadraticBezierTo(w * 0.5f, h * 0.7f, w * 0.64f, h * 0.58f)
        }
        drawPath(
            path = smilePath,
            color = tint,
            style = Stroke(width = strokeWidth * 0.9f, cap = StrokeCap.Round)
        )
    }
}
