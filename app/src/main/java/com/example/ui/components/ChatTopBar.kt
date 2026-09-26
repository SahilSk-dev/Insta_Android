package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.InstagramSubtext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatTopBar(
    name: String,
    handle: String,
    avatarName: String,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChangeAvatar: () -> Unit,
    onVideoCallClick: () -> Unit,
    onTagCaptureScreenshot: () -> Unit,
    onOpenBackend: () -> Unit,
    onClearChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(InstagramBlack)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("top_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Profile info (click to view profile, long press to change avatar)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false, radius = 24.dp),
                            onClick = onProfileClick,
                            onLongClick = onChangeAvatar
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pure round mini avatar
                    RoundAvatar(
                        avatarName = avatarName,
                        contentDescription = name,
                        size = 38.dp,
                        modifier = Modifier.testTag("top_avatar")
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default,
                                maxLines = 1,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = InstagramSubtext,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = handle,
                            color = InstagramSubtext,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            maxLines = 1,
                            lineHeight = 14.sp
                        )
                    }
                }

                // 1. Audio Call Icon
                IconButton(
                    onClick = onVideoCallClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("top_audio_call_button")
                ) {
                    InstagramPhoneCallIcon(
                        tint = Color.White,
                        size = 22.dp
                    )
                }

                // 2. Video Call Icon
                IconButton(
                    onClick = onVideoCallClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("top_video_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Videocam,
                        contentDescription = "Video Call",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // 3. Info (i) / Details Icon - Tap: Open Backend, Long-press: Screenshot Dialog
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false, radius = 20.dp),
                            onClick = onOpenBackend,
                            onLongClick = onTagCaptureScreenshot
                        )
                        .testTag("top_info_button"),
                    contentAlignment = Alignment.Center
                ) {
                    InstagramInfoIcon(
                        tint = Color.White,
                        size = 23.dp
                    )
                }
            }
        }
        HorizontalDivider(
            color = Color(0xFF1A1A1A),
            thickness = 0.5.dp
        )
    }
}
