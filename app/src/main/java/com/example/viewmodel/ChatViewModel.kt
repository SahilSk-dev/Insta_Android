package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatProfileEntity
import com.example.data.repository.ChatRepository
import com.example.ui.components.EmojiHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChatRepository
    val profile: StateFlow<ChatProfileEntity>
    val messages: StateFlow<List<ChatMessageEntity>>

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    private val _currentScreen = MutableStateFlow("DM") // "DM" or "BACKEND"
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val sahilResponses = listOf(
        "Arey bhai! Kaise ho?",
        "Free Fire MAX me rank push karoge aaj? 🔥",
        "1v1 custom room challenge accepted! 🎮👑",
        "Aaj Booyah confirm hai bro! 💯",
        "Haan bolo bhai, sab theek? 😎",
        "Squad full hone wala hai, jaldi aao! 🚀",
        "Headshot sensitivity settings share karu kya? 🎯⚡"
    )
    private var responseIndex = 0

    init {
        val db = AppDatabase.getInstance(application)
        repository = ChatRepository(db.chatDao())

        profile = repository.profile
            .map { it ?: ChatProfileEntity() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ChatProfileEntity()
            )

        messages = repository.messages
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = listOf(
                    ChatMessageEntity(
                        id = "init_1",
                        text = "Hello",
                        isFromMe = true,
                        timestamp = "12:41 PM",
                        type = "TEXT",
                        orderIndex = 1L
                    )
                )
            )

        // Seed initial data if empty or migrate
        viewModelScope.launch {
            repository.profile.collect { p ->
                if (p == null) {
                    repository.resetToDefault()
                } else if (p.name.isBlank() || p.name.contains("🦋")) {
                    repository.saveProfile(
                        p.copy(
                            name = "Sahil",
                            handle = if (p.handle.isBlank() || p.handle == "md.sahil_sk_") "not__ur__sahil_77" else p.handle,
                            autoReplyEnabled = false
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            repository.messages.collect { msgList ->
                if (msgList.isEmpty() || (msgList.size == 1 && msgList.first().id == "init_1")) {
                    repository.resetToDefault()
                }
            }
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun updateProfile(newProfile: ChatProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(newProfile)
        }
    }

    fun toggleBlock(blocked: Boolean) {
        viewModelScope.launch {
            val current = profile.value
            repository.saveProfile(current.copy(isBlocked = blocked))
        }
    }

    fun sendMessage(
        text: String,
        isFromMe: Boolean = true,
        type: String = "TEXT",
        imageResName: String? = null,
        audioDuration: String? = null,
        theme: String = "CLASSIC",
        customTimestamp: String? = null
    ) {
        val currentProfile = profile.value
        if (currentProfile.isBlocked && isFromMe) return

        val time = customTimestamp ?: getCurrentTimeString()
        val isPureEmoji = EmojiHelper.isPureEmojiString(text.trim())
        val messageType = if (isPureEmoji && type == "TEXT") "STICKER" else type

        viewModelScope.launch {
            val msg = ChatMessageEntity(
                text = text,
                isFromMe = isFromMe,
                timestamp = time,
                type = messageType,
                imageResName = imageResName,
                audioDuration = audioDuration,
                theme = theme,
                orderIndex = System.currentTimeMillis()
            )

            repository.addMessage(msg)
        }
    }

    fun editMessage(id: String, newText: String, newTimestamp: String, isFromMe: Boolean, theme: String = "CLASSIC") {
        viewModelScope.launch {
            val existing = messages.value.find { it.id == id }
            if (existing != null) {
                repository.updateMessage(
                    existing.copy(
                        text = newText,
                        timestamp = newTimestamp,
                        isFromMe = isFromMe,
                        theme = theme
                    )
                )
            }
        }
    }

    fun clearAllMessages() {
        viewModelScope.launch {
            repository.clearAllMessages()
        }
    }

    fun deleteMessage(id: String) {
        viewModelScope.launch {
            repository.deleteMessage(id)
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            repository.resetToDefault()
        }
    }

    private fun getCurrentTimeString(): String {
        return try {
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        } catch (e: Exception) {
            "12:42 PM"
        }
    }
}
