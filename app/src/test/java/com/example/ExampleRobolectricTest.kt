package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.components.EmojiHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Direct Chat", appName)
  }

  @Test
  fun `test emoji string detection`() {
    val isEmoji = EmojiHelper.isPureEmojiString("❤️ 🔥 😂")
    assertTrue(isEmoji)
  }

  @Test
  fun `test text with emoji splitting for system font`() {
    val sample = "হ্যালো কেমন আছো 🔥 ভাইয়া"
    val segments = EmojiHelper.parseTextWithEmojis(sample)
    assertTrue(segments.any { it is EmojiHelper.TextSegment.PlainText })
    assertTrue(segments.any { it is EmojiHelper.TextSegment.Emoji })
  }
}
