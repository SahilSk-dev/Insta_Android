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
import androidx.compose.material.icons.filled.ContentCopy
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
    onEditMessage: (id: String, newText: String, newTimestamp: String, isFromMe: Boolean) -> Unit,
    onDeleteMessage: (id: String) -> Unit,
    onReactEmoji: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isEditingMode by remember { mutableStateOf(false) }

    var editText by remember { mutableStateOf(message.text) }
    var editTimestamp by remember { mutableStateOf(message.timestamp) }
    var editIsFromMe by remember { mutableStateOf(message.isFromMe) }

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
                // Quick Reaction Bar
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
                        Text(
                            text = emoji,
                            fontSize = 28.sp,
                            modifier = Modifier
                                .clickable {
                                    onReactEmoji(emoji)
                                    onDismiss()
                                }
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

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
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = message.timestamp,
                                color = InstagramSubtext,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.text.ifEmpty { "[${message.type}]" },
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions List
                ActionRow(
                    icon = Icons.Default.Edit,
                    label = "Edit Message",
                    tint = Color.White,
                    onClick = { isEditingMode = true },
                    tag = "action_edit_message"
                )

                HorizontalDivider(color = InstagramDivider, thickness = 0.5.dp)

                ActionRow(
                    icon = Icons.Default.Delete,
                    label = "Remove / Delete Message",
                    tint = InstagramRed,
                    onClick = {
                        onDeleteMessage(message.id)
                        onDismiss()
                    },
                    tag = "action_delete_message"
                )
            } else {
                // Editing form
                Text(
                    text = "Edit Message",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Message Text",
                    color = InstagramSubtext,
                    fontSize = 12.sp
                )
                OutlinedTextField(
                    value = editText,
                    onValueChange = { editText = it },
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
                        .testTag("modal_edit_msg_text")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Timestamp",
                    color = InstagramSubtext,
                    fontSize = 12.sp
                )
                OutlinedTextField(
                    value = editTimestamp,
                    onValueChange = { editTimestamp = it },
                    singleLine = true,
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
                        .testTag("modal_edit_msg_timestamp")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Switch sender option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { editIsFromMe = !editIsFromMe }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch sender",
                        tint = InstagramBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (editIsFromMe) "Sender: Me (Purple)" else "Sender: Sahil Sk (Dark)",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { isEditingMode = false },
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramInputBg),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color.White)
                    }

                    Button(
                        onClick = {
                            onEditMessage(message.id, editText, editTimestamp, editIsFromMe)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = InstagramBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionRow(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
