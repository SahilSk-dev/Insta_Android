package com.example

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.components.MessageActionBottomSheet
import com.example.ui.components.SafetyTipsBottomSheet
import com.example.ui.components.StickerPickerSheet
import com.example.ui.components.UserProfileBottomSheet
import com.example.ui.components.VideoCallOverlay
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.InstagramBubbleReceived
import com.example.ui.theme.InstagramPlaceholder
import com.example.ui.theme.MyApplicationTheme
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
                            onAddMessage = { text, isFromMe, time ->
                                viewModel.sendMessage(
                                    text = text,
                                    isFromMe = isFromMe,
                                    customTimestamp = time
                                )
                            },
                            onEditMessage = { id, newText, newTime, isFromMe ->
                                viewModel.editMessage(id, newText, newTime, isFromMe)
                            },
                            onDeleteMessage = { id -> viewModel.deleteMessage(id) },
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
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isTyping by viewModel.isTyping.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }

    // Dialog & Sheet visibility states
    var showSafetyTips by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }
    var showProfileSheet by remember { mutableStateOf(false) }
    var showStickerSheet by remember { mutableStateOf(false) }
    var showVideoCall by remember { mutableStateOf(false) }
    var showChangeAvatar by remember { mutableStateOf(false) }
    var selectedMessageForAction by remember { mutableStateOf<ChatMessageEntity?>(null) }

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
                showStickerSheet || showBlockDialog || showChangeAvatar ||
                selectedMessageForAction != null
    ) {
        when {
            showVideoCall -> showVideoCall = false
            showProfileSheet -> showProfileSheet = false
            showSafetyTips -> showSafetyTips = false
            showStickerSheet -> showStickerSheet = false
            showBlockDialog -> showBlockDialog = false
            showChangeAvatar -> showChangeAvatar = false
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
                    onDetailsClick = { viewModel.navigateTo("BACKEND") },
                    onOpenBackend = { viewModel.navigateTo("BACKEND") }
                )
            },
            bottomBar = {
                ChatInputBar(
                    messageText = inputText,
                    onMessageChange = { inputText = it },
                    onSendClick = {
                        if (inputText.isNotBlank()) {
                            val txt = inputText.trim()
                            inputText = ""
                            viewModel.sendMessage(txt, isFromMe = true)
                        }
                    },
                    onCameraClick = {
                        viewModel.sendMessage(
                            text = "Free Fire Booyah victory screenshot",
                            isFromMe = true,
                            type = "IMAGE"
                        )
                    },
                    onMicClick = {
                        viewModel.sendMessage(
                            text = "",
                            isFromMe = true,
                            type = "AUDIO",
                            audioDuration = "0:04"
                        )
                    },
                    onGalleryClick = {
                        viewModel.sendMessage(
                            text = "Free Fire Booyah victory screenshot",
                            isFromMe = true,
                            type = "IMAGE"
                        )
                    },
                    onStickersClick = { showStickerSheet = true },
                    onPlusClick = { showStickerSheet = true },
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(InstagramBubbleReceived)
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${profile.name} is typing…",
                                    color = InstagramPlaceholder,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Sub-sheets and Modals
        if (showSafetyTips) {
            SafetyTipsBottomSheet(
                onDismiss = { showSafetyTips = false }
            )
        }

        if (showBlockDialog) {
            BlockUserDialog(
                handle = profile.handle,
                onConfirmBlock = {
                    showBlockDialog = false
                    viewModel.toggleBlock(true)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Blocked ${profile.handle}")
                    }
                },
                onDismiss = { showBlockDialog = false }
            )
        }

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

        if (showStickerSheet) {
            StickerPickerSheet(
                onStickerSelected = { sticker ->
                    viewModel.sendMessage(sticker, isFromMe = true, type = "STICKER")
                },
                onDismiss = { showStickerSheet = false }
            )
        }

        // Change Profile Picture bottom sheet (opened by long-pressing avatar)
        if (showChangeAvatar) {
            ChangeAvatarBottomSheet(
                currentAvatarName = profile.avatarName,
                onAvatarSelected = { newAvatar ->
                    viewModel.updateProfile(profile.copy(avatarName = newAvatar))
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Profile picture changed!")
                    }
                },
                onDismiss = { showChangeAvatar = false }
            )
        }

        // Message Long-press Action Sheet (Edit, Delete, React)
        selectedMessageForAction?.let { msg ->
            MessageActionBottomSheet(
                message = msg,
                onEditMessage = { id, newText, newTime, isFromMe ->
                    viewModel.editMessage(id, newText, newTime, isFromMe)
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
