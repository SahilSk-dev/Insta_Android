package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatProfileEntity
import com.example.data.repository.ChatRepository
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
        "Free Fire MAX me rank push karoge aaj?",
        "1v1 custom room challenge accepted! 🎮🔥",
        "Aaj Booyah confirm hai bro!",
        "Haan bolo bhai, sab theek?",
        "Squad full hone wala hai, jaldi aao!",
        "Headshot sensitivity settings share karu kya? 🎯"
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

        // Seed initial data if empty
        viewModelScope.launch {
            repository.profile.collect { p ->
                if (p == null) {
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
        customTimestamp: String? = null
    ) {
        val currentProfile = profile.value
        if (currentProfile.isBlocked && isFromMe) return

        val time = customTimestamp ?: getCurrentTimeString()
        val msg = ChatMessageEntity(
            text = text,
            isFromMe = isFromMe,
            timestamp = time,
            type = type,
            imageResName = imageResName,
            audioDuration = audioDuration,
            orderIndex = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.addMessage(msg)

            if (isFromMe && currentProfile.autoReplyEnabled) {
                delay(800)
                _isTyping.value = true
                delay(1400)
                _isTyping.value = false

                val replyText = sahilResponses[responseIndex % sahilResponses.size]
                responseIndex++
                val replyMsg = ChatMessageEntity(
                    text = replyText,
                    isFromMe = false,
                    timestamp = getCurrentTimeString(),
                    type = "TEXT",
                    orderIndex = System.currentTimeMillis()
                )
                repository.addMessage(replyMsg)
            }
        }
    }

    fun editMessage(id: String, newText: String, newTimestamp: String, isFromMe: Boolean) {
        viewModelScope.launch {
            val existing = messages.value.find { it.id == id }
            if (existing != null) {
                repository.updateMessage(
                    existing.copy(
                        text = newText,
                        timestamp = newTimestamp,
                        isFromMe = isFromMe
                    )
                )
            }
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
