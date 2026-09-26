package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// -------------------------------------------------------------------------
// Supported Lyrics / Chat Bubble Overlay Themes
// -------------------------------------------------------------------------
enum class BubbleTheme(val id: String, val title: String, val emoji: String) {
    CLASSIC("CLASSIC", "Classic Instagram", "💬"),
    OBSIDIAN_HEART("OBSIDIAN_HEART", "Obsidian Ruby Hearts", "❤️‍🔥"),
    MIDNIGHT_BUTTERFLY("MIDNIGHT_BUTTERFLY", "Midnight Butterfly", "🦋"),
    NEON_CYBER("NEON_CYBER", "Cyber Neon Glow", "⚡"),
    GOLDEN_LUXE("GOLDEN_LUXE", "Golden Sparkle Luxe", "✨")
}

// -------------------------------------------------------------------------
// 1. Ruby Heart Vector Component (Precise Bézier Curve + Radiant Gradient)
// -------------------------------------------------------------------------
@Composable
fun RubyHeart(
    size: Dp,
    rotation: Float = 0f,
    modifier: Modifier = Modifier
) {
    val rubyBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF758C),
            Color(0xFFFF1654),
            Color(0xFF990024)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Canvas(
        modifier = modifier
            .size(size)
            .rotate(rotation)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(size / 2),
                ambientColor = Color(0xFFFF1654),
                spotColor = Color(0xFFFF1654)
            )
    ) {
        val w = this.size.width
        val h = this.size.height

        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.88f)
            cubicTo(
                w * 0.12f, h * 0.62f,
                0f, h * 0.42f,
                0f, h * 0.26f
            )
            cubicTo(
                0f, h * 0.08f,
                w * 0.22f, 0f,
                w * 0.5f, h * 0.28f
            )
            cubicTo(
                w * 0.78f, 0f,
                w, h * 0.08f,
                w, h * 0.26f
            )
            cubicTo(
                w, h * 0.42f,
                w * 0.88f, h * 0.62f,
                w * 0.5f, h * 0.88f
            )
            close()
        }

        drawPath(path = path, brush = rubyBrush)
    }
}

// -------------------------------------------------------------------------
// 2. Midnight Glowing Butterfly Vector Component
// -------------------------------------------------------------------------
@Composable
fun GlowingButterfly(
    size: Dp,
    rotation: Float = 0f,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFFD500F9),
    modifier: Modifier = Modifier
) {
    val butterflyBrush = Brush.linearGradient(
        colors = listOf(
            primaryColor,
            secondaryColor,
            Color(0xFF651FFF)
        )
    )

    Canvas(
        modifier = modifier
            .size(size)
            .rotate(rotation)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(size / 2),
                ambientColor = primaryColor,
                spotColor = secondaryColor
            )
    ) {
        val w = this.size.width
        val h = this.size.height

        // Left upper wing
        val leftUpperWing = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            cubicTo(w * 0.3f, h * 0.1f, w * 0.05f, h * 0.15f, w * 0.08f, h * 0.4f)
            cubicTo(w * 0.1f, h * 0.6f, w * 0.35f, h * 0.6f, w * 0.5f, h * 0.5f)
            close()
        }
        // Right upper wing
        val rightUpperWing = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            cubicTo(w * 0.7f, h * 0.1f, w * 0.95f, h * 0.15f, w * 0.92f, h * 0.4f)
            cubicTo(w * 0.9f, h * 0.6f, w * 0.65f, h * 0.6f, w * 0.5f, h * 0.5f)
            close()
        }
        // Left lower wing
        val leftLowerWing = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            cubicTo(w * 0.35f, h * 0.65f, w * 0.15f, h * 0.75f, w * 0.22f, h * 0.9f)
            cubicTo(w * 0.3f, h * 0.98f, w * 0.45f, h * 0.75f, w * 0.5f, h * 0.5f)
            close()
        }
        // Right lower wing
        val rightLowerWing = Path().apply {
            moveTo(w * 0.5f, h * 0.5f)
            cubicTo(w * 0.65f, h * 0.65f, w * 0.85f, h * 0.75f, w * 0.78f, h * 0.9f)
            cubicTo(w * 0.7f, h * 0.98f, w * 0.55f, h * 0.75f, w * 0.5f, h * 0.5f)
            close()
        }

        drawPath(path = leftUpperWing, brush = butterflyBrush)
        drawPath(path = rightUpperWing, brush = butterflyBrush)
        drawPath(path = leftLowerWing, brush = butterflyBrush)
        drawPath(path = rightLowerWing, brush = butterflyBrush)

        // Center body line
        drawLine(
            color = Color.White,
            start = Offset(w * 0.5f, h * 0.3f),
            end = Offset(w * 0.5f, h * 0.75f),
            strokeWidth = 3f
        )
    }
}

