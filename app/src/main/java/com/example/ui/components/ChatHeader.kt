package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatProfileEntity
import com.example.ui.theme.InstagramSubtext

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
        // Pure Round Avatar (Tap to view profile, Long-press to upload / change custom DP)
        RoundAvatar(
            avatarName = profile.avatarName,
            contentDescription = profile.name,
            size = 96.dp,
            onClick = onChangeAvatar,
            modifier = Modifier.testTag("chat_header_avatar")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Name
        Text(
            text = profile.name,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Username and Instagram branding
        Text(
            text = "${profile.handle} · Instagram",
            color = InstagramSubtext,
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Followers & Posts
        Text(
            text = "${profile.followersCount} followers · ${profile.postsCount} post",
            color = InstagramSubtext,
            fontSize = 13.5.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Mutual followers
        Text(
            text = profile.mutualFollowText,
            color = InstagramSubtext,
            fontSize = 13.5.sp,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Native Instagram Android: View profile button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF262626))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onProfileClick
                )
                .padding(horizontal = 16.dp, vertical = 7.dp)
                .testTag("action_view_profile"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "View profile",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Default
            )
        }
    }
}
