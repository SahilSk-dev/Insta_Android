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

    suspend fun deleteMessage(id: String) {
        chatDao.deleteMessageById(id)
    }

    suspend fun resetToDefault() {
        chatDao.clearAllMessages()
        chatDao.insertOrUpdateProfile(
            ChatProfileEntity(
                id = 1,
                name = "Sahil Sk",
                handle = "md.sahil_sk_",
                joinedDate = "Joined Oct 2025",
                followersCount = "108",
                postsCount = "1",
                followingCount = "142",
                followsYouText = "Follows you",
                mutualFollowText = "You both follow __broken__heart__019",
                bio = "🎮 Free Fire MAX Esports Player 🔥\n⚡ Headshot machine | 1v1 Room Challenge\n🏆 Guild Leader #Booyah",
                chatTimestamp = "12:41 PM",
                isBlocked = false,
                autoReplyEnabled = true
            )
        )
        chatDao.insertMessage(
            ChatMessageEntity(
                id = "init_1",
                text = "Hello",
                isFromMe = true,
                timestamp = "12:41 PM",
                type = "TEXT",
                orderIndex = 1L
            )
        )
    }
}
