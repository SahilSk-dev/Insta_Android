package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatMessageEntity
import com.example.ui.theme.InstagramBubblePurple
import com.example.ui.theme.InstagramBubbleReceived
import com.example.ui.theme.InstagramPlaceholder

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
            fontWeight = FontWeight.Normal
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
            .padding(horizontal = 14.dp, vertical = 3.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isMe) {
            // Received message avatar
            Image(
                painter = painterResource(id = getAvatarResId(avatarName)),
                contentDescription = senderName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 2.dp)
                    .size(28.dp)
                    .clip(OctagonBadgeShape)
                    .clickable(onClick = onAvatarClick)
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
                            .background(InstagramBubbleReceived)
                            .combinedClickable(
                                onClick = {},
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
                    Text(
                        text = message.text,
                        fontSize = 48.sp,
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {},
                                onLongClick = onLongClickMessage
                            )
                            .padding(4.dp)
                    )
                }

                "AUDIO" -> {
                    Row(
                        modifier = Modifier
                            .widthIn(min = 180.dp, max = 240.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isMe) InstagramBubblePurple else InstagramBubbleReceived)
                            .combinedClickable(
                                onClick = {},
                                onLongClick = onLongClickMessage
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play voice message",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "ılılıllı|lıl ${message.audioDuration ?: "0:04"}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                else -> { // "TEXT"
                    Box(
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = if (isMe) 20.dp else 4.dp,
                                    bottomEnd = if (isMe) 4.dp else 20.dp
                                )
                            )
                            .background(if (isMe) InstagramBubblePurple else InstagramBubbleReceived)
                            .combinedClickable(
                                onClick = {},
                                onLongClick = onLongClickMessage
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("chat_bubble_${message.id}")
                    ) {
                        Text(
                            text = message.text,
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
