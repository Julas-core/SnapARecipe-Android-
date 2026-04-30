package com.example.snaprecipe

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.snaprecipe.camera.CameraManager
import com.example.snaprecipe.ui.Screens.ResultScreen
import com.example.snaprecipe.ui.model.RecipeLanguage
import com.example.snaprecipe.ui.theme.SnapRecipeTheme
import com.example.snaprecipe.viewmodel.MainViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val maxImageBytes = 15L * 1024 * 1024

    private var selectedImageBytes by mutableStateOf<ByteArray?>(null)
    private var selectedImageBase64 by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val cameraManager = remember { CameraManager(context) }
            var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }
            var pendingLanguage by remember { mutableStateOf(RecipeLanguage("English", "en")) }

            val takePictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicture()
            ) { success ->
                val targetUri = pendingCameraUri
                if (success && targetUri != null) {
                    handleImageUri(context, targetUri)
                } else if (!success) {
                    viewModel.setError("Camera capture canceled")
                }
            }

            val pickImageLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    handleImageUri(context, uri)
                }
            }

            SnapRecipeTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                ResultScreen(
                    resultText = viewModel.uiState,
                    imageBytes = selectedImageBytes,
                    recipe = viewModel.recipe,
                    onTakePhoto = { language ->
                        val imageFile = cameraManager.createImageFile()
                        val imageUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            imageFile
                        )
                        pendingLanguage = language
                        pendingCameraUri = imageUri
                        takePictureLauncher.launch(imageUri)
                    },
                    onUploadImage = { language ->
                        pendingLanguage = language
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onGenerateRecipe = { language ->
                        val base64 = selectedImageBase64
                        if (base64.isNullOrBlank()) {
                            viewModel.setError("Please choose an image first")
                            return@ResultScreen
                        }

                        viewModel.analyze(base64, language.label, language.code)
                    }
                )
            }
        }
    }

    private fun handleImageUri(context: Context, uri: Uri) {
        var imageSize = getImageSizeBytes(context, uri)
        var bytes: ByteArray? = null
        if (imageSize <= 0) {
            bytes = readBytes(context, uri)
            if (bytes == null) {
                viewModel.setError("Unable to read image data")
                return
            }
            imageSize = bytes.size.toLong()
        }

        if (imageSize > maxImageBytes) {
            bytes = compressImage(context, uri, imageSize)
            if (bytes == null || bytes.size > maxImageBytes) {
                viewModel.setError("Image must be 15 MB or smaller")
                return
            }
        }

        if (bytes == null) {
            bytes = readBytes(context, uri)
            if (bytes == null) {
                viewModel.setError("Unable to read image data")
                return
            }
        }

        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        selectedImageBytes = bytes
        selectedImageBase64 = base64
        viewModel.reset()
    }

    private fun getImageSizeBytes(context: Context, uri: Uri): Long {
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
                ?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        if (sizeIndex >= 0) {
                            val size = cursor.getLong(sizeIndex)
                            if (size > 0) {
                                return size
                            }
                        }
                    }
                }
        }

        return try {
            context.contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
        } catch (_: Exception) {
            val path = uri.path ?: return -1
            File(path).length()
        }
    }

    private fun readBytes(context: Context, uri: Uri): ByteArray? {
        val inputStream: InputStream = try {
            context.contentResolver.openInputStream(uri) ?: return null
        } catch (_: Exception) {
            return null
        }

        return inputStream.use { it.readBytes() }
    }

    private fun compressImage(context: Context, uri: Uri, originalSizeBytes: Long): ByteArray? {
        val sampleSize = calculateSampleSize(originalSizeBytes, maxImageBytes)
        val bitmap = decodeBitmap(context, uri, sampleSize) ?: return null
        val outputStream = ByteArrayOutputStream()
        val qualities = listOf(90, 80, 70, 60, 50, 40, 30)

        var result: ByteArray? = null
        for (quality in qualities) {
            outputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            val bytes = outputStream.toByteArray()
            if (bytes.size <= maxImageBytes) {
                result = bytes
                break
            }
        }

        bitmap.recycle()
        return result
    }

    private fun calculateSampleSize(originalSizeBytes: Long, maxBytes: Long): Int {
        val ratio = originalSizeBytes.toDouble() / maxBytes.toDouble()
        if (ratio <= 1.0) {
            return 1
        }

        val scale = ceil(log2(sqrt(ratio)))
        val sampleSize = 2.0.pow(scale).toInt()
        return sampleSize.coerceAtLeast(1)
    }

    private fun decodeBitmap(context: Context, uri: Uri, sampleSize: Int): Bitmap? {
        val boundsOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, boundsOptions)
        }

        if (boundsOptions.outWidth <= 0 || boundsOptions.outHeight <= 0) {
            return null
        }

        val options = BitmapFactory.Options().apply {
            inSampleSize = sampleSize
            inPreferredConfig = Bitmap.Config.RGB_565
        }

        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }
}