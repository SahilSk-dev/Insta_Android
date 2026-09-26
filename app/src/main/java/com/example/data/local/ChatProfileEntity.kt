package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_profile")
data class ChatProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Sahil Sk",
    val handle: String = "md.sahil_sk_",
    val joinedDate: String = "Joined Oct 2025",
    val followersCount: String = "108",
    val postsCount: String = "1",
    val followingCount: String = "142",
    val followsYouText: String = "Follows you",
    val mutualFollowText: String = "You both follow __broken__heart__019",
    val bio: String = "🎮 Free Fire MAX Esports Player 🔥\n⚡ Headshot machine | 1v1 Room Challenge\n🏆 Guild Leader #Booyah",
    val chatTimestamp: String = "12:41 PM",
    val isBlocked: Boolean = false,
    val autoReplyEnabled: Boolean = true,
    val avatarName: String = "sahil_avatar"
)
