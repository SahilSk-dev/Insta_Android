package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.ui.theme.InstagramBlue
import com.example.ui.theme.InstagramBubblePurple
import com.example.ui.theme.InstagramDarkSurface
import com.example.ui.theme.InstagramDivider
import com.example.ui.theme.InstagramInputBg
import com.example.ui.theme.InstagramRed
import com.example.ui.theme.InstagramSubtext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageActionBottomSheet(
    message: ChatMessageEntity,
    contactName: String = "Sahil",
    onEditMessage: (id: String, newText: String, newTimestamp: String, isFromMe: Boolean, theme: String) -> Unit,
    onDeleteMessage: (id: String) -> Unit,
    onReactEmoji: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isEditingMode by remember { mutableStateOf(false) }

    var editText by remember { mutableStateOf(message.text) }
    var editTimestamp by remember { mutableStateOf(message.timestamp) }
    var editIsFromMe by remember { mutableStateOf(message.isFromMe) }
    var selectedTheme by remember { mutableStateOf(message.theme) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = InstagramDarkSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            if (!isEditingMode) {
                // Quick Reaction Bar with System Emojis
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(InstagramInputBg)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val emojis = listOf("❤️", "😂", "🔥", "😮", "😢", "👍")
                    emojis.forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .clickable {
                                    onReactEmoji(emoji)
                                    onDismiss()
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 28.sp,
                                fontFamily = FontFamily.Default
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Apply / Remove Lyrics Theme & Overlay
                Text(
                    text = "Lyrics / Overlay Effects (Projapoti, Hearts, Neon):",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val themes = listOf(
                        "CLASSIC" to "Normal",
                        "OBSIDIAN_HEART" to "❤️‍🔥 Hearts",
                        "MIDNIGHT_BUTTERFLY" to "🦋 Butterfly",
                        "NEON_CYBER" to "⚡ Cyber",
                        "GOLDEN_LUXE" to "✨ Luxe"
                    )
                    themes.forEach { (thmKey, thmLabel) ->
                        val isSelected = message.theme == thmKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InstagramBlue else InstagramInputBg)
                                .clickable {
                                    onEditMessage(message.id, message.text, message.timestamp, message.isFromMe, thmKey)
                                    onDismiss()
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = thmLabel,
                                color = if (isSelected) Color.White else InstagramSubtext,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Message summary preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(InstagramInputBg)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (message.isFromMe) InstagramBubblePurple else InstagramBlue)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (message.isFromMe) "You (Me)" else "Received",
                                color = if (message.isFromMe) InstagramBubblePurple else InstagramBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Default
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message.timestamp,
                                color = InstagramSubtext,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Default
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = message.text.ifEmpty { "[${message.type}]" },
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action: Edit message
                ActionRowItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Message Content",
                    textColor = Color.White,
                    onClick = { isEditingMode = true }
                )

                HorizontalDivider(color = InstagramDivider, thickness = 0.5.dp)

                // Action: Switch sender (Me <-> Them)
                ActionRowItem(
                    icon = Icons.Default.SwapHoriz,
                    label = if (message.isFromMe) "Change Sender to $contactName" else "Change Sender to You",
                    textColor = Color.White,
                    onClick = {
                        onEditMessage(message.id, message.text, message.timestamp, !message.isFromMe, message.theme)
                        onDismiss()
                    }
                )

                HorizontalDivider(color = InstagramDivider, thickness = 0.5.dp)

                // Action: Delete message
                ActionRowItem(
                    icon = Icons.Default.Delete,
                    label = "Unsend / Delete Message",
                    textColor = InstagramRed,
                    onClick = {
                        onDeleteMessage(message.id)
                        onDismiss()
                    }
                )

            } else {
                // Inline editing mode inside sheet
                Text(
                    text = "Edit Message",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
                    label = { Text("Message Text", fontFamily = FontFamily.Default) },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = InstagramBlue,
                        unfocusedBorderColor = InstagramDivider,
                        focusedContainerColor = InstagramInputBg,
                        unfocusedContainerColor = InstagramInputBg
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = editTimestamp,
                    onValueChange = { editTimestamp = it },
                    label = { Text("Timestamp (e.g. 12:42 PM)", fontFamily = FontFamily.Default) },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = InstagramBlue,
                        unfocusedBorderColor = InstagramDivider,
                        focusedContainerColor = InstagramInputBg,
                        unfocusedContainerColor = InstagramInputBg
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Theme / Overlay Effect:",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Default
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val themes = listOf(
                        "CLASSIC" to "Normal",
                        "OBSIDIAN_HEART" to "❤️‍🔥 Hearts",
                        "MIDNIGHT_BUTTERFLY" to "🦋 Butterfly",
                        "NEON_CYBER" to "⚡ Cyber",
                        "GOLDEN_LUXE" to "✨ Luxe"
                    )
                    themes.forEach { (thmKey, thmLabel) ->
                        val isSelected = selectedTheme == thmKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) InstagramBlue else InstagramInputBg)
                                .clickable { selectedTheme = thmKey }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = thmLabel,
                                color = if (isSelected) Color.White else InstagramSubtext,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { isEditingMode = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("Cancel", color = InstagramSubtext, fontFamily = FontFamily.Default)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onEditMessage(message.id, editText, editTimestamp, editIsFromMe, selectedTheme)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Changes", color = Color.White, fontFamily = FontFamily.Default)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionRowItem(
    icon: ImageVector,
    label: String,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Default
        )
    }
}
