package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InstagramBlue
import com.example.ui.theme.InstagramDarkSurface
import com.example.ui.theme.InstagramDivider
import com.example.ui.theme.InstagramSubtext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickerPickerSheet(
    onEmojiInsert: (String) -> Unit,
    onSendDirect: ((String) -> Unit)? = null,
    onBackspace: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = InstagramDarkSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Emojis & Stickers",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default
                    )
                    Text(
                        text = "Tap to add emoji to text",
                        color = InstagramSubtext,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Default
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBackspace != null) {
                        IconButton(
                            onClick = onBackspace,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("emoji_keyboard_backspace")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Backspace",
                                tint = InstagramSubtext,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Category Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = InstagramDarkSurface,
                contentColor = Color.White,
                edgePadding = 12.dp,
                divider = { HorizontalDivider(color = InstagramDivider, thickness = 0.5.dp) },
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                        color = InstagramBlue,
                        height = 2.dp
                    )
                }
            ) {
                iosEmojiCategories.forEachIndexed { index, category ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = category.iconEmoji,
                                fontSize = 22.sp,
                                fontFamily = FontFamily.Default
                            )
                        },
                        modifier = Modifier.testTag("tab_emoji_${category.title}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // System Emoji Grid Keyboard
            val currentCategory = iosEmojiCategories[selectedCategoryIndex]
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 46.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(currentCategory.emojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .clickable {
                                onEmojiInsert(emoji)
                            }
                            .testTag("sticker_$emoji"),
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
        }
    }
}
