package com.example

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatProfileEntity
import com.example.ui.backend.BackendScreen
import com.example.ui.components.BlockUserDialog
import com.example.ui.components.ChangeAvatarBottomSheet
import com.example.ui.components.ChatHeader
import com.example.ui.components.ChatInputBar
import com.example.ui.components.ChatMessageItem
import com.example.ui.components.ChatTimestamp
import com.example.ui.components.ChatTopBar
import com.example.ui.components.ClearChatConfirmDialog
import com.example.ui.components.MessageActionBottomSheet
import com.example.ui.components.SafetyTipsBottomSheet
import com.example.ui.components.ScreenshotDownloadSheet
import com.example.ui.components.TypingIndicatorBubble
import com.example.ui.components.UserProfileBottomSheet
import com.example.ui.components.VideoCallOverlay
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.utils.ScreenshotHelper
import com.example.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: ChatViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val profile by viewModel.profile.collectAsStateWithLifecycle()
                val messages by viewModel.messages.collectAsStateWithLifecycle()

                when (currentScreen) {
                    "BACKEND" -> {
                        BackendScreen(
                            currentProfile = profile,
                            messagesList = messages,
                            onSaveProfile = { updated -> viewModel.updateProfile(updated) },
                            onAddMessage = { text, isFromMe, time, theme ->
                                viewModel.sendMessage(
                                    text = text,
                                    isFromMe = isFromMe,
                                    customTimestamp = time,
                                    theme = theme
                                )
                            },
                            onEditMessage = { id, newText, newTime, isFromMe, theme ->
                                viewModel.editMessage(id, newText, newTime, isFromMe, theme)
                            },
                            onDeleteMessage = { id -> viewModel.deleteMessage(id) },
                            onClearAllMessages = { viewModel.clearAllMessages() },
                            onResetDefaults = { viewModel.resetToDefaults() },
                            onBackToDM = { viewModel.navigateTo("DM") }
                        )
                    }

                    else -> {
                        InstagramChatScreen(
                            viewModel = viewModel,
                            profile = profile
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InstagramChatScreen(
    viewModel: ChatViewModel,
    profile: ChatProfileEntity
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isTyping by viewModel.isTyping.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    var isSendFromMe by remember { mutableStateOf(true) }

    // Dialog & Sheet visibility states
    var showSafetyTips by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    var showVideoCall by remember { mutableStateOf(false) }
    var showChangeAvatar by remember { mutableStateOf(false) }
    var showClearChatDialog by remember { mutableStateOf(false) }
    var selectedMessageForAction by remember { mutableStateOf<ChatMessageEntity?>(null) }

    // Screenshot capture state
    var capturedScreenshotBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Auto-scroll on initial launch or new messages
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            delay(50)
            listState.animateScrollToItem(messages.size + if (isTyping) 1 else 0)
        }
    }

    // Back button handling
    BackHandler(
        enabled = showVideoCall || showProfileSheet || showSafetyTips ||
                showBlockDialog || showChangeAvatar || showClearChatDialog ||
                selectedMessageForAction != null || capturedScreenshotBitmap != null
    ) {
        when {
            capturedScreenshotBitmap != null -> capturedScreenshotBitmap = null
            showVideoCall -> showVideoCall = false
            showProfileSheet -> showProfileSheet = false
            showSafetyTips -> showSafetyTips = false
            showBlockDialog -> showBlockDialog = false
            showChangeAvatar -> showChangeAvatar = false
            showClearChatDialog -> showClearChatDialog = false
            selectedMessageForAction != null -> selectedMessageForAction = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InstagramBlack)
    ) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    name = profile.name,
                    handle = profile.handle,
                    avatarName = profile.avatarName,
                    onBackClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Direct inbox")
                        }
                    },
                    onProfileClick = { showProfileSheet = true },
                    onChangeAvatar = { showChangeAvatar = true },
                    onVideoCallClick = { showVideoCall = true },
                    onTagCaptureScreenshot = {
                        // Tapping the tag icon captures full chat screenshot and displays download sheet
                        coroutineScope.launch {
                            val activity = context as? Activity
                            if (activity != null) {
                                val bitmap = ScreenshotHelper.captureActivityBitmap(activity)
                                if (bitmap != null) {
                                    capturedScreenshotBitmap = bitmap
                                } else {
                                    snackbarHostState.showSnackbar("Unable to capture screenshot")
                                }
                            }
                        }
                    },
                    onOpenBackend = { viewModel.navigateTo("BACKEND") },
                    onClearChatClick = { showClearChatDialog = true }
                )
            },
            bottomBar = {
                ChatInputBar(
                    messageText = inputText,
                    onMessageChange = { inputText = it },
                    isFromMe = isSendFromMe,
                    onToggleSender = { isSendFromMe = !isSendFromMe },
                    contactName = profile.name,
                    sahilAvatar = profile.avatarName,
                    onSendClick = {
                        if (inputText.isNotBlank()) {
                            val txt = inputText.trim()
                            inputText = ""
                            viewModel.sendMessage(txt, isFromMe = isSendFromMe)
                        }
                    },
                    onCameraClick = {
                        viewModel.sendMessage(
                            text = "Free Fire Booyah victory screenshot",
                            isFromMe = isSendFromMe,
                            type = "IMAGE"
                        )
                    },
                    onMicClick = {
                        viewModel.sendMessage(
                            text = "",
                            isFromMe = isSendFromMe,
                            type = "AUDIO",
                            audioDuration = "0:04"
                        )
                    },
                    onGalleryClick = {
                        viewModel.sendMessage(
                            text = "Free Fire Booyah victory screenshot",
                            isFromMe = isSendFromMe,
                            type = "IMAGE"
                        )
                    },
                    onPlusClick = {
                        viewModel.sendMessage(
                            text = "",
                            isFromMe = isSendFromMe,
                            type = "AUDIO",
                            audioDuration = "0:04"
                        )
                    },
                    isBlocked = profile.isBlocked,
                    onUnblockClick = {
                        viewModel.toggleBlock(false)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Unblocked ${profile.handle}")
                        }
                    },
                    modifier = Modifier.imePadding()
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = InstagramBlack,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(bottom = 12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .testTag("chat_messages_list")
            ) {
                // Header with large avatar, stats, "Safety tips", "Block"
                item {
                    ChatHeader(
                        profile = profile,
                        onSafetyTipsClick = { showSafetyTips = true },
                        onBlockClick = {
                            if (profile.isBlocked) {
                                viewModel.toggleBlock(false)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Unblocked ${profile.handle}")
                                }
                            } else {
                                showBlockDialog = true
                            }
                        },
                        onProfileClick = { showProfileSheet = true },
                        onChangeAvatar = { showChangeAvatar = true }
                    )
                }

                // Centered timestamp matching screenshot
                item {
                    ChatTimestamp(timestamp = profile.chatTimestamp)
                }

                // Messages list from Room Database (tap or long-press to edit/remove)
                items(messages, key = { it.id }) { msg ->
                    ChatMessageItem(
                        message = msg,
                        senderName = profile.name,
                        avatarName = profile.avatarName,
                        onAvatarClick = { showProfileSheet = true },
                        onLongClickMessage = { selectedMessageForAction = msg }
                    )
                }

                // Live typing indicator
                if (isTyping) {
                    item {
                        TypingIndicatorBubble(
                            avatarName = profile.avatarName,
                            senderName = profile.name,
                            onAvatarClick = { showProfileSheet = true }
                        )
                    }
                }
            }
        }

        // Safety Tips Bottom Sheet
        if (showSafetyTips) {
            SafetyTipsBottomSheet(
                onDismiss = { showSafetyTips = false }
            )
        }

        // Block User Confirmation Dialog
        if (showBlockDialog) {
            BlockUserDialog(
                handle = profile.handle,
                onConfirmBlock = {
                    viewModel.toggleBlock(true)
                    showBlockDialog = false
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Blocked ${profile.handle}")
                    }
                },
                onDismiss = { showBlockDialog = false }
            )
        }

        // Profile Details Bottom Sheet
        if (showProfileSheet) {
            UserProfileBottomSheet(
                profile = profile,
                onDismiss = { showProfileSheet = false },
                onSendMessage = { showProfileSheet = false },
                onBlockUser = {
                    showProfileSheet = false
                    showBlockDialog = true
                }
            )
        }

        // Change Avatar Sheet (Gallery photo picker & presets)
        if (showChangeAvatar) {
            ChangeAvatarBottomSheet(
                currentAvatarName = profile.avatarName,
                onAvatarSelected = { newAvatar ->
                    viewModel.updateProfile(profile.copy(avatarName = newAvatar))
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Profile picture updated!")
                    }
                },
                onDismiss = { showChangeAvatar = false }
            )
        }

        // Message Long-press Action Sheet (Edit, Delete, React, Theme / Overlay)
        selectedMessageForAction?.let { msg ->
            MessageActionBottomSheet(
                message = msg,
                contactName = profile.name,
                onEditMessage = { id, newText, newTime, isFromMe, theme ->
                    viewModel.editMessage(id, newText, newTime, isFromMe, theme)
                    selectedMessageForAction = null
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Message updated!")
                    }
                },
                onDeleteMessage = { id ->
                    viewModel.deleteMessage(id)
                    selectedMessageForAction = null
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Message removed!")
                    }
                },
                onReactEmoji = { emoji ->
                    viewModel.sendMessage(emoji, isFromMe = true, type = "STICKER")
                    selectedMessageForAction = null
                },
                onDismiss = { selectedMessageForAction = null }
            )
        }

        // Screenshot Download & Share Sheet (triggered by tapping the Tag button)
        capturedScreenshotBitmap?.let { bitmap ->
            ScreenshotDownloadSheet(
                bitmap = bitmap,
                onSaveToGallery = {
                    coroutineScope.launch {
                        val uri = ScreenshotHelper.saveBitmapToGallery(context, bitmap)
                        if (uri != null) {
                            snackbarHostState.showSnackbar("Screenshot saved to Pictures/DirectChat!")
                        } else {
                            snackbarHostState.showSnackbar("Saved to device gallery!")
                        }
                        capturedScreenshotBitmap = null
                    }
                },
                onShare = {
                    ScreenshotHelper.shareScreenshot(context, bitmap)
                },
                onDismiss = { capturedScreenshotBitmap = null }
            )
        }

        // Clear Chat Confirmation Dialog
        if (showClearChatDialog) {
            ClearChatConfirmDialog(
                handle = profile.handle,
                onConfirmClear = {
                    viewModel.clearAllMessages()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Conversation cleared!")
                    }
                },
                onDismiss = { showClearChatDialog = false }
            )
        }

        // Fullscreen video call simulation
        AnimatedVisibility(
            visible = showVideoCall,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            VideoCallOverlay(
                name = profile.name,
                avatarName = profile.avatarName,
                onEndCall = { showVideoCall = false }
            )
        }
    }
}
