package com.example.model

enum class MessageType {
    TEXT,
    IMAGE,
    AUDIO,
    STICKER
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String = "",
    val isFromMe: Boolean = true,
    val timestamp: String = "12:41 PM",
    val type: MessageType = MessageType.TEXT,
    val imageResId: Int? = null,
    val audioDuration: String? = null,
    val isSeen: Boolean = true
)