// -------------------------------------------------------------------------
// 3. Golden Luxe Sparkle Vector
// -------------------------------------------------------------------------
@Composable
fun LuxeSparkle(
    size: Dp,
    rotation: Float = 0f,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(size)
            .rotate(rotation)
    ) {
        val w = this.size.width
        val h = this.size.height

        val starPath = Path().apply {
            moveTo(w * 0.5f, 0f)
            cubicTo(w * 0.5f, h * 0.35f, w * 0.65f, h * 0.5f, w, h * 0.5f)
            cubicTo(w * 0.65f, h * 0.5f, w * 0.5f, h * 0.65f, w * 0.5f, h)
            cubicTo(w * 0.5f, h * 0.65f, w * 0.35f, h * 0.5f, 0f, h * 0.5f)
            cubicTo(w * 0.35f, h * 0.5f, w * 0.5f, h * 0.35f, w * 0.5f, 0f)
            close()
        }

        val goldBrush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFF7C2), Color(0xFFFFD700), Color(0xFFFF8C00)),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.6f
        )

        drawPath(path = starPath, brush = goldBrush)
    }
}

// -------------------------------------------------------------------------
// Themed Styled Lyrics Bubble Wrapper
// Form-fitting: Normal Instagram bubble sizing matching the web app
// -------------------------------------------------------------------------
@Composable
fun AestheticLyricsBubble(
    text: String,
    theme: BubbleTheme,
    isFromMe: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bubbleShape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = if (isFromMe) 18.dp else 4.dp,
        bottomEnd = if (isFromMe) 4.dp else 18.dp
    )

    when (theme) {
        BubbleTheme.OBSIDIAN_HEART -> {
            val bg = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1B0C16),
                    Color(0xFF2A0822),
                    Color(0xFF150616)
                )
            )

            Box(
                modifier = modifier
                    .widthIn(max = 280.dp)
                    .background(
                        brush = bg,
                        shape = bubbleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFFFF2A6D),
                        shape = bubbleShape
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // Top-Right subtle ruby heart
                RubyHeart(
                    size = 16.dp,
                    rotation = -16f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-5).dp)
                )

                // Bottom-Left subtle ruby heart
                RubyHeart(
                    size = 12.dp,
                    rotation = 14f,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-6).dp, y = 4.dp)
                )

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 14.5.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default
                )
            }
        }

        BubbleTheme.MIDNIGHT_BUTTERFLY -> {
            val bg = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF0F0C29),
                    Color(0xFF302B63),
                    Color(0xFF24243E)
                )
            )

            Box(
                modifier = modifier
                    .widthIn(max = 280.dp)
                    .background(
                        brush = bg,
                        shape = bubbleShape
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF00E5FF), Color(0xFFD500F9))
                        ),
                        shape = bubbleShape
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // Top-Right butterfly
                GlowingButterfly(
                    size = 18.dp,
                    rotation = -18f,
                    primaryColor = Color(0xFF00E5FF),
                    secondaryColor = Color(0xFFD500F9),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-5).dp)
                )

                // Bottom-Left butterfly
                GlowingButterfly(
                    size = 13.dp,
                    rotation = 16f,
                    primaryColor = Color(0xFFFF4081),
                    secondaryColor = Color(0xFF7C4DFF),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-6).dp, y = 4.dp)
                )

                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 14.5.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default
                )
            }
        }

        BubbleTheme.NEON_CYBER -> {
            val bg = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF051923),
                    Color(0xFF003554),
                    Color(0xFF006494)
                )
            )

            Box(
                modifier = modifier
                    .widthIn(max = 280.dp)
                    .background(
                        brush = bg,
                        shape = bubbleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF00F0FF),
                        shape = bubbleShape
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                LuxeSparkle(
                    size = 16.dp,
                    rotation = 45f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                )

                LuxeSparkle(
                    size = 12.dp,
                    rotation = 15f,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-4).dp, y = 4.dp)
                )

                Text(
                    text = text,
                    color = Color(0xFFE0FBFC),
                    fontSize = 14.5.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default
                )
            }
        }

        BubbleTheme.GOLDEN_LUXE -> {
            val bg = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF2C1E03),
                    Color(0xFF4A3408),
                    Color(0xFF1E1402)
                )
            )

            Box(
                modifier = modifier
                    .widthIn(max = 280.dp)
                    .background(
                        brush = bg,
                        shape = bubbleShape
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFFF7C2), Color(0xFFFFD700), Color(0xFFB8860B))
                        ),
                        shape = bubbleShape
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                LuxeSparkle(
                    size = 16.dp,
                    rotation = 12f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                )

                LuxeSparkle(
                    size = 12.dp,
                    rotation = -20f,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-4).dp, y = 4.dp)
                )

                Text(
                    text = text,
                    color = Color(0xFFFFFDF0),
                    fontSize = 14.5.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default
                )
            }
        }

        else -> {
            Box(
                modifier = modifier
                    .widthIn(max = 280.dp)
                    .clip(bubbleShape)
                    .background(
                        if (isFromMe) Brush.horizontalGradient(
                            listOf(Color(0xFF3870F8), Color(0xFF7A3FE4), Color(0xFFB832B0), Color(0xFFE024A8))
                        )
                        else androidx.compose.ui.graphics.SolidColor(Color(0xFF262626))
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 14.5.sp,
                    lineHeight = 19.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default
                )
            }
        }
    }
}
