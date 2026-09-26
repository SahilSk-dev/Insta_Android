package com.example.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object EmojiHelper {

    // Regex matching unicode emoji sequences including skin tones and zero width joiners
    val EMOJI_REGEX = Regex(
        "(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]|[\u2700-\u27BF]|[\u2B50]|[\u231A\u231B\u23E9-\u23EC\u23F0\u23F3]|[\uFE0F]|[\u200D])+"
    )

    fun isPureEmojiString(text: String): Boolean {
        if (text.isBlank()) return false
        val trimmed = text.trim()
        val match = EMOJI_REGEX.findAll(trimmed).joinToString("") { it.value }
        return match.replace(" ", "") == trimmed.replace(" ", "") && trimmed.length <= 32
    }

    sealed class TextSegment {
        data class PlainText(val text: String) : TextSegment()
        data class Emoji(val emoji: String) : TextSegment()
    }

    fun parseTextWithEmojis(text: String): List<TextSegment> {
        val segments = mutableListOf<TextSegment>()
        var lastIndex = 0
        val matches = EMOJI_REGEX.findAll(text)

        for (match in matches) {
            if (match.range.first > lastIndex) {
                segments.add(TextSegment.PlainText(text.substring(lastIndex, match.range.first)))
            }
            segments.add(TextSegment.Emoji(match.value))
            lastIndex = match.range.last + 1
        }

        if (lastIndex < text.length) {
            segments.add(TextSegment.PlainText(text.substring(lastIndex)))
        }

        return segments
    }
}

/**
 * Native System Emoji Text component:
 * Renders all emojis and characters with native Android system typography and rendering.
 */
@Composable
fun SystemEmojiText(
    emoji: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp
) {
    Text(
        text = emoji,
        fontSize = fontSize,
        fontFamily = FontFamily.Default,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/**
 * Universal text rendering with native Android system fonts:
 * Fast, pure system text and emoji rendering without any external image downloads or iOS skins.
 */
@Composable
fun FormattedEmojiText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: TextUnit = 15.sp,
    lineHeight: TextUnit = 20.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    emojiSize: Dp = 20.dp
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        fontFamily = FontFamily.Default,
        modifier = modifier
    )
}
