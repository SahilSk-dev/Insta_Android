package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import java.io.File

fun getAvatarResId(avatarName: String?): Int {
    return when (avatarName) {
        "avatar_cyber_samurai" -> R.drawable.avatar_cyber_samurai
        "avatar_gold_tiger" -> R.drawable.avatar_gold_tiger
        else -> R.drawable.sahil_avatar
    }
}

data class AvatarOption(
    val id: String,
    val name: String,
    val resId: Int
)

val availableAvatars = listOf(
    AvatarOption("sahil_avatar", "Default Avatar", R.drawable.sahil_avatar),
    AvatarOption("avatar_cyber_samurai", "Cyber Ninja", R.drawable.avatar_cyber_samurai),
    AvatarOption("avatar_gold_tiger", "Golden Flame Tiger", R.drawable.avatar_gold_tiger)
)

/**
 * Universal Circle Profile Picture Composable that handles both custom uploaded image files/URIs
 * and built-in preset drawable avatars with pure round styling.
 */
@Composable
fun RoundAvatar(
    avatarName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    borderColor: Color? = null,
    borderWidth: Dp = 2.dp,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val isCustomUri = avatarName.startsWith("content://") ||
            avatarName.startsWith("file://") ||
            avatarName.startsWith("/")

    val borderModifier = if (borderColor != null) {
        Modifier.border(borderWidth, borderColor, CircleShape)
    } else Modifier

    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier

    Box(
        modifier = modifier
            .size(size)
            .then(borderModifier)
            .clip(CircleShape)
            .then(clickModifier)
            .background(Color(0xFF262626)),
        contentAlignment = Alignment.Center
    ) {
        if (isCustomUri) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(if (avatarName.startsWith("/")) File(avatarName) else avatarName)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = getAvatarResId(avatarName)),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
