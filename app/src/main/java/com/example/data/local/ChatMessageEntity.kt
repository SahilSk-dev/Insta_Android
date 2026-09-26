package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "",
    val isFromMe: Boolean = true,
    val timestamp: String = "12:41 PM",
    val type: String = "TEXT", // "TEXT", "IMAGE", "AUDIO", "STICKER"
    val imageResName: String? = null,
    val audioDuration: String? = null,
    val orderIndex: Long = System.currentTimeMillis()
)
