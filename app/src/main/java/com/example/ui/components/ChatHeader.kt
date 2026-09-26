package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatProfileEntity
import com.example.ui.theme.InstagramSubtext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatHeader(
    profile: ChatProfileEntity,
    onSafetyTipsClick: () -> Unit,
    onBlockClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChangeAvatar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large 8-sided badge avatar (Tap to view profile, Long-press to change avatar)
        Image(
            painter = painterResource(id = getAvatarResId(profile.avatarName)),
            contentDescription = profile.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(92.dp)
                .clip(OctagonBadgeShape)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 46.dp),
                    onClick = onProfileClick,
                    onLongClick = onChangeAvatar
                )
                .testTag("chat_header_avatar")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Name
        Text(
            text = profile.name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Username and Joined Date
        Text(
            text = "${profile.handle} · ${profile.joinedDate}",
            color = InstagramSubtext,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Followers & Posts
        Text(
            text = "${profile.followersCount} followers · ${profile.postsCount} post",
            color = InstagramSubtext,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Follows you
        Text(
            text = profile.followsYouText,
            color = InstagramSubtext,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Mutual followers
        Text(
            text = profile.mutualFollowText,
            color = InstagramSubtext,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons: Safety tips & Block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Safety tips
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = 32.dp),
                        onClick = onSafetyTipsClick
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .testTag("action_safety_tips"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShieldHeartIcon(
                    tint = Color.White,
                    size = 30.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.safety_tips),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.width(28.dp))

            // Block / Unblock
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = 32.dp),
                        onClick = onBlockClick
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .testTag("action_block"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BlockSlashIcon(
                    tint = Color.White,
                    size = 30.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (profile.isBlocked) stringResource(id = R.string.unblock) else stringResource(id = R.string.block),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
