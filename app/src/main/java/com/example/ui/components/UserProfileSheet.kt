package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ChatProfileEntity
import com.example.ui.theme.InstagramBlue
import com.example.ui.theme.InstagramDarkSurface
import com.example.ui.theme.InstagramInputBg
import com.example.ui.theme.InstagramSubtext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileBottomSheet(
    profile: ChatProfileEntity,
    onDismiss: () -> Unit,
    onSendMessage: () -> Unit,
    onBlockUser: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isFollowing by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = InstagramDarkSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 36.dp)
        ) {
            // Header handle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = profile.handle,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Profile stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pure Round avatar
                RoundAvatar(
                    avatarName = profile.avatarName,
                    contentDescription = "Profile picture",
                    size = 80.dp
                )

                Spacer(modifier = Modifier.width(20.dp))

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatItem(count = profile.postsCount, label = "posts")
                    ProfileStatItem(count = profile.followersCount, label = "followers")
                    ProfileStatItem(count = profile.followingCount, label = "following")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name and bio
            Text(
                text = profile.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default
            )
            Text(
                text = profile.bio,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = profile.followsYouText,
                color = InstagramSubtext,
                fontSize = 12.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isFollowing = !isFollowing },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing) InstagramInputBg else InstagramBlue
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("profile_follow_button")
                ) {
                    Text(
                        text = if (isFollowing) "Following" else "Follow back",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default
                    )
                }

                Button(
                    onClick = {
                        onDismiss()
                        onSendMessage()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InstagramInputBg),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("profile_message_button")
                ) {
                    Text(
                        text = "Message",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Posts tab indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GridOn,
                    contentDescription = "Grid",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post grid showing 1 post
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(InstagramInputBg)
                        .testTag("profile_single_post")
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.gaming_post),
                        contentDescription = "Free Fire Booyah victory post",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(
    count: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontFamily = FontFamily.Default
        )
    }
}
