package com.example.ui.backend

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatProfileEntity
import com.example.ui.components.OctagonBadgeShape
import com.example.ui.components.availableAvatars
import com.example.ui.theme.InstagramBlack
import com.example.ui.theme.InstagramBlue
import com.example.ui.theme.InstagramBubblePurple
import com.example.ui.theme.InstagramBubbleReceived
import com.example.ui.theme.InstagramDarkSurface
import com.example.ui.theme.InstagramDivider
import com.example.ui.theme.InstagramInputBg
import com.example.ui.theme.InstagramPlaceholder
import com.example.ui.theme.InstagramRed
import com.example.ui.theme.InstagramSubtext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackendScreen(
    currentProfile: ChatProfileEntity,
    messagesList: List<ChatMessageEntity>,
    onSaveProfile: (ChatProfileEntity) -> Unit,
    onAddMessage: (text: String, isFromMe: Boolean, timestamp: String) -> Unit,
    onEditMessage: (id: String, newText: String, newTimestamp: String, isFromMe: Boolean) -> Unit,
    onDeleteMessage: (id: String) -> Unit,
    onResetDefaults: () -> Unit,
    onBackToDM: () -> Unit
) {
    BackHandler {
        onBackToDM()
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Profile Details, 1: Messages Editor

    // Profile form state
    var name by remember(currentProfile) { mutableStateOf(currentProfile.name) }
    var handle by remember(currentProfile) { mutableStateOf(currentProfile.handle) }
    var joinedDate by remember(currentProfile) { mutableStateOf(currentProfile.joinedDate) }
    var followersCount by remember(currentProfile) { mutableStateOf(currentProfile.followersCount) }
    var postsCount by remember(currentProfile) { mutableStateOf(currentProfile.postsCount) }
    var followingCount by remember(currentProfile) { mutableStateOf(currentProfile.followingCount) }
    var followsYouText by remember(currentProfile) { mutableStateOf(currentProfile.followsYouText) }
    var mutualFollowText by remember(currentProfile) { mutableStateOf(currentProfile.mutualFollowText) }
    var bio by remember(currentProfile) { mutableStateOf(currentProfile.bio) }
    var chatTimestamp by remember(currentProfile) { mutableStateOf(currentProfile.chatTimestamp) }
    var autoReplyEnabled by remember(currentProfile) { mutableStateOf(currentProfile.autoReplyEnabled) }
    var isBlocked by remember(currentProfile) { mutableStateOf(currentProfile.isBlocked) }
    var avatarName by remember(currentProfile) { mutableStateOf(currentProfile.avatarName) }

    // New Message form state
    var newMsgText by remember { mutableStateOf("") }
    var newMsgSenderMe by remember { mutableStateOf(true) }
    var newMsgTimestamp by remember { mutableStateOf("12:42 PM") }

    // Edit Message dialog state
    var editingMessageId by remember { mutableStateOf<String?>(null) }
    var editingMsgText by remember { mutableStateOf("") }
    var editingMsgTimestamp by remember { mutableStateOf("") }
    var editingMsgIsMe by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(InstagramDarkSurface)
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToDM,
                        modifier = Modifier.testTag("backend_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to DM",
                            tint = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DM Backend Editor",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Edit user, followers & chat interface",
                            color = InstagramSubtext,
                            fontSize = 12.sp
                        )
                    }

                    // Reset button
                    IconButton(
                        onClick = {
                            onResetDefaults()
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Reset all data to default template!")
                            }
                        },
                        modifier = Modifier.testTag("backend_reset_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset to default",
                            tint = InstagramSubtext
                        )
                    }

                    // View Live DM button
                    Button(
                        onClick = onBackToDM,
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("backend_view_live_dm_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Live DM", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Tabs: Profile Details | Messages Manager
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = InstagramDarkSurface,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = InstagramBlue,
                            height = 3.dp
                        )
                    },
                    divider = { HorizontalDivider(color = InstagramDivider) }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Profile & Header",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("tab_profile_details")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Chat Messages",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.testTag("tab_messages_manager")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = InstagramBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .navigationBarsPadding()
        ) {
            if (selectedTab == 0) {
                // ==================== TAB 0: USER PROFILE DETAILS ====================
                Text(
                    text = "User Identity & Follower Details",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                BackendTextField(
                    label = "Full Name (Shown in Top Bar & Header)",
                    value = name,
                    onValueChange = { name = it },
                    tag = "input_profile_name"
                )

                BackendTextField(
                    label = "Username / Handle",
                    value = handle,
                    onValueChange = { handle = it },
                    tag = "input_profile_handle"
                )

                BackendTextField(
                    label = "Joined Date",
                    value = joinedDate,
                    onValueChange = { joinedDate = it },
                    tag = "input_profile_joined_date"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        BackendTextField(
                            label = "Followers Count",
                            value = followersCount,
                            onValueChange = { followersCount = it },
                            tag = "input_profile_followers"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BackendTextField(
                            label = "Posts Count",
                            value = postsCount,
                            onValueChange = { postsCount = it },
                            tag = "input_profile_posts"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        BackendTextField(
                            label = "Following Count",
                            value = followingCount,
                            onValueChange = { followingCount = it },
                            tag = "input_profile_following"
                        )
                    }
                }

                BackendTextField(
                    label = "Follow Status Badge Text",
                    value = followsYouText,
                    onValueChange = { followsYouText = it },
                    tag = "input_profile_follows_you"
                )

                BackendTextField(
                    label = "Mutual Follow Text (You both follow...)",
                    value = mutualFollowText,
                    onValueChange = { mutualFollowText = it },
                    tag = "input_profile_mutual"
                )

                BackendTextField(
                    label = "Profile Bio Description",
                    value = bio,
                    onValueChange = { bio = it },
                    singleLine = false,
                    tag = "input_profile_bio"
                )

                BackendTextField(
                    label = "Conversation Centered Timestamp",
                    value = chatTimestamp,
                    onValueChange = { chatTimestamp = it },
                    tag = "input_profile_chat_timestamp"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Profile Picture Preset",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableAvatars.forEach { option ->
                        val isSelected = option.id == avatarName
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { avatarName = option.id }
                                .then(
                                    if (isSelected) Modifier.border(2.dp, InstagramBlue, RoundedCornerShape(10.dp))
                                    else Modifier
                                ),
                            colors = CardDefaults.cardColors(containerColor = InstagramDarkSurface),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = option.resId),
                                    contentDescription = option.name,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(OctagonBadgeShape)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = option.name,
                                    color = if (isSelected) InstagramBlue else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Switches: Auto Reply & Block
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = InstagramDarkSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Auto Reply from User", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Simulate intelligent replies when you message", color = InstagramSubtext, fontSize = 12.sp)
                            }
                            Switch(
                                checked = autoReplyEnabled,
                                onCheckedChange = { autoReplyEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = InstagramBlue, checkedTrackColor = InstagramBlue.copy(alpha = 0.5f))
                            )
                        }

                        HorizontalDivider(color = InstagramDivider, modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Block md.sahil_sk_", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Toggle block status in DM interface", color = InstagramSubtext, fontSize = 12.sp)
                            }
                            Switch(
                                checked = isBlocked,
                                onCheckedChange = { isBlocked = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = InstagramRed, checkedTrackColor = InstagramRed.copy(alpha = 0.5f))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Profile Button
                Button(
                    onClick = {
                        val updated = currentProfile.copy(
                            name = name,
                            handle = handle,
                            joinedDate = joinedDate,
                            followersCount = followersCount,
                            postsCount = postsCount,
                            followingCount = followingCount,
                            followsYouText = followsYouText,
                            mutualFollowText = mutualFollowText,
                            bio = bio,
                            chatTimestamp = chatTimestamp,
                            autoReplyEnabled = autoReplyEnabled,
                            isBlocked = isBlocked,
                            avatarName = avatarName
                        )
                        onSaveProfile(updated)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Profile updated! Switched to live DM.")
                            onBackToDM()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("backend_save_profile_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save & Apply to DM",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {
                // ==================== TAB 1: CHAT MESSAGES MANAGER ====================
                Text(
                    text = "Add Custom Message into DM",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = InstagramDarkSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Sender:",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { newMsgSenderMe = true }
                                    .padding(end = 16.dp)
                            ) {
                                RadioButton(
                                    selected = newMsgSenderMe,
                                    onClick = { newMsgSenderMe = true },
                                    colors = RadioButtonDefaults.colors(selectedColor = InstagramBubblePurple)
                                )
                                Text("Me (Purple)", color = Color.White, fontSize = 13.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { newMsgSenderMe = false }
                            ) {
                                RadioButton(
                                    selected = !newMsgSenderMe,
                                    onClick = { newMsgSenderMe = false },
                                    colors = RadioButtonDefaults.colors(selectedColor = InstagramBlue)
                                )
                                Text("Sahil Sk (Dark)", color = Color.White, fontSize = 13.sp)
                            }
                        }

                        BackendTextField(
                            label = "Message Text",
                            value = newMsgText,
                            onValueChange = { newMsgText = it },
                            tag = "input_new_message_text"
                        )

                        BackendTextField(
                            label = "Timestamp (e.g. 12:42 PM)",
                            value = newMsgTimestamp,
                            onValueChange = { newMsgTimestamp = it },
                            tag = "input_new_message_timestamp"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (newMsgText.isNotBlank()) {
                                    onAddMessage(newMsgText.trim(), newMsgSenderMe, newMsgTimestamp)
                                    newMsgText = ""
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Message added to DM!")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("btn_add_message_to_dm")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add to Conversation", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Existing Messages (${messagesList.size})",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                messagesList.forEach { msg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = InstagramDarkSurface),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge indicating sender
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (msg.isFromMe) InstagramBubblePurple else InstagramBlue)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (msg.isFromMe) "You (Me)" else currentProfile.name,
                                        color = if (msg.isFromMe) InstagramBubblePurple else InstagramBlue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = msg.timestamp,
                                        color = InstagramPlaceholder,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.text.ifEmpty { "[${msg.type}]" },
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }

                            // Edit Message
                            IconButton(
                                onClick = {
                                    editingMessageId = msg.id
                                    editingMsgText = msg.text
                                    editingMsgTimestamp = msg.timestamp
                                    editingMsgIsMe = msg.isFromMe
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = InstagramSubtext,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Delete Message
                            IconButton(
                                onClick = {
                                    onDeleteMessage(msg.id)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Message deleted from DM")
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = InstagramRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Inline Message Edit Modal / Bottom Sheet
        if (editingMessageId != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = InstagramDarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Edit Message",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    BackendTextField(
                        label = "Text",
                        value = editingMsgText,
                        onValueChange = { editingMsgText = it },
                        tag = "edit_msg_text"
                    )

                    BackendTextField(
                        label = "Timestamp",
                        value = editingMsgTimestamp,
                        onValueChange = { editingMsgTimestamp = it },
                        tag = "edit_msg_timestamp"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { editingMessageId = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text("Cancel", color = InstagramSubtext)
                        }

                        Button(
                            onClick = {
                                editingMessageId?.let { id ->
                                    onEditMessage(id, editingMsgText, editingMsgTimestamp, editingMsgIsMe)
                                }
                                editingMessageId = null
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Message updated!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackendTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    tag: String,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            color = InstagramSubtext,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = InstagramBlue,
                unfocusedBorderColor = InstagramDivider,
                focusedContainerColor = InstagramInputBg,
                unfocusedContainerColor = InstagramInputBg
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(tag)
        )
    }
}
