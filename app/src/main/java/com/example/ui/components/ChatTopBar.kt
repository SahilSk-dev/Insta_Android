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
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    onDetailsClick: () -> Unit,
    onOpenBackend: () -> Unit,
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
                    // Mini avatar with 8-sided rounded badge
                    Image(
                        painter = painterResource(id = getAvatarResId(avatarName)),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(OctagonBadgeShape)
                            .testTag("top_avatar")
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = handle,
                            color = InstagramSubtext,
                            fontSize = 12.sp,
                            maxLines = 1,
                            lineHeight = 14.sp
                        )
                    }
                }

                // Smiley speech bubble icon - Tapping this opens the Backend Settings!
                IconButton(
                    onClick = onOpenBackend,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("top_reactions_button")
                ) {
                    InstagramSmileyBubbleIcon(
                        tint = Color.White,
                        size = 23.dp
                    )
                }

                // Video call icon
                IconButton(
                    onClick = onVideoCallClick,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("top_video_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Videocam,
                        contentDescription = "Video Call",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Tag / Details icon - also opens settings or details
                IconButton(
                    onClick = onDetailsClick,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("top_tag_details_button")
                ) {
                    InstagramTagIcon(
                        tint = Color.White,
                        size = 23.dp
                    )
                }
            }
        }
    }
}
