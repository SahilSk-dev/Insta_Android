package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatProfileEntity
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val chatDao: ChatDao) {

    val profile: Flow<ChatProfileEntity?> = chatDao.getProfile()
    val messages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    suspend fun saveProfile(profile: ChatProfileEntity) {
        chatDao.insertOrUpdateProfile(profile)
    }

    suspend fun addMessage(message: ChatMessageEntity) {
        chatDao.insertMessage(message)
    }

    suspend fun updateMessage(message: ChatMessageEntity) {
        chatDao.updateMessage(message)
    }

    suspend fun clearAllMessages() {
        chatDao.clearAllMessages()
    }

    suspend fun deleteMessage(id: String) {
        chatDao.deleteMessageById(id)
    }

    suspend fun resetToDefault() {
        chatDao.clearAllMessages()
        chatDao.insertOrUpdateProfile(
            ChatProfileEntity(
                id = 1,
                name = "Sahil",
                handle = "not__ur__sahil_77",
                joinedDate = "Joined Oct 2025",
                followersCount = "108",
                postsCount = "1",
                followingCount = "142",
                followsYouText = "Follows you",
                mutualFollowText = "You both follow __broken__heart__019",
                bio = "🎮 Free Fire MAX Esports Player 🔥\n⚡ Headshot machine | 1v1 Room Challenge\n🏆 Guild Leader #Booyah",
                chatTimestamp = "12:41 PM",
                isBlocked = false,
                autoReplyEnabled = false
            )
        )
        val defaultMessages = listOf(
            ChatMessageEntity(
                id = "msg-1",
                text = "Hello Sahil bhai! Kemon acho? Free Fire MAX khelbe aaj?",
                isFromMe = true,
                timestamp = "12:41 PM",
                type = "TEXT",
                theme = "CLASSIC",
                orderIndex = 1L
            ),
            ChatMessageEntity(
                id = "msg-2",
                text = "Arey bhai! Ekdom bhalo achi. Aajke rank push korbo, squad ready ache! 🔥🎮",
                isFromMe = false,
                timestamp = "12:41 PM",
                type = "TEXT",
                theme = "OBSIDIAN_HEART",
                orderIndex = 2L
            ),
            ChatMessageEntity(
                id = "msg-3",
                text = "Free Fire Booyah victory screenshot",
                isFromMe = false,
                timestamp = "12:42 PM",
                type = "IMAGE",
                imageResName = "gaming_post",
                theme = "CLASSIC",
                orderIndex = 3L
            ),
            ChatMessageEntity(
                id = "msg-4",
                text = "",
                isFromMe = false,
                timestamp = "12:42 PM",
                type = "AUDIO",
                audioDuration = "0:04",
                theme = "CLASSIC",
                orderIndex = 4L
            ),
            ChatMessageEntity(
                id = "msg-5",
                text = "Tumi amar moner majhe ekla projapoti 🦋✨",
                isFromMe = true,
                timestamp = "12:43 PM",
                type = "TEXT",
                theme = "MIDNIGHT_BUTTERFLY",
                orderIndex = 5L
            ),
            ChatMessageEntity(
                id = "msg-6",
                text = "🔥",
                isFromMe = false,
                timestamp = "12:43 PM",
                type = "STICKER",
                theme = "CLASSIC",
                orderIndex = 6L
            )
        )
        defaultMessages.forEach { chatDao.insertMessage(it) }
    }
}
