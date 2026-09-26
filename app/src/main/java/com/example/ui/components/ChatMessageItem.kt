package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatMessageEntity
import com.example.ui.theme.InstagramBubblePurple
import com.example.ui.theme.InstagramBubbleReceived
import com.example.ui.theme.InstagramPlaceholder

// Authentic Instagram Direct Message Gradient (Purple #7038F8 -> Violet #8A3FFC -> Magenta/Pink #A020F0)
val InstagramMyBubbleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF7038F8),
        Color(0xFF8A3FFC),
        Color(0xFF9E27E8)
    )
)

@Composable
fun ChatTimestamp(
    timestamp: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = timestamp,
            color = InstagramPlaceholder,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Default
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatMessageItem(
    message: ChatMessageEntity,
    senderName: String,
    avatarName: String,
    onAvatarClick: () -> Unit,
    onLongClickMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMe = message.isFromMe

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (isMe) 54.dp else 8.dp,
                end = if (isMe) 8.dp else 54.dp,
                top = 2.dp,
                bottom = 2.dp
            ),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            // Received message pure round avatar
            RoundAvatar(
                avatarName = avatarName,
                contentDescription = senderName,
                size = 28.dp,
                onClick = onAvatarClick,
                modifier = Modifier.padding(end = 8.dp, bottom = 2.dp)
            )
        }

        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            when (message.type) {
                "IMAGE" -> {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF262626))
                            .combinedClickable(
                                onClick = onLongClickMessage,
                                onLongClick = onLongClickMessage
                            )
                            .testTag("chat_image_${message.id}")
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gaming_post),
                            contentDescription = "Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }
                }

                "STICKER" -> {
                    // Render single or multiple big sticker/reaction using system emoji
                    Box(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = onLongClickMessage,
                                onLongClick = onLongClickMessage
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = message.text,
                            fontSize = 38.sp,
                            fontFamily = FontFamily.Default
                        )
                    }
                }

                "AUDIO" -> {
                    Row(
                        modifier = Modifier
                            .widthIn(min = 160.dp, max = 220.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .then(
                                if (isMe) Modifier.background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFF7038F8), Color(0xFF8A3FFC), Color(0xFF9E27E8))
                                    )
                                )
                                else Modifier.background(Color(0xFF262626))
                            )
                            .combinedClickable(
                                onClick = onLongClickMessage,
                                onLongClick = onLongClickMessage
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play voice message",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ılılıllı|lıl ${message.audioDuration ?: "0:04"}",
                            color = Color.White,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Default
                        )
                    }
                }

                else -> { // "TEXT"
                    val bubbleTheme = try {
                        BubbleTheme.valueOf(message.theme)
                    } catch (e: Exception) {
                        BubbleTheme.CLASSIC
                    }

                    if (bubbleTheme != BubbleTheme.CLASSIC) {
                        // Render aesthetic overlay theme (Obsidian Ruby Hearts, Midnight Butterfly, Neon Cyber, Golden Luxe)
                        Box(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = onLongClickMessage,
                                    onLongClick = onLongClickMessage
                                )
                                .testTag("chat_bubble_${message.id}")
                        ) {
                            AestheticLyricsBubble(
                                text = message.text,
                                theme = bubbleTheme,
                                isFromMe = isMe
                            )
                        }
                    } else {
                        // Classic Clean Instagram Bubble
                        Box(
                            modifier = Modifier
                                .widthIn(max = 280.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 18.dp,
                                        topEnd = 18.dp,
                                        bottomStart = if (isMe) 18.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 18.dp
                                    )
                                )
                                .then(
                                    if (isMe) Modifier.background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFF3870F8),
                                                Color(0xFF7A3FE4),
                                                Color(0xFFB832B0),
                                                Color(0xFFE024A8)
                                            )
                                        )
                                    )
                                    else Modifier.background(Color(0xFF262626))
                                )
                                .combinedClickable(
                                    onClick = onLongClickMessage,
                                    onLongClick = onLongClickMessage
                                )
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("chat_bubble_${message.id}")
                        ) {
                            FormattedEmojiText(
                                text = message.text,
                                color = Color.White,
                                fontSize = 14.5.sp,
                                lineHeight = 19.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Authentic Instagram 3-Dot Animated Bouncing Typing Indicator Bubble:
 * Displays pulsing dots inside a native bubble next to the contact avatar.
 */
@Composable
fun TypingIndicatorBubble(
    avatarName: String,
    senderName: String,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "typing_dots_animation")

    val dot1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, delayMillis = 120, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 350, delayMillis = 240, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 54.dp, top = 2.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        RoundAvatar(
            avatarName = avatarName,
            contentDescription = senderName,
            size = 28.dp,
            onClick = onAvatarClick,
            modifier = Modifier.padding(end = 8.dp, bottom = 2.dp)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp))
                .background(Color(0xFF262626))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = dot1Offset.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8E8E93))
                )
                Box(
                    modifier = Modifier
                        .offset(y = dot2Offset.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8E8E93))
                )
                Box(
                    modifier = Modifier
                        .offset(y = dot3Offset.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8E8E93))
                )
            }
        }
    }
}
