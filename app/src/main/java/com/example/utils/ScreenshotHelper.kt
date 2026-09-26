package com.example.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.coroutines.resume

object ScreenshotHelper {

    /**
     * Captures a high-resolution full bitmap from the Root Android Activity view safely.
     * Uses PixelCopy on API 26+ with canvas fallback to avoid hardware acceleration errors.
     */
    suspend fun captureActivityBitmap(activity: Activity): Bitmap? = withContext(Dispatchers.Main) {
        try {
            val window: Window = activity.window ?: return@withContext null
            val decorView: View = window.decorView.rootView ?: return@withContext null
            val width = decorView.width
            val height = decorView.height
            if (width <= 0 || height <= 0) return@withContext null

            // Try PixelCopy API first for accurate hardware-rendered Jetpack Compose screenshots
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val pixelCopySuccess = suspendCancellableCoroutine<Boolean> { cont ->
                    PixelCopy.request(
                        window,
                        bitmap,
                        { copyResult ->
                            cont.resume(copyResult == PixelCopy.SUCCESS)
                        },
                        Handler(Looper.getMainLooper())
                    )
                }

                if (pixelCopySuccess) {
                    return@withContext bitmap
                }
            }

            // Fallback to Software Canvas drawing
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.BLACK)
            decorView.draw(canvas)
            return@withContext bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            // Final fallback safe bitmap capture
            return@withContext try {
                val view = activity.window.decorView
                view.isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(view.drawingCache)
                view.isDrawingCacheEnabled = false
                bitmap
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }

    /**
     * Saves the captured chat screenshot bitmap into the device's Pictures / Downloads folder
     * and returns the resulting content URI or file path.
     */
    suspend fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        val filename = "Insta_Direct_Chat_${System.currentTimeMillis()}.png"
        var outputStream: OutputStream? = null
        var imageUri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DirectChat")
                }
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri != null) {
                    outputStream = resolver.openOutputStream(imageUri)
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                val dir = File(imagesDir, "DirectChat")
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, filename)
                outputStream = FileOutputStream(file)
                imageUri = Uri.fromFile(file)
            }

            outputStream?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            return@withContext imageUri
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback save to internal cache and return file URI
            try {
                val cacheFile = File(context.cacheDir, filename)
                FileOutputStream(cacheFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                return@withContext Uri.fromFile(cacheFile)
            } catch (ex: Exception) {
                return@withContext null
            }
        }
    }

    /**
     * Shares the screenshot image via system intent
     */
    fun shareScreenshot(context: Context, bitmap: Bitmap) {
        try {
            val cacheFile = File(context.cacheDir, "chat_screenshot_share.png")
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                cacheFile
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Chat Screenshot"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
