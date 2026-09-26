package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.SentimentSatisfiedAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.InstagramBlue
import com.example.ui.theme.InstagramInputBg
import com.example.ui.theme.InstagramPlaceholder

@Composable
fun ChatInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onCameraClick: () -> Unit,
    onMicClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onStickersClick: () -> Unit,
    onPlusClick: () -> Unit,
    isBlocked: Boolean = false,
    onUnblockClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(InstagramBlack)
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (isBlocked) {
            // Blocked state bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(CircleShape)
                    .background(InstagramInputBg)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.blocked_message),
                    color = InstagramPlaceholder,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(id = R.string.unblock),
                    color = InstagramBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = onUnblockClick)
                        .padding(8.dp)
                        .testTag("unblock_button_bar")
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blue circular camera button
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(InstagramBlue)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onCameraClick
                        )
                        .testTag("chat_camera_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Camera",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Pill container for text input and actions
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(CircleShape)
                        .background(InstagramInputBg)
                        .padding(start = 16.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Input TextField
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (messageText.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.message_placeholder),
                                color = InstagramPlaceholder,
                                fontSize = 15.sp
                            )
                        }

                        BasicTextField(
                            value = messageText,
                            onValueChange = onMessageChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 15.sp
                            ),
                            cursorBrush = SolidColor(InstagramBlue),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = { onSendClick() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("chat_input_field")
                        )
                    }

                    if (messageText.isNotBlank()) {
                        // "Send" action button when text exists
                        Text(
                            text = stringResource(id = R.string.send),
                            color = InstagramBlue,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable(onClick = onSendClick)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("chat_send_button")
                        )
                    } else {
                        // When text is empty, display: Mic, Gallery, Sticker, Plus
                        IconButton(
                            onClick = onMicClick,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("chat_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Mic,
                                contentDescription = "Voice note",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        IconButton(
                            onClick = onGalleryClick,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("chat_gallery_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Image,
                                contentDescription = "Gallery",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        IconButton(
                            onClick = onStickersClick,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("chat_sticker_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SentimentSatisfiedAlt,
                                contentDescription = "Stickers",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        IconButton(
                            onClick = onPlusClick,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("chat_plus_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AddCircleOutline,
                                contentDescription = "More actions",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
