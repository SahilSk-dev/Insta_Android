package com.example.ui.components

import com.example.R

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
    AvatarOption("sahil_avatar", "Free Fire MAX (Default)", R.drawable.sahil_avatar),
    AvatarOption("avatar_cyber_samurai", "Cyber Ninja", R.drawable.avatar_cyber_samurai),
    AvatarOption("avatar_gold_tiger", "Golden Flame Tiger", R.drawable.avatar_gold_tiger)
)
